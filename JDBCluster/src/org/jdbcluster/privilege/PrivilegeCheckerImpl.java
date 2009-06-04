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
package org.jdbcluster.privilege;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.domain.DomainChecker;
import org.jdbcluster.domain.DomainCheckerImpl;
import org.jdbcluster.domain.DomainPrivilegeList;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.PrivilegesCluster;
import org.jdbcluster.metapersistence.annotation.PrivilegesDomain;
import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;
import org.jdbcluster.metapersistence.annotation.PrivilegesParameter;
import org.jdbcluster.metapersistence.annotation.PrivilegesService;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.security.user.IUser;
import org.jdbcluster.service.PrivilegedService;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.util.Assert;

/**
 * Main Class for handling privileges
 * 
 * @author FaKod
 */
public class PrivilegeCheckerImpl extends PrivilegeBase implements PrivilegeChecker {

	/** Logger available to subclasses */
	protected final Logger logger = Logger.getLogger(getClass());
	
	private static PrivilegeChecker instance;

	/**
	 * always reused instance of springs internal bean wrapper class for Cluster
	 * stuff
	 */
	private static BeanWrapperImpl beanWrapper = new BeanWrapperImpl(true);

	/**
	 * always reused instance of springs internal bean wrapper class for Service
	 * stuff
	 */
	private static BeanWrapperImpl serviceBeanWrapper = new BeanWrapperImpl(true);

	/**
	 * maps: Class -> Method -> HashSet of privileges
	 */
	private static HashMap<Class<?>, HashMap<Method, HashSet<String>>> privilegesCluster = new HashMap<Class<?>, HashMap<Method, HashSet<String>>>();

	/**
	 * maps: Class -> Service Method -> HashSet of privileges
	 */
	private static HashMap<Class<?>, HashMap<Method, HashSet<String>>> privilegesService = new HashMap<Class<?>, HashMap<Method, HashSet<String>>>();

	/**
	 * C-Tor to use Factory method
	 */
	private PrivilegeCheckerImpl() {
	}

	/**
	 * public intance gettet
	 * 
	 * @return PrivilegeChecker
	 */
	public static PrivilegeChecker getInstance() {
		if(instance == null) {
			instance = new PrivilegeCheckerImpl();
		}
		return instance;
	}

	
	/**
	 * intersects required static privileges for cluster new against given
	 * privileges
	 * 
	 * @param clusterType defines the Cluster to check
	 * @return true if the privileges are sufficient
	 */
	public boolean checkClusterNew(IUser user, String clusterType) {

		Assert.hasLength(clusterType, "clusterType may not be null or \"\"");

		Class<? extends Cluster> clazz = ClusterTypeFactory.newInstance(clusterType).getClusterClass();

		// if Cluster is no PrivilegedCluster creation is always possible
		if (!PrivilegedCluster.class.isAssignableFrom(clazz))
			return true;

		Class<? extends PrivilegedCluster> clusterClass = clazz.asSubclass(PrivilegedCluster.class);
		Set<String> requiredPrivileges = calcStaticClusterPrivileges(clusterClass);
		return userPrivilegeIntersect(user, requiredPrivileges);
	}

