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

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Vector;

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.impl.SessionImpl;
import org.jdbcluster.metapersistence.cluster.ICluster;
import org.jdbcluster.template.SessionFactoryTemplate;
import org.jdbcluster.template.SessionTemplate;

/**
 * 
 * @author Philipp Noggler
 * @author FaKod
 */
public class HibernateSessionFactory implements SessionFactoryTemplate {

	protected org.hibernate.SessionFactory factory;
	
	/*
	 * HIBERNATE Special treatment
	 * use standard method getNativeSessionFactory
	 */
	private Interceptor interceptor = null;
	
	/**
	 * used to store sessions generated with this session factory
	 */
	private List<WeakReference<HibernateSession>> sessionList = null;

	/**
	 * C tor stores hibernate factory and created two session vectors
	 * @param hibernateFactory the linked hibernate factory
	 */
	public HibernateSessionFactory(SessionFactory hibernateFactory) {
		factory = hibernateFactory;
		sessionList = new Vector<WeakReference<HibernateSession>>();
	}

	public HibernateSession openSession() {
		HibernateSession session = new HibernateSession(this);

		if(interceptor==null)
			session.setHibernateSession(factory.openSession());
		else
			session.setHibernateSession(factory.openSession(interceptor));
		sessionList.add(new WeakReference<HibernateSession>(session));
		return session;
	}
	
	public org.hibernate.SessionFactory getFactory() {
		return factory;
	}

	public void setFactory(org.hibernate.SessionFactory factory) {
		this.factory = factory;
	}

	public HibernateSession getSession() {
		HibernateSession session = new HibernateSession(this);
		session.setHibernateSession(factory.getCurrentSession());
		return session;
	}

	@SuppressWarnings("unchecked")
	public <T> T getNativeSessionFactory() {
		return (T) this;
	}
	
	public Interceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}
	
	/**
	 * removes hibernate session from list
	 * @param session Hibernate Session to remove
	 */
	synchronized void removeSessionFromSessionList(HibernateSession session) {
		for( int i=0; i < sessionList.size() ; i++) {
			HibernateSession hs = sessionList.get(i).get();
			if(hs==null || hs.equals(session)) {
				sessionList.remove(i--);
			}
		}
	}
	
	/**
	 * Returns the current session the cluster object is currently connected
	 * @param cluster Cluster object
	 * @return SessionTemplate if found. Null if the session is not found or closed.
	 */
	public synchronized SessionTemplate getSessionFromCluster (ICluster cluster) {
		for(WeakReference<HibernateSession> wr : sessionList) {
			HibernateSession hs = wr.get();
			if(hs!=null) {
				SessionImpl s = (SessionImpl) hs.getNativeSession();
				if(s.isOpen() && s.contains(cluster.getDao()))
					return hs;
			}
		}
		return null;
	}
}
