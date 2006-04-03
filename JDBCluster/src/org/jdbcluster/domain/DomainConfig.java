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

/**
 * 
 * @author Christopher Schmidt
 * basic interface 
 */
public interface DomainConfig {
	
	/**
	 * returns instance of EntrySet for a specific domain
	 * @param domainId requested domain id
	 * @return EntrySet singleton instanstance
	 */
	public EntrySet getEntrySet(String domainId);
	
	/**
	 * returns configured DomainList instance for a specific domain id
	 * @param domainId requested domain id
	 * @return String class name
	 */
	public String getDomainListClass(String domainId);

}
