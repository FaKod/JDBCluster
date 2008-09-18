/*
 * Copyright 2002-2005 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jdbcluster;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdbcluster.clustertype.ClusterTypeBase;
import org.jdbcluster.clustertype.ClusterTypeConfig;
import org.jdbcluster.dao.Dao;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.springframework.util.Assert;

/**
 * Utility class mainly for reflection stuff
 * 
 * @author FaKod
 * @author Thomas Bitzer
 */
public abstract class JDBClusterUtil {

	/** Logger available to subclasses */
	static final Logger logger = Logger.getLogger(JDBClusterUtil.class);

	/** map with the dao class and the corresponding cluster ids */
	private static Map<Class<? extends Dao>, Set<String>> dao2clusterId = null;

	/**
	 * returns the corresponding cluster id for a dao class
	 * 
	 * @param clazz
	 *            the dao class
	 * @return the cluster id. Can be {@code null}
	 */
	static public String getClusterId(Class<? extends Dao> clazz) {
		if (dao2clusterId == null) {
			fillDao2ClusterId();
		}

		Set<String> clusterIds = dao2clusterId.get(clazz);
		if (clusterIds == null || clusterIds.isEmpty()) {
			return null;
		} else if (clusterIds.size() == 1) {
			return clusterIds.iterator().next();
		} else {
			throw new ConfigurationException(
					"several clusters defined using this dao ["
							+ clazz.getName() + "]. Not supported yet");
		}
	}

	/** creates the map linking the DAO class to the cluster definition */
	@SuppressWarnings("unchecked")
	private static void fillDao2ClusterId() {
		ClusterTypeConfig config = ClusterTypeBase.getClusterTypeConfig();

		if (config == null) {
			throw new ConfigurationException("no clusterTypeConfig available");
		}

		dao2clusterId = new HashMap<Class<? extends Dao>, Set<String>>();

		List<String> ids = config.getClusterIDs();
		for (String id : ids) {
			Class<? extends Cluster> clazz = (Class<? extends Cluster>) createClass(config
					.getClusterClassName(id));
			DaoLink daoLink = clazz.getAnnotation(DaoLink.class);
			if (daoLink == null) {
				throw new ConfigurationException(
						"cluster defined in configuration file without DaoLink: id ["
								+ id + "] and class [" + clazz + "]");
			}

			Class daoClazz = daoLink.dAOClass();
			Set<String> clusterIds = dao2clusterId.get(daoClazz);
			if (clusterIds == null) {
				clusterIds = new HashSet<String>();
			}
			clusterIds.add(id);
			dao2clusterId.put(daoClazz, clusterIds);
		}
	}

	/**
	 * creates object from class named className
	 * 
	 * @param className
	 *            name of the class
	 * @return Object instance of class className
	 */
	static public Object createClassObject(String className) {
		return createClassObject(createClass(className));
	}

