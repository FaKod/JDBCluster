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

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.jdbcluster.template.SessionFactoryTemplate;

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

	public HibernateSessionFactory(SessionFactory hibernateFactory) {
		factory = hibernateFactory;
	}

	public HibernateSession openSession() {
		HibernateSession session = new HibernateSession();

		if(interceptor==null)
			session.setHibernateSession(factory.openSession());
		else
			session.setHibernateSession(factory.openSession(interceptor));
		return session;
	}
	
	public HibernateStatelessSession openStatelessSession() {
		HibernateStatelessSession session = new HibernateStatelessSession();
		session.setHibernateSession(factory.openStatelessSession());		
		return session;
	}
	
	public org.hibernate.SessionFactory getFactory() {
		return factory;
	}

	public void setFactory(org.hibernate.SessionFactory factory) {
		this.factory = factory;
	}

	public HibernateSession getSession() {
		HibernateSession session = new HibernateSession();
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
}
