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

import org.hibernate.SessionFactory;
import org.jdbcluster.template.SessionFactoryTemplate;

/**
 * 
 * @author Philipp Noggler
 *
 */
public class HibernateSessionFactory implements SessionFactoryTemplate {

	private org.hibernate.SessionFactory factory;
	private HibernateSession session;


	public HibernateSessionFactory(SessionFactory hibernateFactory) {
		factory = hibernateFactory;
	}

	public HibernateSession openSession() {
		session = new HibernateSession();
		session.setHibernateSession(factory.openSession());
		return session;
	}
	
	public org.hibernate.SessionFactory getFactory() {
		return factory;
	}

	public void setFactory(org.hibernate.SessionFactory factory) {
		this.factory = factory;
	}

	public HibernateSession getSession() {
		return session;
	}

	public void setSession(HibernateSession session) {
		this.session = session;
	}
	
	

}
