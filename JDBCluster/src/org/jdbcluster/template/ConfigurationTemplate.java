package org.jdbcluster.template;

public interface ConfigurationTemplate {

	public void setConfiguration(Object cfg);
	public SessionFactoryTemplate buildSessionFactory();
}
