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

import java.util.Set;

/**
 * hat to return required privileges for the given domain id
 * and value
 * @author FaKod
 *
 */
public interface DomainPrivilegeList {

	/**
	 * <B>This method is called often, so it has to be fast</B>
	 * @param domainId as configured in domain validation 
	 * @param value one value of domain 
	 * @return set of required privileges
	 */
	Set<String> getDomainEntryPivilegeList(String domainId, String value);
}