	/**
	 * should do an intersection between the user privileges and the privileges
	 * given through requiredPrivileges
	 * 
	 * @param requiredPrivileges the required privileges
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, Set<String> requiredPrivileges) {
		return getUserPrivilege().userPrivilegeIntersect(user,requiredPrivileges);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param clusterObject
	 * @param methodName method name to check
	 * @param args of method parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedCluster clusterObject, String methodName, Object... args) {

		Assert.notNull(clusterObject, "clusterObject may not be null");
		Assert.hasLength(methodName, "methodName may not be null or \"\"");

		Method calledMethod = getMethod(clusterObject, methodName, args);
		return userPrivilegeIntersect(user, clusterObject, calledMethod, args);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param clusterObject cluster object instance
	 * @param methodName method name to check
	 * @param args array of parameter
	 * @param argTypes array of parameter class objects
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedCluster clusterObject, String methodName, Object[] args, Class[] argTypes) {

		Assert.notNull(clusterObject, "clusterObject may not be null");
		Assert.hasLength(methodName, "methodName may not be null or \"\"");

		Method calledMethod = JDBClusterUtil.getMethod(clusterObject.getClass(), methodName, argTypes);
		return userPrivilegeIntersect(user, clusterObject, calledMethod, args);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param clusterObject cluster object instance
	 * @param method method name to check
	 * @param args array of parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedCluster clusterObject, Method method, Object... args) {

		Assert.notNull(clusterObject, "clusterObject may not be null");
		Assert.notNull(method, "method may not be null");

		return userPrivilegeIntersect(user, clusterObject, method, args);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, PrivilegedCluster clusterObject, Method calledMethod, Object... args) {

		Assert.notNull(clusterObject, "clusterObject may not be null");
		Assert.notNull(calledMethod, "calledMethod may not be null");

		HashSet<String> privileges = getStaticPrivilegesCluster(calledMethod, clusterObject);
		privileges.addAll(getDynamicPrivilegesCluster(clusterObject, calledMethod, args));
		return userPrivilegeIntersect(user, privileges);
	}

	/**
	 * intersects required privileges without a method call (new(..)) against
	 * given privileges
	 * 
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, PrivilegedCluster clusterObject) {

		Assert.notNull(clusterObject, "clusterObject may not be null");

		return userPrivilegeIntersect(user, calcStaticClusterPrivileges(clusterObject.getClass()));
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param serviceObject service object to check
	 * @param serviceMethodName method name to check
	 * @param args of method parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedService serviceObject, String serviceMethodName, Object... args) {

		Assert.notNull(serviceObject, "serviceObject may not be null");
		Assert.hasLength(serviceMethodName, "serviceMethodName may not be null or \"\"");

		Method calledMethod = getMethod(serviceObject, serviceMethodName, args);
		return userPrivilegeIntersect(user, serviceObject, calledMethod, args);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param serviceObject service object to check
	 * @param serviceMethodName method name to check
	 * @param args array of parameter
	 * @param argTypes array of parameter class objects
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedService serviceObject, String serviceMethodName, Object[] args, Class[] argTypes) {

		Assert.notNull(serviceObject, "serviceObject may not be null");
		Assert.hasLength(serviceMethodName, "serviceMethodName may not be null or \"\"");

		Method calledMethod = JDBClusterUtil.getMethod(serviceObject.getClass(), serviceMethodName, argTypes);
		return userPrivilegeIntersect(user, serviceObject, calledMethod, args);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param serviceObject service object to check
	 * @param serviceMethod method name to check
	 * @param args array of parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedService serviceObject, Method serviceMethod, Object... args) {

		Assert.notNull(serviceObject, "serviceObject may not be null");
		Assert.notNull(serviceMethod, "serviceMethodName may not be null");

		return userPrivilegeIntersect(user, serviceObject, serviceMethod, args);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param calledMethod the method called
	 * @param serviceObject service object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, PrivilegedService serviceObject, Method calledMethod, Object... args) {

		Assert.notNull(serviceObject, "serviceObject may not be null");
		Assert.notNull(calledMethod, "calledMethod may not be null");

		HashSet<String> privileges = getStaticPrivilegesService(calledMethod, serviceObject);
		privileges.addAll(getServiceMethodParameterPrivileges(calledMethod, args));
		return userPrivilegeIntersect(user, privileges);
	}

	/**
	 * extract method instance from name and parameter type
	 * 
	 * @param object object to check
	 * @param methodName the method name
	 * @param args the arguments as objects
	 * @return Method
	 */
	private Method getMethod(Object object, String methodName, Object... args) {
		Class[] paramTypes = new Class[args.length];
		for (int i = args.length - 1; i >= 0; i--) {
			paramTypes[i] = args[i].getClass();
		}
		return JDBClusterUtil.getMethodBestParameterFit(object, methodName, paramTypes);
	}

