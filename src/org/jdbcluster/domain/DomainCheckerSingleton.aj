package org.jdbcluster.domain;

import org.jdbcluster.metapersistence.aspects.SingletonPattern;

/**
 * define DomainCheckerImpl as a singleton instance
 * @author FaKod
 */
public aspect DomainCheckerSingleton extends SingletonPattern {
	declare parents: DomainCheckerImpl implements Singleton;
}
