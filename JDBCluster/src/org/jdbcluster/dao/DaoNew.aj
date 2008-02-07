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

import org.jdbcluster.exception.ConfigurationException;
import org.springframework.beans.BeanWrapperImpl;
import java.util.HashMap;

/**
 * Asdvices creation of Dao objects for default values
 * @author Christopher Schmidt
 */
public aspect DaoNew {
	
	/**
	 * always reused instance of springs internal bean wrapper class
	 */
	static private BeanWrapperImpl beanWrapper = new BeanWrapperImpl(true);
	
	static private HashMap<Class<?>, HashMap<String, String>> classToPropsMap = 
		new HashMap<Class<?>, HashMap<String, String>>();

	pointcut newDao(Dao dao): 
		initialization ( Dao.new(..) ) && 
		target(dao);
	
	/**
	 * advice looks for configured default values
	 * and uses spring bean wrapper to set the string values
	 */

	after (Dao dao) returning : newDao(dao) {
		//moved to Dao used as DaoFactory now
		/*Class<?> daoClass = dao.getClass();
		HashMap<String, String> hm = classToPropsMap.get(daoClass);
		if(hm==null)
			hm=putCacheMap(daoClass);
		
		beanWrapper.setWrappedInstance(dao);

		for(String prop : hm.keySet()) {
			String value = hm.get(prop);
			Class propClass = beanWrapper.getPropertyType(prop);
            Object o = beanWrapper.convertIfNecessary(value, propClass);
            beanWrapper.setPropertyValue(new PropertyValue(prop, o));
		}*/
	}
	
	private HashMap<String, String> putCacheMap(Class<?> daoClass) {
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
