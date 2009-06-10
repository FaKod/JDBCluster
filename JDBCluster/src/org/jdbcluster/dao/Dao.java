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
package org.jdbcluster.dao;

import java.util.HashMap;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.exception.ConfigurationException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

/**
 * Basic Dao class
 * @author FaKod
 */
public abstract class Dao {

	private static DaoConfig daoConfig;
	
	static private HashMap<Class<?>, HashMap<String, String>> classToPropsMap = 
		new HashMap<Class<?>, HashMap<String, String>>();

	/**
	 * gets configuration class for Daos
	 * @return config object
	 */
	public static DaoConfig getDaoConfig() {
		return daoConfig;
	}

	/**
	 * sets configuration class for Daos
	 * @param daoConfig config object
	 */
	public static void setDaoConfig(DaoConfig daoConfig) {
		Dao.daoConfig = daoConfig;
	}
	
	/**
	 * creates new instance of a Dao using given Dao class
	 * @param daoClass class of Object to create
	 * @return created dao instance
	 */
	public static Object newInstance(Class<?> daoClass) {
		Object dao = JDBClusterUtil.createClassObject(daoClass);
		//get property of DAO and initial value from jdbcluster.dao.conf.xml
		HashMap<String, String> hm;
		synchronized (classToPropsMap) {
			hm = classToPropsMap.get(daoClass);
			if(hm==null)
				hm=putCacheMap(daoClass);			
		}
		
		BeanWrapper beanWrapper = new BeanWrapperImpl();
		beanWrapper.setWrappedInstance(dao);

		for(String prop : hm.keySet()) {
			String value = hm.get(prop);
			//get property of DAO
			Class<?> propClass = beanWrapper.getPropertyType(prop);
			//convert to Type if necessary
            Object o = beanWrapper.convertIfNecessary(value, propClass);
            //set the value of the predefined property read from jdbcluster.dao.conf.xml
            beanWrapper.setPropertyValue(new PropertyValue(prop, o));
		}
		return dao;
	}
	
	/**
	 * caches dao configurations
	 * @param daoClass class of dao object
	 * @return map element
	 */
	private static HashMap<String, String> putCacheMap(Class<?> daoClass) {
		DaoConfig daoConfig = Dao.getDaoConfig();
		if(daoConfig==null)
			throw new ConfigurationException("No Dao configuration present. Call Dao.setDaoConfig first");
		String daoId = daoConfig.getDaoId(daoClass.getName());
		HashMap<String, String> hm = new HashMap<String, String>();
		classToPropsMap.put(daoClass, hm);
		
		String[] props = daoConfig.getPropertyName(daoId);
		for(String prop : props) {
			String value = daoConfig.getPropertieValue(daoId, prop);
			hm.put(prop, value);
		}
		return hm;
	}
	
}
