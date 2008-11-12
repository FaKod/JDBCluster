/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.metapersistence.aspects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import org.aspectj.lang.SoftException;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.FieldSignature;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.AssocCluster;
import org.jdbcluster.metapersistence.cluster.CSet;
import org.jdbcluster.metapersistence.cluster.Cluster;

/**
 * @author Christopher Schmidt
 * 1 to n relation CSet handling
 */
public aspect ClusterSet extends ClusterBaseAspect {

	public Set CSet.daoSet;

	public Class CSet.dAOClass;

	/**
	 * for all access to CSet attributes
	 */
	pointcut getCSet(Cluster c):
		get(CSet+ Cluster+.*) && target(c);

	/**
	 * for all adds to a CSet instance
	 */
	pointcut cSetAdd(CSet s):
		execution(* CSet+.add*(..)) && within(CSet) && target(s);

	/**
	 * for all removes from a CSet instance
	 */
	pointcut cSetRemove(CSet s):
		execution(* CSet+.remove*(..)) && within(CSet) && target(s);

	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(Cluster c):getCSet(c) {
		Object o = proceed(c);
		if (o != null)
			return o;

		// get JointPoint Method
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fJointPoint = sig.getField();

		// get type argument of parameterized type
		ParameterizedType retType = (ParameterizedType) fJointPoint.getGenericType();
		Class argTypeClass = (Class) retType.getActualTypeArguments()[0];

		// get the Set and Class Annotations
		DaoLink fieldAnno = fJointPoint.getAnnotation(DaoLink.class);

		try {
			// get set...(CSet) Method
			String setCSetName = argTypeClass.getSimpleName();
			setCSetName = "set" + setCSetName.substring(0, 1) + setCSetName.substring(1).toLowerCase();
			Method mSetCSet = c.getClass().getMethod(setCSetName, fJointPoint.getType());

			// create Cluster Set
			CSet cSet = (CSet) fJointPoint.getType().newInstance();

			// getting DAO Set
			String daoGetSetMethName = "get" + fieldAnno.dAOClass().getSimpleName();
			Method mDaoGetSet = c.getDaoClass().getMethod(daoGetSetMethName);
			Set dAOSet = (Set) mDaoGetSet.invoke(c.getDao());

			// copy DAO Set to Cluster Set
			cSet.daoSet = dAOSet;
			cSet.dAOClass = fieldAnno.dAOClass();

			// copy elements from DAO to Cluster
			for (Object daoElement : dAOSet) {
				Cluster cSetElement = (Cluster) argTypeClass.newInstance();
				cSetElement.setDao(daoElement);
				cSet.add(cSetElement);
			}

			// call setCSet(CSet...) Method
			mSetCSet.invoke(c, cSet);

			return cSet;

		} catch (Exception e) {
			throw new SoftException(e);
		}
	}

	@SuppressAjWarnings("adviceDidNotMatch")
	after(CSet s) returning:cSetAdd(s) {
		// get Cluster Object to Add
		Object[] args = thisJoinPoint.getArgs();
		AssocCluster objectToAdd = (AssocCluster) args[0];

		try {
			// get DAO Class
				Object dAO = s.dAOClass.newInstance();
				if(objectToAdd.getDao()==null) {
					objectToAdd.setDao(dAO);
					objectToAdd.setDaoClass(s.dAOClass);
				}
				s.daoSet.add(dAO);
		} catch (Exception e) {
			throw new SoftException(e);
		}
	}

	@SuppressAjWarnings("adviceDidNotMatch")
	after(CSet s) returning:cSetRemove(s) {
		// get Cluster Object to Add
		Object[] args = thisJoinPoint.getArgs();
		AssocCluster objectToRemove = (AssocCluster) args[0];

		s.daoSet.remove(objectToRemove.getDao());
		objectToRemove.setDao(null);
	}
}