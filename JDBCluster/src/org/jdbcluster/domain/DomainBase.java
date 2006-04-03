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
package org.jdbcluster.domain;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;

/**
 * 
 * @author Christopher Schmidt
 * @author FaKod
 * Base class for domain checkng stuff
 */
public abstract class DomainBase {

	/**
	 * static instance of the configuration file
	 */
	private static DomainConfig domainConfig;
	
	/**
	 * Map for instances of domain list
	 */
	private Map<String, DomainList> domainListMap = new HashMap<String, DomainList>();


	public static DomainConfig getDomainConfig() {
		return domainConfig;
	}

	public static void setDomainConfig(DomainConfig domainConfig) {
		DomainBase.domainConfig = domainConfig;
	}
	
	/**
	 * creates singelton instances of DomainList implementation as configured
	 * @param domainId configured domain id
	 * @return singleton instance of DomainList
	 */
	public DomainList getDomainListInstance(String domainId) {
		DomainList dl = domainListMap.get(domainId);
		if(dl==null) {
			String domainClassString = domainConfig.getDomainListClass(domainId);
			if(domainClassString==null || domainClassString.length()==0)
				throw new ConfigurationException("DomainList instance for " + domainId + " is undefined");
			dl = (DomainList) JDBClusterUtil.createClassObject(domainClassString);
			domainListMap.put(domainId, dl);
		}
		return dl;
	}
	
	/**
	 * Check algorithmus 
	 * 1. valid or invalid tags with value attribute 
	 * 2. valid tags with all attribute 
	 * 3. invalid tags with all attribute
	 * 
	 * @param valid configured ValidEntryList
	 * @param slaveValue slave value to check
	 * @return true if its allowed
	 */
	static boolean validate(ValidEntryList valid, String slaveValue) {
		Valid validDomEntry = valid.getValidFromDomainEntry(slaveValue);
		if (validDomEntry != null) {
			return validDomEntry.valid;
		}

		for (Valid v : valid) {
			if(v.all) {
				if (v.valid) {
					if (v.nullValue)
						return true;
					else
						return slaveValue != null;
				}
				else {
					if (v.nullValue)
						return false;
					else
						return slaveValue == null;
				}
			}
			else {
				if (v.valid) { // is it a valid tag or an invalid tag
					if (v.nullValue) // check if null value is allowed
						return slaveValue == null;
					else
						return slaveValue != null;
				} else { // its a invalid tag
					if (v.nullValue) { // check if null value is allowed
						return slaveValue != null;
					}
					else
						return slaveValue == null;
				}
			}
		}
		
		if(valid.isContainsValidElements())
			return false;
		return true;
	}
	
	/**
	 * gets domain id from annotations
	 * @param f the field instance
	 * @param mustHaveDependencies true if a @DomainDependancy has to be present
	 * @return the domain id
	 */
	static String getDomainIdFromField(Field f, boolean mustHaveDependencies) {
		DomainDependancy dd = f.getAnnotation(DomainDependancy.class);
		if(dd==null && mustHaveDependencies)
			throw new ConfigurationException("no annotation @DomainDependancy found on: " + f.getName());
		if(dd!=null)
			return dd.domainId();
		
		Domain d = f.getAnnotation(Domain.class);
		if(d==null)
			throw new ConfigurationException("no annotation @Domain found on: " + f.getName());
		return d.domainId();
	}
}
