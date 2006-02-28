package org.jdbcluster.dao;

public interface DaoConfig {
	
	String getDaoClass(String daoId);
	String getDaoId(String daoClass);
	String[] getPropertyName(String daoId);
	String getPropertieValue(String daoId, String Property);
	String getPropertyClass(String daoId, String Property);
}