	/**
	 * calculates and stores static required privileges
	 * 
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return return reqired privileges
	 */
	private HashSet<String> getStaticPrivilegesCluster(Method calledMethod, PrivilegedCluster clusterObject) {
		HashMap<Method, HashSet<String>> mp = privilegesCluster.get(clusterObject.getClass());
		if (mp == null) {
			mp = new HashMap<Method, HashSet<String>>();
			privilegesCluster.put(clusterObject.getClass(), mp);
		}
		HashSet<String> hs = mp.get(calledMethod);
		if (hs == null) {
			hs = calcClusterPrivileges(calledMethod, clusterObject);
			mp.put(calledMethod, hs);
		}
		return new HashSet<String>(hs);
	}

	/**
	 * intersects a specific domain value with the needed rights for this value
	 * 
	 * @param domainId configured domain id
	 * @param value the value of the domain
	 * @return true if the user rights are sufficient
	 */
	public boolean userPrivilegeIntersectDomain(IUser user, String domainId, String value) {

		Assert.hasLength(domainId, "domainId may not be null or \"\"");

		DomainChecker dc = DomainCheckerImpl.getInstance();
		DomainPrivilegeList dpl;
		try {
			dpl = (DomainPrivilegeList) dc.getDomainListInstance(domainId);
		} catch (ClassCastException e) {
			throw new ConfigurationException("[" + domainId + "] must be a privileged domain and needs a implemented DomainPrivilegeList Interface", e);
		}

		Set<String> neededRights = dpl.getDomainEntryPivilegeList(domainId, value);
		return userPrivilegeIntersect(user, neededRights);
	}

	/**
	 * calculates instance and parameter specific privileges
	 * 
	 * @param clusterObject cluster object instance
	 * @param calledMethod the method called
	 * @param args method parameters
	 * @return return reqired privileges
	 */
	private Set<String> getDynamicPrivilegesCluster(PrivilegedCluster clusterObject, Method calledMethod, Object[] args) {
		DomainChecker dc = DomainCheckerImpl.getInstance();
		Set<String> result = new HashSet<String>();
		PrivilegesCluster pcAnno = clusterObject.getClass().getAnnotation(PrivilegesCluster.class);

		if (pcAnno == null || pcAnno.property().length == 0 || pcAnno.property()[0].length() == 0)
			return result;

		if (isGetMethodInRequiredProperty(pcAnno.property(), calledMethod))
			return result;

		beanWrapper.setWrappedInstance(clusterObject);

		for (String propertyPath : pcAnno.property()) {

			PropertyDescriptor pd = getPropertyDescriptor(propertyPath, beanWrapper);

			if (pd != null) {
				Field f = getPropertyField(propertyPath, pd);
				String domId = getDomainIdFromField(f);
				DomainPrivilegeList dpl;
				try {
					dpl = (DomainPrivilegeList) dc.getDomainListInstance(domId);
				} catch (ClassCastException e) {
					throw new ConfigurationException("privileged domain [" + domId + "] needs implemented DomainPrivilegeList Interface", e);
				}
				if (!(pd.getReadMethod().equals(calledMethod) || pd.getWriteMethod().equals(calledMethod))) {
					String value = getPropertyValue(propertyPath, beanWrapper);
					Set<String> setToAdd = dpl.getDomainEntryPivilegeList(domId, value);
					if (setToAdd != null)
						result.addAll(setToAdd);
				} else {
					if (pd.getWriteMethod().equals(calledMethod)) {
						if ((args.length != 1 || !(args[0] instanceof String)) && args[0] != null)
							throw new ConfigurationException("privilege checked property [" + propertyPath + "] needs 1 String argument setter");
						Set<String> ergList = dpl.getDomainEntryPivilegeList(domId, (String) args[0]);
						if (ergList != null)
							result.addAll(ergList);
					}
				}
			}
		}
		return result;
	}