	/**
	 * creates Class<?> from class named className
	 * 
	 * @param className
	 *            name of the class
	 * @return Class instance of class className
	 */
	static public Class<?> createClass(String className) {

		Assert.hasLength(className, "className may not be null or \"\"");

		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("no definition for the class ["
					+ className + "] with the specified name could be found", e);
		}
	}

	/**
	 * @see createClassObject(String className) Creates Class from class object
	 * @param clazz
	 *            Class Object
	 * @return created instance
	 */
	static public Object createClassObject(Class<?> clazz) {

		Assert.notNull(clazz, "clazz may not be null");

		String className = clazz.getName();
		try {
			Constructor ctor = clazz.getDeclaredConstructor();
			Object o = ctor.newInstance();
			return o;
		} catch (InstantiationException e) {
			throw new ConfigurationException(
					"specified class ["
							+ className
							+ "] object cannot be instantiated because it is an interface or is an abstract class",
					e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(
					"the currently executed ctor for class [" + className
							+ "] does not have access", e);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access class [" + className
					+ "] with the specified name", e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException(
					"number of actual and formal parameters differ for the class ["
							+ className, e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException(
					"the underlying constructor of the class [" + className
							+ "] throws an exception", e);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("method of configured class ["
					+ className + "]  could not be found", e);
		}
	}

	/**
	 * calles a getter method on instance obj. Iterates over all superclasses
	 * 
	 * @param propName
	 *            name of the property
	 * @param obj
	 *            instance of property
	 * @return Object property value
	 */
	static public Object invokeGetPropertyMethod(String propName, Object obj) {

		Assert.notNull(obj, "obj may not be null");
		Assert.hasLength(propName, "obj may not be null or \"\"");

		String getMethName = "get" + propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		try {
			Method mGet = JDBClusterUtil.getMethod(obj, getMethName,
					(Class[]) null);
			return mGet.invoke(obj);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property ["
					+ propName + "] with the specified name in "
					+ obj.getClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException(
					"number of actual and formal parameters differ for the property ["
							+ propName + " in " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(
					"the currently executed ctor for property [" + propName
							+ "] does not have access in "
							+ obj.getClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException(
					"the underlying constructor of the property [" + propName
							+ "] throws an exception in "
							+ obj.getClass().getName(), e);
		}
	}

	/**
	 * calles a setter on instance obj. Iterates over all superclasses
	 * 
	 * @param propName
	 *            property name
	 * @param propValue
	 *            value to set
	 * @param obj
	 *            instance with the setter
	 */
	static public void invokeSetPropertyMethod(String propName,
			Object propValue, Object obj) {

		Assert.notNull(obj, "obj may not be null");
		Assert.notNull(propValue, "propValue may not be null");
		Assert.hasLength(propName, "obj may not be null or \"\"");

		String setMethName = "set" + propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		Object[] args = { propValue };
		Class[] paramType = { propValue.getClass() };
		try {
			Method mSet = JDBClusterUtil.getMethod(obj, setMethName, paramType);
			mSet.invoke(obj, args);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property ["
					+ propName + "] with the specified name in "
					+ obj.getClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException(
					"number of actual and formal parameters differ for the property ["
							+ propName + " in " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(
					"the currently executed ctor for property [" + propName
							+ "] does not have access in "
							+ obj.getClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException(
					"the underlying constructor of the property [" + propName
							+ "] throws an exception in "
							+ obj.getClass().getName(), e);
		}
	}

	/**
	 * returnes property value directly from field. Iterates over all
	 * superclasses
	 * 
	 * @param propName
	 *            name of property
	 * @param obj
	 *            instance of object
	 * @return Object the property value
	 */
	static public Object getProperty(String propName, Object obj) {

		Assert.notNull(obj, "obj may not be null");
		Assert.hasLength(propName, "obj may not be null or \"\"");

		try {
			Field f = JDBClusterUtil.getField(propName, obj);
			return f.get(obj);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property ["
					+ propName + "] with the specified name in "
					+ obj.getClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException(
					"number of actual and formal parameters differ for the property ["
							+ propName + "] in " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(
					"the currently executed ctor for property [" + propName
							+ "] does not have access in "
							+ obj.getClass().getName(), e);
		}
	}

	/**
	 * returns Filed object for Properties. Iterates over all superclasses
	 * 
	 * @param o
	 *            Object
	 * @param propName
	 *            path to property
	 * @return Field instance
	 */
	static public Field getField(String propName, Object o) {
		return JDBClusterUtil.getField(propName, o.getClass());
	}

	/**
	 * returns Filed object for Properties. Iterates over all superclasses Find
	 * a Field with the given Field name and the given parameter types, declared
	 * on the given class or one of its superclasses. Prefers public Field, but
	 * will return a protected, package access, or private Field too.
	 * <p>
	 * Checks <code>Class.getField</code> first, falling back to
	 * <code>getDeclaredField</code>.
	 * 
	 * @param propName
	 *            path to property
	 * @param c
	 *            Class object
	 * @return Field instance
	 */
	static public Field getField(String propName, Class clazz) {

		Assert.notNull(clazz, "clazz may not be null");
		Assert.hasLength(propName, "obj may not be null or \"\"");

		Field f = null;
		try {
			f = clazz.getField(propName);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get field for property ["
					+ propName + "] with the specified name for "
					+ clazz.getName(), e);
		} catch (NoSuchFieldException e) {
			return JDBClusterUtil.getDeclaredField(propName, clazz);
		}
		return f;
	}

	/**
	 * returns Field object for Properties. Iterates over all superclasses Find
	 * a Field with the given Field name and the given parameter types, declared
	 * on the given class or one of its superclasses. Will return a protected,
	 * package access, or private Field too.
	 * <p>
	 * Checks <code>getDeclaredField</code>.
	 * 
	 * @param propName
	 *            path to property
	 * @param c
	 *            Class object
	 * @return Field instance
	 */
	static public Field getDeclaredField(String propName, Class clazz) {

		Assert.notNull(clazz, "clazz may not be null");
		Assert.hasLength(propName, "obj may not be null or \"\"");

		Field f = null;
		try {
			f = clazz.getDeclaredField(propName);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get field for property ["
					+ propName + "] with the specified name for "
					+ clazz.getName(), e);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null) {
				return JDBClusterUtil.getDeclaredField(propName, clazz
						.getSuperclass());
			}
			throw new ConfigurationException("cant get field for property ["
					+ propName + "] with the specified name  for "
					+ clazz.getName(), e);
		}
		return f;
	}

	/**
	 * calculates method object. Iterates over all superclasses
	 * 
	 * @param o
	 *            Object
	 * @param methodName
	 *            method name
	 * @param parameterTypes
	 *            parameter types of method
	 * @return Method
	 */
	static public Method getMethod(Object o, String methodName,
			Class... parameterTypes) {
		return JDBClusterUtil.getMethod(o.getClass(), methodName,
				parameterTypes);
	}

	/**
	 * calculates method object. Iterates over all superclasses Find a method
	 * with the given method name and the given parameter types, declared on the
	 * given class or one of its superclasses. Prefers public methods, but will
	 * return a protected, package access, or private method too.
	 * <p>
	 * Checks <code>Class.getMethod</code> first, falling back to
	 * <code>getDeclaredMethod</code>.
	 * 
	 * @param clazz
	 *            Class of Object
	 * @param methodName
	 *            method name
	 * @param parameterTypes
	 *            parameter types of method
	 * @return
	 */
	static public Method getMethod(Class clazz, String methodName,
			Class... parameterTypes) {

		Assert.notNull(clazz, "clazz may not be null");
		Assert.hasLength(methodName, "methodName may not be null or \"\"");

		Method m = null;
		try {
			m = clazz.getMethod(methodName, parameterTypes);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get Method for method ["
					+ methodName + "] with the specified name", e);
		} catch (NoSuchMethodException e) {
			return JDBClusterUtil.getDeclaredMethod(clazz, methodName,
					parameterTypes);
		}
		return m;
	}

	/**
	 * calculates method object. Iterates over all superclasses. Find a method
	 * with the given method name and the given parameter types, declared on the
	 * given class or one of its superclasses. Will return a public, protected,
	 * package access, or private method.
	 * <p>
	 * Checks <code>Class.getDeclaredMethod</code>, cascading upwards to all
	 * superclasses.
	 * 
	 * @param clazz
	 *            Class of Object
	 * @param methodName
	 *            method name
	 * @param parameterTypes
	 *            parameter types of method
	 * @return
	 */
	static public Method getDeclaredMethod(Class clazz, String methodName,
			Class... parameterTypes) {

		Assert.notNull(clazz, "clazz may not be null");
		Assert.hasLength(methodName, "methodName may not be null or \"\"");

		Method m = null;
		try {
			m = clazz.getDeclaredMethod(methodName, parameterTypes);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get Method for method ["
					+ methodName + "] with the specified name", e);
		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() != null) {
				return JDBClusterUtil.getDeclaredMethod(clazz.getSuperclass(),
						methodName, parameterTypes);
			}
			throw new ConfigurationException("cant get Method for method ["
					+ methodName + "] with the specified name", e);
		}
		return m;
	}

	/**
	 * tries to determine the best fitting method for a set of parameter types.
	 * Uses getMethod() first, if there is no direct match every parameter is
	 * checkt if there is a method with a superclass match. etc
	 * 
	 * @see #getMethodBestParameterFit(Class, String, Class[])
	 * @param o
	 *            Object of the class
	 * @param methodName
	 *            name of the method to find
	 * @param parameterTypes
	 *            parameter types of method
	 * @return Method to find
	 */
	static public Method getMethodBestParameterFit(Object o, String methodName,
			Class... parameterTypes) {
		return getMethodBestParameterFit(o.getClass(), methodName,
				parameterTypes);
	}

	/**
	 * Tries to determine the best fitting method for a set of parameter types.
	 * Uses getMethod() first, if there is no direct match every parameter is
	 * checked if there is a method with a superclass or superinterface match.
	 * etc. For this best fitting method a scoring is used. It counts for every
	 * parameter the "distance" from the class to the requested superclass or
	 * superinterface. These "distances" are cummulated to one value. The method
	 * with the lowest scoring value is returned.
	 * 
	 * @see #getMethod(Class, String, Class[])
	 * @param clazz
	 *            Class of Object
	 * @param methodName
	 *            name of the method to find
	 * @param parameterTypes
	 *            parameter types of method
	 * @return Method to find
	 */
	static public Method getMethodBestParameterFit(Class clazz,
			String methodName, Class... parameterTypes) {

		/*
		 * first try the normal way (exact match)
		 */
		try {
			return JDBClusterUtil.getMethod(clazz, methodName, parameterTypes);
		} catch (ConfigurationException e) {
			if (!(e.getCause() instanceof NoSuchMethodException))
				throw e;
		}

		/*
		 * get all methods and select only equal name and parameter length in
		 * mList
		 */
		Method[] mAll = clazz.getMethods();
		List<Method> mList = new ArrayList<Method>();

		for (Method m : mAll) {
			if (m.getParameterTypes().length == parameterTypes.length
					&& m.getName().equals(methodName))
				mList.add(m);
		}

		if (mList.size() > 1)
			logger
					.warn("possible ambiguity. Found more than one method with name ["
							+ methodName + "]. " + "trying best fit method");

		/*
		 * check if parameter superclasses do fit for all above selected methods
		 */
		HashMap<Method, Integer> methodScore = new HashMap<Method, Integer>();
		for (Method m : mList) {
			Class<?>[] mParameterTypes = m.getParameterTypes();

			for (int i = 0; i < parameterTypes.length; i++) {
				int count = countSuper(mParameterTypes[i], parameterTypes[i]);
				Integer oldInt = methodScore.get(m);
				if (oldInt == null)
					oldInt = 0;
				methodScore.put(m, oldInt + count);
				if (count < 0) {
					methodScore.put(m, -1);
					break; // if there is no match this method is not fitting
				}
			}
		}

		/*
		 * evaluate scoring
		 */
		Method mResult = null;
		int resultScore = Integer.MAX_VALUE;
		for (Method m : mList) {
			int score = methodScore.get(m);
			if (score != -1) {
				if (score < resultScore) {
					resultScore = score;
					mResult = m;
				}
			}
		}

		if (mResult == null)
			throw new ConfigurationException(
					"cant get MethodBestParameterFit for method [" + methodName
							+ "] with the specified name");

		return mResult;
	}

	/**
	 * @param superClass
	 *            Class or Interface to look for
	 * @param clazz
	 *            the class instance
	 * @return -1 for not found. Counter for supercasses above parameter
	 *         superClass
	 */
	static private int countSuper(Class<?> superClass, Class<?> clazz) {
		if (superClass.isInterface())
			return countSuperInterface(0, superClass, clazz.getInterfaces());
		else
			return countSuperClass(0, superClass, clazz);
	}

	/**
	 * tries to validate class instances through Super Classes
	 * 
	 * @param count
	 *            recursive parameter. Use 0 for the first call
	 * @param superClass
	 *            the Superclass to validate against
	 * @param clazz
	 *            the class instance
	 * @return -1 for not found. Counter for supercasses above parameter
	 *         superClass
	 */
	static private int countSuperClass(int count, Class<?> superClass,
			Class<?> clazz) {
		if (clazz == null)
			return -1;
		if (superClass == clazz)
			return count;
		return countSuperClass(++count, superClass, clazz.getSuperclass());
	}

	/**
	 * tries to validate interface instances through Super Interfaces
	 * 
	 * @param count
	 *            recursive parameter. Use 0 for the first call
	 * @param superInterface
	 * @param interfaceClasses
	 * @return -1 for not found. Counter for superInterfaces above parameter
	 *         superInterface
	 */
	static private int countSuperInterface(int count, Class<?> superInterface,
			Class<?>[] interfaceClasses) {
		int ret = 0;

		if (interfaceClasses == null)
			return -1;

		for (Class<?> c : interfaceClasses) {
			if (superInterface == c)
				return count;
		}
		++count;
		for (Class<?> c : interfaceClasses) {
			ret = countSuperInterface(count, superInterface, c.getInterfaces());
			if (ret != -1)
				break;
		}
		return ret;
	}
}
