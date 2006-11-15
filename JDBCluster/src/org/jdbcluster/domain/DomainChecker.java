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

import org.jdbcluster.metapersistence.cluster.ClusterBase;

public interface DomainChecker {

	/**
	 * Check if there is a valid value stored in a property
	 * This can be used to check the internal state of the Cluster object
	 * @param cluster ClusterBase objects to test
	 * @param propSlavePath path to slave property (usually the name)
	 * @return true if value is valid
	 */
	public abstract boolean check(ClusterBase cluster, String propSlavePath);
	
	/**
	 * Check if propValue is a valid value
	 * This can be used to avoid a setter exception
	 * @param cluster ClusterBase objects to test
	 * @param propSlavePath path to slave property (usually the name)
	 * @param propValue the property value to check
	 * @return true if value is valid
	 */
	public abstract boolean check(ClusterBase cluster, String propSlavePath, Object propValue);

	/**
	 * calculates set of valid domain values (master or slave)
	 * @param cluster Cluster object to use
	 * @param propPath path to the master or slave property
	 * @return ValidDomainEntries<String> with the valid domain values
	 */
	public abstract ValidDomainEntries<String> getValidDomainEntries(ClusterBase cluster, String propPath);
	
	/**
	 * returns the corresponding domain id
	 * @param cluster Cluster object to use
	 * @param propPath path to the master or slave property
	 * @return String domain id
	 */
	public abstract String getDomainId(ClusterBase cluster, String propPath);

	/**
	 * returns all values for domain slaveDomainId
	 * @param domainId requested domain id
	 * @return ordered list of all domain entries
	 */
	public abstract ValidDomainEntries<String> getPossibleDomainEntries(String domainId);
	
	/**
	 * creates singelton instances of DomainList implementation as configured
	 * @param domainId configured domain id
	 * @return singleton instance of DomainList
	 */
	public DomainList getDomainListInstance(String domainId);

}