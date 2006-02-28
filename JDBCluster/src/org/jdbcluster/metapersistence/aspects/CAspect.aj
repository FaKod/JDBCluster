package org.jdbcluster.metapersistence.aspects;

import org.jdbcluster.dao.Dao;
import org.jdbcluster.metapersistence.cluster.*;

/**
 * @author Christopher Schmidt
 * Aspect for inter type declaration of the
 * Dao stuff
 */
public abstract aspect CAspect {
	public Dao ClusterBase.dao;
	public Class ClusterBase.daoClass;
}
