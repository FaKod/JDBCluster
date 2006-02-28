package org.jdbcluster.dao;

public class Dao {

	protected long id;
	private static DaoConfig daoConfig;

	public DaoConfig getDaoConfig() {
		return daoConfig;
	}

	public static void setDaoConfig(DaoConfig daoConfig) {
		Dao.daoConfig = daoConfig;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
