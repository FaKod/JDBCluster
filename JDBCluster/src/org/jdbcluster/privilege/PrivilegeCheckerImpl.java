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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.domain.DomainCheckerImpl;
import org.jdbcluster.domain.DomainPrivilegeList;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.PrivilegesCluster;
import org.jdbcluster.metapersistence.annotation.PrivilegesDomain;
import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;
import org.jdbcluster.metapersistence.annotation.PrivilegesService;
import org.jdbcluster.service.PrivilegedService;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

/**
 * 
 * @author FaKod
 * 
 */
public class PrivilegeCheckerImpl extends PrivilegeBase implements PrivilegeChecker {

	/** Logger available to subclasses */
	protected final Logger logger = Logger.getLogger(getClass());

	/**
	 * always reused instance of springs internal bean wrapper class
	 */
	static private BeanWrapperImpl beanWrapper = new BeanWrapperImpl(true);

	/**
	 * maps: Class -> Method -> HashSet of privileges
	 */
	private static HashMap<Class<?>, HashMap<Method, HashSet<String>>> privilegesClustere = new HashMap<Class<?>, HashMap<Method, HashSet<String>>>();

	/**
	 * maps: Class -> Servicce Method -> HashSet of privileges
	 */
	private static HashMap<Class<?>, HashMap<Method, HashSet<String>>> privilegesService = new HashMap<Class<?>, HashMap<Method, HashSet<String>>>();

	/**
	 * should do an intersection between the user privileges and the privileges
	 * given through requiredPrivileges
	 * 
	 * @param requiredPrivileges
	 *            the required privileges
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(Set<String> requiredPrivileges) {
		return getUserPrivilege().userPrivilegeIntersect(requiredPrivileges);
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param calledMethod
	 *            the method called
	 * @param clusterObject
	 *            cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(Method calledMethod, PrivilegedCluster clusterObject) {
		DomainCheckerImpl dc = new DomainCheckerImpl();
		HashSet<String> priv = new HashSet<String>(getPrivilegesCluster(calledMethod, clusterObject));
		PrivilegesCluster pcAnno = clusterObject.getClass().getAnnotation(PrivilegesCluster.class);
		
		if(!isRequiredPropertyInGetMethod(pcAnno.property(), calledMethod)) {
			beanWrapper.setWrappedInstance(clusterObject);
			if (pcAnno.property().length > 0 && pcAnno.property()[0].length() > 0) {
				for (String property : pcAnno.property()) {
					PropertyDescriptor pd = null;
					try {
						pd = beanWrapper.getPropertyDescriptor(property);
					} catch (Exception e1) {
						if (logger.isInfoEnabled())
							logger.info("property [" + property + "] is not accessable. Skipping Priv test");
					}
					
					if (pd != null) {
						if (!pd.getReadMethod().equals(calledMethod) && !pd.getWriteMethod().equals(calledMethod)) {
	
							// get property field
							Method propertyReadMethod = pd.getReadMethod();
							String mName = propertyReadMethod.getName();
							String pName = mName.substring(3,4).toLowerCase() + mName.substring(4);
							Class clazzContainingProperty = propertyReadMethod.getDeclaringClass();
							Field f = JDBClusterUtil.getField(pName, clazzContainingProperty);
							if (f == null)
								throw new ConfigurationException("property [" + property + "] does not exist");
	
							String domId = getDomainIdFromField(f);
	
							String value = null;
							try {
								value = (String) beanWrapper.getPropertyValue(property);
							} catch (ClassCastException e) {
								throw new ConfigurationException("property [" + property + "] is not a string property", e);
							} catch (Exception e) {
								if (logger.isDebugEnabled())
									logger.debug("property [" + property + "] is not accessable. Skipping Priv test");
							}
	
							DomainPrivilegeList dpl;
							try {
								dpl = (DomainPrivilegeList) dc.getDomainListInstance(domId);
							} catch (ClassCastException e) {
								throw new ConfigurationException("privileged domain [" + domId + "] needs implemented DomainPrivilegeList Interface", e);
							}
	
							priv.addAll(dpl.getDomainEntryPivilegeList(domId, value));
						}
					}
				}
			}
		}
		return userPrivilegeIntersect(priv);
	}

	/**
	 * checks wether get method call is part of privileged properties
	 * @param property array of properties
	 * @param calledMethod called method
	 */
	private boolean isRequiredPropertyInGetMethod(String[] properties, Method calledMethod) {
		String mName = calledMethod.getName();
		if(mName.startsWith("set"))
			return false;
		
		String pName = mName.substring(3,4).toLowerCase() + mName.substring(4);
		for(String property : properties) {
			if(property.indexOf(pName)>-1)
				return true;
		}
		return false;
	}

	/**
	 * intersects required privileges against given privileges
	 * 
	 * @param calledServiceMethod
	 *            the service method called
	 * @param serObject
	 *            service instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(Method calledServiceMethod, PrivilegedService serObject) {
		HashSet<String> priv = new HashSet<String>(getPrivilegesService(calledServiceMethod, serObject));
		return userPrivilegeIntersect(priv);
	}

	/**
	 * calculates and stores static required privileges
	 * 
	 * @param calledMethod
	 *            the method called
	 * @param clusterObject
	 *            cluster object instance
	 * @return return reqired privileges
	 */
	private HashSet<String> getPrivilegesCluster(Method calledMethod, PrivilegedCluster clusterObject) {
		HashMap<Method, HashSet<String>> mp = privilegesClustere.get(clusterObject.getClass());
		if (mp == null) {
			mp = new HashMap<Method, HashSet<String>>();
			privilegesClustere.put(clusterObject.getClass(), mp);
		}
		HashSet<String> hs = mp.get(calledMethod);
		if (hs == null) {
			hs = calcClusterPrivileges(calledMethod, clusterObject);
			mp.put(calledMethod, hs);
		}
		return hs;
	}

	/**
	 * calculates and stores static required privileges
	 * 
	 * @param calledServiceMethod
	 *            the service method called
	 * @param serObject
	 *            service object instance
	 * @return reqired privileges
	 */
	private HashSet<String> getPrivilegesService(Method calledServiceMethod, PrivilegedService serObject) {
		HashMap<Method, HashSet<String>> mp = privilegesService.get(serObject.getClass());
		if (mp == null) {
			mp = new HashMap<Method, HashSet<String>>();
			privilegesClustere.put(serObject.getClass(), mp);
		}
		HashSet<String> hs = mp.get(calledServiceMethod);
		if (hs == null) {
			hs = calcServicePrivileges(calledServiceMethod, serObject);
			mp.put(calledServiceMethod, hs);
		}
		return hs;
	}

	/**
	 * calculates static required privileges
	 * 
	 * @param calledMethod
	 *            the method called
	 * @param clusterObject
	 *            cluster object instance
	 * @return return reqired privileges
	 */
	private HashSet<String> calcClusterPrivileges(Method calledMethod, PrivilegedCluster clusterObject) {
		HashSet<String> hs = new HashSet<String>();
		PrivilegesMethod pmAnno = calledMethod.getAnnotation(PrivilegesMethod.class);
		PrivilegesCluster pcAnno = clusterObject.getClass().getAnnotation(PrivilegesCluster.class);

		if (pcAnno != null) {
			for (String s : pcAnno.required()) {
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
	 * calculates static required privileges
	 * 
	 * @param calledServiceMethod
	 *            the service method called
	 * @param serObject
	 *            service instance
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
	 * @param f
	 *            the field instance
	 * @return the domain id
	 */
	private String getDomainIdFromField(Field f) {
		if(!f.isAnnotationPresent(PrivilegesDomain.class)) {
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