	/**
	 * returns PropertyDescriptor from a given wrapped instance
	 * 
	 * @param propertyPath
	 * @return PropertyDescriptor
	 */
	private PropertyDescriptor getPropertyDescriptor(String propertyPath, BeanWrapperImpl beanWrapper) {
		PropertyDescriptor pd = null;
		try {
			pd = beanWrapper.getPropertyDescriptor(propertyPath);
		} catch (NullValueInNestedPathException nve) {
			if (logger.isInfoEnabled())
				logger.info("property [" + propertyPath + "] is not accessable. Skipping Priv test");
		} catch (Exception e1) {
			throw new ConfigurationException("property [" + propertyPath + "] is not accessable.", e1);
		}
		return pd;
	}

	/**
	 * calculates parameter specific privileges for service method calls
	 * null values for PrivilegeCluster arguments are always allowed!
	 * 
	 * @param calledServiceMethod the method called
	 * @param args method parameters
	 * @return Set<String>
	 */
	private Set<String> getServiceMethodParameterPrivileges(Method calledServiceMethod, Object[] args) {
		Set<String> result = new HashSet<String>();
		DomainChecker dc = DomainCheckerImpl.getInstance();
		Annotation[][] methodParameterAnnos = calledServiceMethod.getParameterAnnotations();
		if (methodParameterAnnos == null)
			return result;

		for (int i = 0; i < args.length; i++) {
			if (methodParameterAnnos.length > 0) {
				PrivilegesParameter pp = null;
				for (int t = 0; t < methodParameterAnnos[i].length; t++) {
					Annotation anno = methodParameterAnnos[i][t];
					if (anno instanceof PrivilegesParameter) {
						pp = (PrivilegesParameter) anno;
					}
				}
				if (pp != null) {
					Object arg = args[i];
					if (arg != null) {
						if (arg instanceof PrivilegedCluster) {
							serviceBeanWrapper.setWrappedInstance(arg);
							for (String propertyPath : pp.property()) {
								PropertyDescriptor pd = getPropertyDescriptor(propertyPath, serviceBeanWrapper);
								if (pd != null) {
									Field f = getPropertyField(propertyPath, pd);
									String domId = getDomainIdFromField(f);
									DomainPrivilegeList dpl;
									try {
										dpl = (DomainPrivilegeList) dc.getDomainListInstance(domId);
									} catch (ClassCastException e) {
										throw new ConfigurationException("privileged domain [" + domId + "] needs implemented DomainPrivilegeList Interface", e);
									}
									String value = getPropertyValue(propertyPath, serviceBeanWrapper);
									result.addAll(dpl.getDomainEntryPivilegeList(domId, value));
								}
							}
						} else
							throw new ConfigurationException("Argument of method: " + calledServiceMethod.getName() + " has @" + PrivilegesParameter.class.getName() + " annotation but is not a instance of "
									+ PrivilegedCluster.class.getName());
					}
				}
			}
		}
		return result;
	}

	/**
	 * gets Field instance of property path
	 * 
	 * @param propertyPath property path
	 * @param propDesc PropertyDescriptor
	 * @return Field instance
	 */
	private Field getPropertyField(String propertyPath, PropertyDescriptor propDesc) {
		Method propertyReadMethod = propDesc.getReadMethod();
		String mName = propertyReadMethod.getName();
		String pName = mName.substring(3, 4).toLowerCase() + mName.substring(4);
		Class clazzContainingProperty = propertyReadMethod.getDeclaringClass();
		Field f = JDBClusterUtil.getField(pName, clazzContainingProperty);
		if (f == null)
			throw new ConfigurationException("property [" + propertyPath + "] does not exist");
		return f;
	}

	/**
	 * returns the property value
	 * 
	 * @param propertyPath property path
	 * @return property value
	 */
	private String getPropertyValue(String propertyPath, BeanWrapperImpl beanWrapper) {
		String value = null;
		try {
			value = (String) beanWrapper.getPropertyValue(propertyPath);
		} catch (ClassCastException e) {
			throw new ConfigurationException("property [" + propertyPath + "] is not a string property", e);
		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.debug("property [" + propertyPath + "] is not accessable. Skipping Priv test");
		}
		return value;
	}

