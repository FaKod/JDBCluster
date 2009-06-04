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
package org.jdbcluster.template.hibernate;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;
import org.jdbcluster.metapersistence.cluster.ICluster;
import org.jdbcluster.metapersistence.security.user.IUser;
import org.jdbcluster.template.QueryTemplate;
import org.jdbcluster.template.SessionFactoryTemplate;
import org.jdbcluster.template.SessionFilter;
import org.jdbcluster.template.SessionTemplate;
import org.jdbcluster.template.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * implements SessionTemplate to use HIBERNATE
 * 
 * @author FaKod
 * @author Philipp Noggler
 * @author thobi
 */
public class HibernateSession implements SessionTemplate {

	private Logger logger = Logger.getLogger(this.getClass());

	protected Session hibernateSession;
	
	protected IUser user;

	protected HibernateSessionFactory factory;
	
	protected HibernateTransaction tx;

	public HibernateSession(IUser user, HibernateSessionFactory factory) {
		this.factory = factory;
		this.user = user;
	}
	
	public void finalize() {
		if(factory!=null)
			factory.removeSessionFromSessionList(this);
	}

	public TransactionTemplate beginTransaction() {
		tx = new HibernateTransaction();
		tx.setTransaction(hibernateSession.beginTransaction());
		return tx;
	}
	
	public TransactionTemplate getTransactionTemplate() {
		return tx;
	}

	public void save(ICluster cluster) {
		Assert.notNull(cluster, "Cluster may not be null");
		cluster.setUser(user);
		hibernateSession.save(cluster.getDao());
	}

	public void close() {
		hibernateSession.close();
	}

	public Session getHibernateSession() {
		return hibernateSession;
	}

	public void setHibernateSession(Session sess) {
		this.hibernateSession = sess;
	}

	public void setSessionFactory(SessionFactoryTemplate factory) {
		this.factory = (HibernateSessionFactory) factory;
	}

	public SessionFactoryTemplate getSessionFactory() {
		return factory;
	}

	public void cancelQuery() {
		hibernateSession.cancelQuery();
	}

	public QueryTemplate createQuery(String queryString) {
		if (logger.isDebugEnabled())
			logger.debug("creating Query with queryString = " + queryString);
		Query query = hibernateSession.createQuery(queryString);
		return new HibernateQuery(user, query);
	}

	public QueryTemplate getNamedQuery(String queryName) {
		if (logger.isDebugEnabled())
			logger.debug("creating NamedQuery with queryName = " + queryName);
		Query query = hibernateSession.getNamedQuery(queryName);
		return new HibernateQuery(user, query);
	}

	public void delete(ICluster cluster) {
		Assert.notNull(cluster, "Cluster may not be null");
		cluster.setUser(user);
		hibernateSession.delete(cluster.getDao());
	}

	public void saveOrUpdate(ICluster cluster) {
		Assert.notNull(cluster, "Cluster may not be null");
		cluster.setUser(user);
		hibernateSession.saveOrUpdate(cluster.getDao());
	}

	public void update(ICluster cluster) {
		Assert.notNull(cluster, "Cluster may not be null");
		cluster.setUser(user);
		hibernateSession.update(cluster.getDao());
	}

	public void save(String id, ICluster cluster) {
		Assert.notNull(cluster, "Cluster may not be null");
		cluster.setUser(user);
		hibernateSession.save(id, cluster.getDao());
	}

	/**
	 * creates a query with given parameters and saves the query object (to get
	 * the result later on)
	 * 
	 * @param ccf the filter that contains the criteria for the select statement
	 * @return QueryTemplate the object holding the hibernate query
	 */
	public QueryTemplate createQuery(CCFilter ccf) {

		Assert.notNull(ccf, "CCFilter may not be null");

		if (logger.isDebugEnabled())
			logger.debug("creating Query with CCFilter = " + ccf.getClass().getName() + " and ClusterType = " + ccf.getClusterType().getName());

		Query query;
		HibernateQuery queryTemplate = new HibernateQuery(user);
		queryTemplate.setClusterType(ccf.getClusterType());

		/**
		 * where statement
		 */
		String whereStatement = ccf.getWhereStatement();
		String staticStatement = ccf.getStaticStatement();
		if (staticStatement != null && staticStatement.length() > 0) {
			if (whereStatement != null && whereStatement.length() > 0)
				whereStatement = " ( " + whereStatement + " ) AND ";
			whereStatement = whereStatement + " ( " + staticStatement + " ) ";
		}

		if (logger.isDebugEnabled())
			logger.debug("using where statement [" + whereStatement + "]");

		String alias = ccf.getAlias();

		/**
		 * part after from before where
		 */
		StringBuilder qStr = new StringBuilder(alias);
		
		String fetch = ccf.getFetch();
		if (fetch != null && fetch.length() > 0) {
			qStr.append(fetch);
		}
				
		String ext = ccf.getExt();
		if (ext != null && ext.length() > 0)
			qStr.append(", ").append(ext);

		if (logger.isDebugEnabled())
			logger.debug("using from ... statement [" + qStr + "]");

		String select = "";
		if (alias != null && alias.length() > 0) // sollte hier nich besser auf ext abgeprüft werden ("from Car c" geht, aber "from Car, Bike" ist anders als erwartet)
			select = "select " + alias + " ";

		/**
		 * order by clause
		 */
		String orderBy = ccf.getOrderBy();
		if (orderBy != null && orderBy.length() > 0)
			orderBy = " order by " + orderBy;

		if (whereStatement != null && whereStatement.length() > 0) {
			String queryString = select + " from " + ccf.getSelectStatementDAO() + " " + qStr + " " + " where " + whereStatement + orderBy;

			if (logger.isDebugEnabled())
				logger.debug("using query string [" + queryString + "]");

			query = hibernateSession.createQuery(queryString);
			queryTemplate.setQuery(query);
			ccf.doBindings(queryTemplate);

		} else {
			String queryString = " from " + ccf.getSelectStatementDAO() + " " + qStr + " " + orderBy;

			if (logger.isDebugEnabled())
				logger.debug("using query string [" + queryString + "]");

			query = hibernateSession.createQuery(queryString);
			queryTemplate.setQuery(query);
		}
		return queryTemplate;
	}

