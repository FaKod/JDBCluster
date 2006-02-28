package org.jdbcluster;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jdbcluster.exception.ConfigurationException;

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
}
