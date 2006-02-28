package org.jdbcluster.template.hibernate;

import org.hibernate.SessionFactory;
import org.jdbcluster.template.SessionFactoryTemplate;

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
