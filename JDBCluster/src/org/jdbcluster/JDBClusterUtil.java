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
 * 
 * @author Christopher Schmdt
 * @author Thomas Bitzer
 * Utility class mainly for reflection stuff
 */
public class JDBClusterUtil {
	
	static public Object createClassObject(String className) {
		try {
			Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
			return createClassObject(clazz);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("no definition for the class [" + className + "] with the specified name could be found", e);
		}
	}
	
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
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("method of configured class [" + className + "]  could not be found", e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("number of actual and formal parameters differ for the class [" + className, e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException("the underlying constructor of the class [" + className + "] throws an exception", e);
		}
	}
	
	static public Object invokeGetPropertyMethod(String propName, Object obj) {
		String getMethName = "get" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
		try {
			Method mGet = obj.getClass().getMethod(getMethName);
			return mGet.invoke(obj);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property [" + propName + "] with the specified name", e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("number of actual and formal parameters differ for the property [" + propName, e);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("method of configured property [" + propName + "]  could not be found", e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for property [" + propName + "] does not have access", e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException("the underlying constructor of the property [" + propName + "] throws an exception", e);
		}
	}
	
	static public void invokeSetPropertyMethod(String propName, Object propValue, Object obj) {
        String setMethName = "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
        Object[] args = {propValue};
        Class[] paramType = {propValue.getClass()};
        try {
                    Method mSet = obj.getClass().getMethod(setMethName, paramType);
                    mSet.invoke(obj, args);
        } catch (SecurityException e) {
                    throw new ConfigurationException("cant access property [" + propName + "] with the specified name", e);
        } catch (IllegalArgumentException e) {
                    throw new ConfigurationException("number of actual and formal parameters differ for the property [" + propName, e);
        } catch (NoSuchMethodException e) {
                    throw new ConfigurationException("method of configured property [" + propName + "]  could not be found", e);
        } catch (IllegalAccessException e) {
                    throw new ConfigurationException("the currently executed ctor for property [" + propName + "] does not have access", e);
        } catch (InvocationTargetException e) {
                    throw new ConfigurationException("the underlying constructor of the property [" + propName + "] throws an exception", e);
        }
	}           


	
	static public Object getProperty(String propName, Object obj) {
		try {
			Field f = obj.getClass().getDeclaredField(propName);
			return f.get(obj);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant access property [" + propName + "] with the specified name", e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException("number of actual and formal parameters differ for the property [" + propName, e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for property [" + propName + "] does not have access", e);
		} catch (NoSuchFieldException e) {
			throw new ConfigurationException("configured property [" + propName + "]  could not be found", e);
		}
	}
	
	/**
	 * returns Filed object for Properties
	 * @param o Object
	 * @param propName path to property
	 * @return Field instance
	 */
	static public Field getField(String propName, Object o) {
		Field f = null;
		try {
			f = o.getClass().getDeclaredField(propName);
		} catch (SecurityException e) {
			throw new ConfigurationException("cant get field for property [" + propName + "] with the specified name", e);
		} catch (NoSuchFieldException e) {
			throw new ConfigurationException("cant get field for property [" + propName + "] with the specified name", e);
		}
		return f;
	}
}
