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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.template.QueryTemplate;
import org.jdbcluster.template.SessionFactoryTemplate;
import org.jdbcluster.template.SessionTemplate;
import org.jdbcluster.template.TransactionTemplate;

/**
 * 
 * @author Christopher Schmidt
 * @author Philipp Noggler
 * implements SessionTemplate to use HIBERNATE
 */
public class HibernateSession implements SessionTemplate{


	//Hibernate session
	private Session hibernateSession;
	private HibernateSessionFactory factory;
	private static HibernateTransaction tx;
	private HibernateQuery queryTemplate;
	

	public HibernateSession() {}

	public TransactionTemplate beginTransaction() {
		tx = new HibernateTransaction();
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
		Query query = hibernateSession.createQuery(queryString);
		queryTemplate.setQuery(query);
		return queryTemplate;
	}
	
	public QueryTemplate getNamedQuery(String queryName) {
		Query query = hibernateSession.getNamedQuery(queryName);
		queryTemplate.setQuery(query);
		return queryTemplate;
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
	 * @param ccf the filter that contains the criteria for the select statement
	 * @return QueryTemplate the object holding the hibernate query
	 */
	public QueryTemplate createQuery(CCFilter ccf) {
		Query query;
		queryTemplate = new HibernateQuery();
		queryTemplate.setClusterType(ccf.getClusterType());
		// create the query with given selectstring and wherestring
		query = hibernateSession.createQuery(
				" from " + ccf.getSelectStatementDAO() +
				" where "+ ccf.getWhereStatement());
		queryTemplate.setQuery(query);
		getAppendedBindings(ccf);

		// return the whole object
		return queryTemplate;
	}

	/**
	 * recursive method that retrieves all appended filters and their
	 * bindings
	 * @param ccf CCFilter that contains the binding
	 */
	public void getAppendedBindings(CCFilter ccf) {
		Class clazz = null;
		Method meth = null;
		//while there are appended filters
		while (ccf != null) {
			
			try {
				// get the classname
				clazz = Class.forName(ccf.getClassName(), false, Thread
						.currentThread().getContextClassLoader());

			} catch (ClassNotFoundException e) {
				throw new org.jdbcluster.exception.ConfigurationException("no definition for the class [" + ccf.getClassName() + "] with the specified name could be found", e);
			}
			
			Object args[] = new Object[0];
			Iterator keyIter = ccf.getBinding().keySet().iterator();
			Iterator valueIter = ccf.getBinding().values().iterator();

			//while HashMaps has bindings
			while (keyIter.hasNext()) {
				String key = (String) keyIter.next();
				String value = (String) valueIter.next();
			
				/*
				 * change the first letter of the attribute defined in the
				 * xml "selects.xml" to an uppercase letter because of
				 * setter method
				 */
				
				String att = value.substring(1);
				String firstLetter = value.substring(0, 1).toUpperCase();
				try {
					
					// get the getter via reflection
					meth = clazz.getMethod(("get" + firstLetter + att), (Class[])null);
					
					// set the binding to the query
					queryTemplate.getQuery().setParameter(key, meth.invoke(ccf, args));
			
				} catch (IllegalArgumentException e) {
					throw new org.jdbcluster.exception.ConfigurationException("number of actual and formal parameters differ for the method " + "get" + firstLetter + att, e);
				} catch (IllegalAccessException e) {
					throw new org.jdbcluster.exception.ConfigurationException("no access to method " + "get" + firstLetter + att, e);
				} catch (InvocationTargetException e) {
					throw new org.jdbcluster.exception.ConfigurationException("the underlaying method throws exeption", e);
				} catch (SecurityException e) {
					throw new org.jdbcluster.exception.ConfigurationException("cant access method " + "get" + firstLetter + att, e);
				} catch (NoSuchMethodException e) {
				}
				
			}
			//call the method recursive with the appended filter as an argument
			getAppendedBindings(ccf.getAppendedFilter());
			return;
		}
		
	}
	
}