	/**
	 * returns native session object
	 */
	@SuppressWarnings("unchecked")
	public <T> T getNativeSession() {
		return (T) hibernateSession;
	}

	/**
	 * Re-read the state of the given instance from the underlying database. It
	 * is inadvisable to use this to implement long-running sessions that span
	 * many business tasks. This method is, however, useful in certain special
	 * circumstances. For example
	 * <ul>
	 * <li>where a database trigger alters the object state upon insert or
	 * update
	 * <li>after executing direct SQL (eg. a mass update) in the same session
	 * <li>after inserting a <tt>Blob</tt> or <tt>Clob</tt>
	 * </ul>
	 * 
	 * @param object a persistent or detached cluster instance
	 */
	public void refresh(ICluster cluster) {
		
		Assert.notNull(cluster, "Cluster may not be null");
		cluster.setUser(user);
		
		hibernateSession.refresh(cluster.getDao());
		
		ClusterFactory.getClusterInterceptor().clusterRefresh(cluster);
	}

	/**
	 * creates a new Cluster Object and read the persistent state associated
	 * with the given identifier into the given instance. Assuming that the
	 * instance exists. non-existence would be an actual error.
	 * 
	 * @param clusterClass class of cluster
	 * @param id primary id of cluster
	 */
	public ICluster load(Class<? extends ICluster> clusterClass, Serializable id) {

		Assert.notNull(clusterClass, "clusterClass may not be null");
		Assert.notNull(id, "id may not be null");

		ICluster cb = ClusterFactory.newInstance(clusterClass, null, true, user);
		hibernateSession.load(cb.getDao(), id);
		return cb;
	}

	/**
	 * Read the persistent state associated with the given identifier into the
	 * given instance. Assuming that the instance exists. non-existence would be
	 * an actual error.
	 * 
	 * @param cluster existing cluster object
	 * @param id primary id of cluster
	 */
	public ICluster load(ICluster cluster, Serializable id) {

		Assert.notNull(cluster, "cluster may not be null");
		Assert.notNull(id, "id may not be null");

		hibernateSession.load(cluster.getDao(), id);
		cluster.setUser(user);
		
		ClusterFactory.getClusterInterceptor().clusterRefresh(cluster);
		
		return cluster;
	}
	
	/**
	 * Read the persistent state associated with the given identifier into the
	 * given instance. Assuming that the instance exists. non-existence would be
	 * an actual error.
	 * 
	 * @param clusterType existing cluster object
	 * @param id primary id of cluster
	 */
	public ICluster load(ClusterType clusterType, Serializable id) {

		Assert.notNull(clusterType, "clusterType may not be null");
		
		return load(clusterType.getClusterClass(), id);
	}

	/**
	 * Read the persistent state associated with the given identifier into the
	 * given instance. Assuming that the instance exists. non-existence would be
	 * an actual error.
	 * 
	 * @param clusterTypeName existing cluster object
	 * @param id primary id of cluster
	 */
	public ICluster load(String clusterTypeName, Serializable id) {

		Assert.notNull(clusterTypeName, "clusterType may not be null");
			
		ClusterType ct = ClusterTypeFactory.newInstance(clusterTypeName);
		return load(ct.getClusterClass(), id);
	}

	/**
	 * creates a new Cluster Object and return the persistent instance of the
	 * given entity class with the given identifier, or null if there is no such
	 * persistent instance. (If the instance, or a proxy for the instance, is
	 * already associated with the session, return that instance or proxy.)
	 * 
	 * @param clusterClass class of cluster
	 * @param id primary id of cluster
	 */
	public ICluster get(Class<? extends ICluster> clusterClass, Serializable id) {

		Assert.notNull(clusterClass, "clusterClass may not be null");
		Assert.notNull(id, "id may not be null");

		Cluster cb = ClusterFactory.newInstance(clusterClass, null, true, user);
		Object dao = hibernateSession.get(cb.getDao().getClass(), id);

		if (dao != null) {
			cb.setDao(dao);
			return cb;
		}
		return null;
	}
	
