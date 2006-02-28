package org.jdbcluster.metapersistence.aspects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aspectj.lang.SoftException;
import org.aspectj.lang.reflect.FieldSignature;

import org.jdbcluster.metapersistence.cluster.CSet;
import org.jdbcluster.metapersistence.cluster.*;
import org.jdbcluster.metapersistence.annotation.*;

/**
 * @author Christopher Schmidt
 * Aspect for attribute access
 */
public aspect CAttributeAspect extends CAspect {

	pointcut getAttribute(ClusterBase c):
		(get(* Cluster+.*) || get(* AssocCluster+.*)) && 
		!get(CSet+ Cluster+.*) && !@annotation(NoDAO) && target(c);

	pointcut setAttribute(ClusterBase c):
		(set(* Cluster+.*) || set(* AssocCluster+.*)) && 
		!set(CSet+ Cluster+.*) && !@annotation(NoDAO) && target(c);

	Object around(ClusterBase c):getAttribute(c) {
		// get attribute name
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fField = sig.getField();
		String fName = fField.getName();

		try {
			// get DAOs GetMethod
			String getMethName = "get" + fName.substring(0, 1).toUpperCase() + fName.substring(1);
			Method mGet = c.daoClass.getMethod(getMethName);
			// and invoke it and set Clusters field
			fField.set(c, mGet.invoke(c.dao));
		} catch (Exception e) {
			throw new SoftException(e);
		}
		return proceed(c);
	}

	Object around(ClusterBase c):setAttribute(c) {
		Object o = proceed(c);
		// get attribute name
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fField = sig.getField();
		String fName = fField.getName();

		try {
			// get get-method
			String setMethName = "set" + fName.substring(0, 1).toUpperCase() + fName.substring(1);
			Method mSet = c.daoClass.getMethod(setMethName, fField.getType());
			// read clusters field and invoke DAOs SetMethod
			mSet.invoke(c.dao, fField.get(c));
		} catch (Exception e) {
			throw new SoftException(e);
		}
		return o;
	}
}
