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
package org.jdbcluster;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jdbcluster.exception.ConfigurationException;

/**
 * Utility class mainly for reflection stuff
 * 
 * @author FaKod
 * @author Thomas Bitzer
 */
public abstract class JDBClusterUtil {

	/**
	 * creates object from class named className
	 * 
	 * @param className name of the class
	 * @return Object instance of class className
	 */
	static public Object createClassObject(String className) {
		try {
			Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
			return createClassObject(clazz);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("no definition for the class [" + className + "] with the specified name could be found", e);
		}
	}

	/**
	 * @see createClassObject(String className) Creates Class from class object
	 * @param clazz Class Object
	 * @return created instance
	 */
	static public Object createClassObject(Class<?> clazz) {

		String className = clazz.getName();
		try {
			Constructor ctor = clazz.getDeclaredConstructor();
			Object o = ctor.newInstance();
			return o;
		} catch (InstantiationException e) {
			throw new ConfigurationException("specified class [" + className + "] object cannot be instantiated because it is an interface or is an abstract class", e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for class [" + className + "] does not have access", e);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access class [" + className + "] with the specified name", e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("number of actual and formal parameters differ for the class [" + className, e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException("the underlying constructor of the class [" + className + "] throws an exception", e);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("method of configured class [" + className + "]  could not be found", e);
		}
	}

	/**
	 * calles a getter method on instance obj. Iterates over all superclasses
	 * 
	 * @param propName name of the property
	 * @param obj instance of property
	 * @return Object property value
	 */
	static public Object invokeGetPropertyMethod(String propName, Object obj) {
		String getMethName = "get" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
		try {
			Method mGet = getMethod(obj, getMethName, (Class[]) null);
			return mGet.invoke(obj);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property [" + propName + "] with the specified name in " + obj.getClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("number of actual and formal parameters differ for the property [" + propName + " in " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for property [" + propName + "] does not have access in " + obj.getClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException("the underlying constructor of the property [" + propName + "] throws an exception in " + obj.getClass().getName(), e);
		}
	}

	/**
	 * calles a setter on instance obj. Iterates over all superclasses
	 * 
	 * @param propName property name
	 * @param propValue value to set
	 * @param obj instance with the setter
	 */
	static public void invokeSetPropertyMethod(String propName, Object propValue, Object obj) {
		String setMethName = "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
		Object[] args = { propValue };
		Class[] paramType = { propValue.getClass() };
		try {
			Method mSet = getMethod(obj, setMethName, paramType);
			mSet.invoke(obj, args);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property [" + propName + "] with the specified name in " + obj.getClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("number of actual and formal parameters differ for the property [" + propName + " in " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for property [" + propName + "] does not have access in " + obj.getClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException("the underlying constructor of the property [" + propName + "] throws an exception in " + obj.getClass().getName(), e);
		}
	}

	/**
	 * returnes property value directly from field. Iterates over all superclasses
	 * 
	 * @param propName name of property
	 * @param obj instance of object
	 * @return Object the property value
	 */
	static public Object getProperty(String propName, Object obj) {
		try {
			Field f = getField(propName, obj);
			return f.get(obj);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property [" + propName + "] with the specified name in " + obj.getClass().getName(), e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("number of actual and formal parameters differ for the property [" + propName + "] in " + obj.getClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for property [" + propName + "] does not have access in " + obj.getClass().getName(), e);
		} 
	}

	/**
	 * returns Filed object for Properties. Iterates over all superclasses
	 * 
	 * @param o Object
	 * @param propName path to property
	 * @return Field instance
	 */
	static public Field getField(String propName, Object o) {
		Field f = null;
		Class clazz = o.getClass();
		try {
			f = clazz.getDeclaredField(propName);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get field for property [" + propName + "] with the specified name", e);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null) {
				return getField(propName, clazz.getSuperclass());
			}
			throw new ConfigurationException("cant get field for property [" + propName + "] with the specified name", e);
		}
		return f;
	}

	/**
	 * returns Filed object for Properties. Iterates over all superclasses
	 * 
	 * @param propName path to property
	 * @param c Class object
	 * @return Field instance
	 */
	static public Field getField(String propName, Class clazz) {
		Field f = null;
		try {
			f = clazz.getDeclaredField(propName);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get field for property [" + propName + "] with the specified name for " + clazz.getName(), e);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null) {
				return getField(propName, clazz.getSuperclass());
			}
			throw new ConfigurationException("cant get field for property [" + propName + "] with the specified name  for " + clazz.getName(), e);
		}
		return f;
	}

	/**
	 * calculates method object. Iterates over all superclasses
	 * 
	 * @param o Object
	 * @param methodName method name
	 * @param parameterTypes parameter types of method
	 * @return Method
	 */
	static public Method getMethod(Object o, String methodName, Class... parameterTypes) {
		Method m = null;
		Class clazz = o.getClass();
		try {
			m = clazz.getDeclaredMethod(methodName, parameterTypes);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get Method for method [" + methodName + "] with the specified name", e);
		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() != null) {
				return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
			}
			throw new ConfigurationException("cant get Method for method [" + methodName + "] with the specified name", e);
		}
		return m;
	}
	
	/**
	 * calculates method object. Iterates over all superclasses
	 * 
	 * @param clazz Class of Object
	 * @param methodName method name
	 * @param parameterTypes parameter types of method
	 * @return
	 */
	static public Method getMethod(Class clazz, String methodName, Class... parameterTypes) {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(methodName, parameterTypes);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get Method for method [" + methodName + "] with the specified name", e);
		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() != null) {
				return getMethod(clazz.getSuperclass(), methodName, parameterTypes);
			}
			throw new ConfigurationException("cant get Method for method [" + methodName + "] with the specified name", e);
		}
		return m;
	}
}