	/**
	 * creates a new Cluster Object and return the persistent instance of the
	 * given entity class with the given identifier, or null if there is no such
	 * persistent instance. (If the instance, or a proxy for the instance, is
	 * already associated with the session, return that instance or proxy.)
	 * 
	 * @param clusterType class of cluster
	 * @param id primary id of cluster
	 */
	public ICluster get(ClusterType clusterType, Serializable id) {
		
		Assert.notNull(clusterType, "clusterType may not be null");
		
		return get(clusterType.getClusterClass(), id);
	}

	/**
	 * creates a new Cluster Object and return the persistent instance of the
	 * given entity class with the given identifier, or null if there is no such
	 * persistent instance. (If the instance, or a proxy for the instance, is
	 * already associated with the session, return that instance or proxy.)
	 * 
	 * @param clusterTypeName class of cluster
	 * @param id primary id of cluster
	 */
	public ICluster get(String clusterTypeName, Serializable id) {
		
		Assert.notNull(clusterTypeName, "clusterTypeName may not be null");
		
		ClusterType ct = ClusterTypeFactory.newInstance(clusterTypeName);
		return get(ct.getClusterClass(), id);
	}


	/**
	 * Return the persistent instance of the given entity class with the given
	 * identifier, or null if there is no such persistent instance. (If the
	 * instance, or a proxy for the instance, is already associated with the
	 * session, return that instance or proxy.)
	 * 
	 * @param cluster existing cluster object
	 * @param id primary id of cluster
	 */
	public ICluster get(ICluster cluster, Serializable id) {

		Assert.notNull(cluster, "cluster may not be null");
		Assert.notNull(id, "id may not be null");

		((Cluster)cluster).setDao(hibernateSession.get(cluster.getDao().getClass(), id));
		cluster.setUser(user);
		
		ClusterFactory.getClusterInterceptor().clusterRefresh(cluster);
		
		return cluster;
	}

	/**
	 * Copy the state of the given object onto the persistent object with the
	 * same identifier. If there is no persistent instance currently associated
	 * with the session, it will be loaded. This operation cascades to
	 * associated instances if the association is mapped with
	 * <tt>cascade="merge"</tt>.<br>
	 * <br>
	 * The semantics of this method are defined by JSR-220.
	 * 
	 * @param cluster cluster object
	 */
	public void merge(ICluster cluster) {

		Assert.notNull(cluster, "cluster may not be null");

		Object dao = cluster.getDao();
		((Cluster)cluster).setDao(hibernateSession.merge(dao));
		cluster.setUser(user);
		
		ClusterFactory.getClusterInterceptor().clusterRefresh(cluster);
	}

	/**
	 * Remove this instance from the session cache. Changes to the instance will
	 * not be synchronized with the database. This operation cascades to associated
	 * instances if the association is mapped with <tt>cascade="evict"</tt>.
	 *
	 * @param object a persistent instance
	 * @throws HibernateException
	 */
	public void evict(ICluster cluster) {
		
		Assert.notNull(cluster, "cluster may not be null");
		cluster.setUser(user);
		Object dao = cluster.getDao();
		hibernateSession.evict(dao);
	}

	/**
	 * Make a transient instance persistent. This operation cascades to associated 
	 * instances if the association is mapped with <tt>cascade="persist"</tt>.<br>
	 * <br>
	 * The semantics of this method are defined by JSR-220.
	 * 
	 * @param object a transient instance to be made persistent
	 */
	public void persist(ICluster cluster) {

		Assert.notNull(cluster, "cluster may not be null");
		cluster.setUser(user);
		Object dao = cluster.getDao();
		hibernateSession.persist(dao);
		
		ClusterFactory.getClusterInterceptor().clusterRefresh(cluster);
	}

	/**
	 * Does this <tt>Session</tt> contain any changes which must be
	 * synchronized with the database? Would any SQL be executed if
	 * we flushed this session?
	 *
	 * @return boolean
	 */
	public boolean isDirty() {
		return hibernateSession.isDirty();
	}

	
	/**
	 * activates the given Filter on the Session. The Filter contains the name, parameter name 
	 * @param sessionFilter the filter with the given name will be activated on the session.
	 */
	public void enableFilter(SessionFilter sessionFilter) {
		Filter filter = hibernateSession.enableFilter(sessionFilter.getFilterName());
		if (filter == null) {
			throw new RuntimeException("Filter with name '" + sessionFilter.getFilterName() + "' is null - check your Hibernate filter configuration");
		}
		for (Object value : sessionFilter.getValues()) {
			if (sessionFilter.getParameterName() != null && !sessionFilter.getParameterName().equals("") && value != null) {
				filter.setParameter(sessionFilter.getParameterName(), value);
			}
		}
	}

	public IUser getUser() {
		return user;
	}

	public boolean contains(ICluster cluster) {
		return hibernateSession.contains(cluster.getDao());
	}

}
