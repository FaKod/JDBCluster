package dao;

import org.jdbcluster.dao.Dao;

public class Fahrzeug extends Dao{

	String name;
	long id;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
