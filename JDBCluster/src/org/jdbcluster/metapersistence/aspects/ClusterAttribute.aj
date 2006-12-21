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

import org.aspectj.lang.SoftException;
import org.aspectj.lang.reflect.FieldSignature;
import org.aspectj.lang.annotation.*;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.metapersistence.cluster.*;
import org.jdbcluster.metapersistence.annotation.*;
import org.jdbcluster.exception.ConfigurationException;

/**
 * @author Christopher Schmidt
 * Aspect for attribute access
 */
public aspect ClusterAttribute extends ClusterBaseAspect {

	pointcut getAttribute(ClusterBase c):
		(get(* Cluster+.*) || get(* AssocCluster+.*)) && 
		!get(CSet+ Cluster+.*) && !@annotation(NoDAO) && target(c);

	pointcut setAttribute(ClusterBase c):
		(set(* Cluster+.*) || set(* AssocCluster+.*)) &&
		!set(CSet+ Cluster+.*) && !@annotation(NoDAO) && target(c);

	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(ClusterBase c):getAttribute(c) {
		// get attribute name
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fField = sig.getField();
		String fName = fField.getName();

		try {
			// and invoke it and set Clusters field
			fField.set(c, JDBClusterUtil.invokeGetPropertyMethod(fName, c.getDao()));
		}
		catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for Field [" + fName + "] does not have access", e);
		}
		return proceed(c);
	}

	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(ClusterBase c):setAttribute(c) {
		Object o = proceed(c);
		// get attribute name
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fField = sig.getField();
		String fName = fField.getName();

		try {
			// get get-method
			String setMethName = "set" + fName.substring(0, 1).toUpperCase() + fName.substring(1);
			Method mSet = c.getDaoClass().getMethod(setMethName, fField.getType());
			// read clusters field and invoke DAOs SetMethod
			mSet.invoke(c.getDao(), fField.get(c));
		} catch (Exception e) {
			throw new SoftException(e);
		}
		return o;
	}
}
