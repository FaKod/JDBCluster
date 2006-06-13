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

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.template.QueryTemplate;
import org.jdbcluster.template.SessionFactoryTemplate;
import org.jdbcluster.template.SessionTemplate;
import org.jdbcluster.template.TransactionTemplate;

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

	protected HibernateSessionFactory factory;

	public HibernateSession() {
	}

	public TransactionTemplate beginTransaction() {
		HibernateTransaction tx = new HibernateTransaction();
		tx.setTransaction(hibernateSession.beginTransaction());
		return tx;
	}

	public void save(Object o) {
		hibernateSession.save(o);
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
		if(logger.isDebugEnabled())
			logger.debug("creating Query with queryString = " + queryString);
		Query query = hibernateSession.createQuery(queryString);
		return new HibernateQuery(query);
	}

	public QueryTemplate getNamedQuery(String queryName) {
		if(logger.isDebugEnabled())
			logger.debug("creating NamedQuery with queryName = " + queryName);
		Query query = hibernateSession.getNamedQuery(queryName);
		return new HibernateQuery(query);
	}

	public void delete(Object o) {
		hibernateSession.delete(o);
	}

	public void saveOrUpdate(Object o) {
		hibernateSession.saveOrUpdate(o);
	}

	public void update(Object o) {
		hibernateSession.update(o);
	}

	public void save(String id, Object o) {
		hibernateSession.save(id, o);
	}

	/**
	 * creates a query with given parameters and saves the query object (to get
	 * the result later on)
	 * 
	 * @param ccf
	 *            the filter that contains the criteria for the select statement
	 * @return QueryTemplate the object holding the hibernate query
	 */
	public QueryTemplate createQuery(CCFilter ccf) {
		if(logger.isDebugEnabled())
			logger.debug("creating Query with CCFilter = " + ccf.getClass().getName() + 
					" and ClusterType = " + ccf.getClusterType().getName());
		
		Query query;
		HibernateQuery queryTemplate = new HibernateQuery();
		queryTemplate.setClusterType(ccf.getClusterType());

		/**
		 * where statement
		 */
		String whereStatement = ccf.getWhereStatement();
		String staticStatement = getStaticStatement(ccf);
		if (staticStatement != null && staticStatement.length() > 0) {
			if (whereStatement != null && whereStatement.length() > 0)
				whereStatement = " ( " + whereStatement + " ) AND ";
			whereStatement = whereStatement + " ( " + staticStatement + " ) ";
		}
		
		if(logger.isDebugEnabled())
			logger.debug("using where statement [" + whereStatement + "]");

		/**
		 * part after from before where
		 */
		String qStr = ccf.getAlias();
		String ext = ccf.getExt();
		if (ext != null && ext.length() > 0)
			qStr = qStr + ", " + ext;
		
		if(logger.isDebugEnabled())
			logger.debug("using from ... statement [" + qStr + "]");

		/**
		 * order by clause
		 */
		String orderBy = ccf.getOrderBy();
		if (orderBy != null && orderBy.length() > 0)
			orderBy = " order by " + orderBy;

		if (whereStatement != null && whereStatement.length() > 0) {
			query = hibernateSession.createQuery(" from "
					+ ccf.getSelectStatementDAO() + " " + qStr + " "
					+ " where " + whereStatement + orderBy);
			queryTemplate.setQuery(query);
			getAppendedBindings(ccf, queryTemplate);
		} else {
			query = hibernateSession.createQuery(" from "
					+ ccf.getSelectStatementDAO() + orderBy);
			queryTemplate.setQuery(query);
		}
		
		if(logger.isDebugEnabled())
			logger.debug("using query string [" + query.getQueryString() + "]");
		
		return queryTemplate;
	}

	/**
	 * value of static statement attribute
	 * @param ccf Filter instance
	 * @return property value
	 */
	public String getStaticStatement(CCFilter ccf) {
		String attr = ccf.getStaticStatementAttribute();
		if (attr == null || attr.length() == 0)
			return null;
		
		if(logger.isDebugEnabled())
			logger.debug("using static statement in " + attr);

		Object o = JDBClusterUtil.invokeGetPropertyMethod(attr, ccf);
		if (o == null) {
			if(logger.isDebugEnabled())
				logger.debug("static statement is null");
			return null;
		}

		if(logger.isDebugEnabled())
			logger.debug("returning value [" + o.toString() + "]");
		
		return o.toString();
	}

	/**
	 * recursive method that retrieves all appended filters and their bindings
	 * 
	 * @param ccf
	 *            CCFilter that contains the binding
	 */
	public void getAppendedBindings(CCFilter ccf, HibernateQuery queryTemplate) {
			
		// while there are appended filters
		while (ccf != null) {
			if(logger.isDebugEnabled())
				logger.debug("binding filter class name [" + ccf.getClass().getName()+"]");

			if (ccf.getBinding() != null) {
				Iterator paramNameIter = ccf.getBinding().keySet().iterator();
				Iterator propParthIter = ccf.getBinding().values().iterator();

				// while HashMaps has bindings
				while (paramNameIter.hasNext()) {
					String paramName = (String) paramNameIter.next();
					String propPath = (String) propParthIter.next();

					if(logger.isDebugEnabled())
						logger.debug("binding property [" + propPath + "] to parameter [" + paramName);
					
					Object val = JDBClusterUtil.invokeGetPropertyMethod(propPath, ccf);

					if(logger.isDebugEnabled())
						logger.debug("using value = " + val.toString());
					
					// set the binding to the query
					queryTemplate.getQuery().setParameter(paramName, val);
				}
			}
			// call the method recursive with the appended filter as an argument
			getAppendedBindings(ccf.getAppendedFilter(), queryTemplate);
			return;
		}

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
	 * @param object
	 *            a persistent or detached cluster instance
	 */
	public void refresh(Object object) {
		hibernateSession.refresh(object);
	}

}
