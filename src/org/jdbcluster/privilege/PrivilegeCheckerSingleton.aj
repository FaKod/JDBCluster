package org.jdbcluster.privilege;

import org.jdbcluster.metapersistence.aspects.SingletonPattern;

/**
 * define PrivilegeChecker as a singleton instance
 * @author FaKod
 */
public aspect PrivilegeCheckerSingleton extends SingletonPattern {
	declare parents: PrivilegeCheckerImpl implements Singleton;
}
