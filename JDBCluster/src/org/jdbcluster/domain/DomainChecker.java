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
	 * calculates set of valid domain values (master or slave)
	 * @param cluster Cluster object to use
	 * @param propPath path to the master or slave property
	 * @return ValidDomainEntries<String> with the valid domain values
	 */
	public abstract ValidDomainEntries<String> getValidDomainEntries(ClusterBase cluster, String propPath);

	/**
	 * returns all values for domain slaveDomainId
	 * @param domainId requested domain id
	 * @return ordered list of all domain entries
	 */
	public abstract ValidDomainEntries<String> getPossibleDomainEntries(String domainId);

}