	/**
	 * checks wether get method call is part of required properties
	 * 
	 * @param property array of properties
	 * @param calledMethod called method
	 * @return boolean true if it is a getter and in required properties
	 */
	private boolean isGetMethodInRequiredProperty(String[] properties, Method calledMethod) {
		String mName = calledMethod.getName();
		if (mName.startsWith("set"))
			return false;

		String pName = mName.substring(3, 4).toLowerCase() + mName.substring(4);
		for (String property : properties) {
			if (property.indexOf(pName) > -1)
				return true;
		}
		return false;
	}

	/**
	 * calculates and stores static required privileges
	 * 
	 * @param calledServiceMethod the service method called
	 * @param serObject service object instance
	 * @return reqired privileges
	 */
	private HashSet<String> getStaticPrivilegesService(Method calledServiceMethod, PrivilegedService serObject) {
		HashMap<Method, HashSet<String>> mp = privilegesService.get(serObject.getClass());
		if (mp == null) {
			mp = new HashMap<Method, HashSet<String>>();
			privilegesService.put(serObject.getClass(), mp);
		}
		HashSet<String> hs = mp.get(calledServiceMethod);
		if (hs == null) {
			hs = calcServicePrivileges(calledServiceMethod, serObject);
			mp.put(calledServiceMethod, hs);
		}
		return new HashSet<String>(hs);
	}

	/**
	 * calculates static required privileges
	 * 
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return return reqired privileges
	 */
	private HashSet<String> calcClusterPrivileges(Method calledMethod, PrivilegedCluster clusterObject) {
		HashSet<String> hs = new HashSet<String>();
		PrivilegesMethod pmAnno = calledMethod.getAnnotation(PrivilegesMethod.class);

		hs.addAll(calcStaticClusterPrivileges(clusterObject.getClass()));

		if (pmAnno != null) {
			for (String s : pmAnno.required()) {
				hs.add(s);
			}
		}
		return hs;
	}

	/**
	 * calculates static required privileges without method
	 * 
	 * @param clusterObject
	 * @return
	 */
	private HashSet<String> calcStaticClusterPrivileges(Class<? extends PrivilegedCluster> clusterObject) {
		HashSet<String> hs = new HashSet<String>();
		PrivilegesCluster pcAnno = clusterObject.getAnnotation(PrivilegesCluster.class);

		if (pcAnno != null) {
			for (String s : pcAnno.required()) {
				hs.add(s);
			}
		}
		return hs;
	}

	/**
	 * calculates static required privileges
	 * 
	 * @param calledServiceMethod the service method called
	 * @param serObject service instance
	 * @return reqired privileges
	 */
	private HashSet<String> calcServicePrivileges(Method calledServiceMethod, PrivilegedService serObject) {
		HashSet<String> hs = new HashSet<String>();
		PrivilegesMethod pmAnno = calledServiceMethod.getAnnotation(PrivilegesMethod.class);
		PrivilegesService psAnno = serObject.getClass().getAnnotation(PrivilegesService.class);

		if (psAnno != null) {
			for (String s : psAnno.required()) {
				hs.add(s);
			}
		}
		if (pmAnno != null) {
			for (String s : pmAnno.required()) {
				hs.add(s);
			}
		}
		return hs;
	}

	/**
	 * gets domain id from annotations
	 * 
	 * @see org.jdbcluster.domain.DomainBase there is nearly the same method
	 * @param f the field instance
	 * @return the domain id
	 */
	private String getDomainIdFromField(Field f) {
		if (!f.isAnnotationPresent(PrivilegesDomain.class)) {
			throw new ConfigurationException("in @PrivilegesCluster referenced domain needs annotation @PrivilegesDomain. Not found on field: " + f.getName());
		}
		DomainDependancy dd = f.getAnnotation(DomainDependancy.class);
		if (dd != null)
			return dd.domainId();
		Domain d = f.getAnnotation(Domain.class);
		if (d == null)
			throw new ConfigurationException("no annotation @Domain or @DomainDependancy found on: " + f.getName());
		return d.domainId();
	}
}
