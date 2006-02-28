package org.jdbcluster.template;



public interface SessionFactoryTemplate {

	public SessionTemplate openSession();
	public SessionTemplate getSession();

}
