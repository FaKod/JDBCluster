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
