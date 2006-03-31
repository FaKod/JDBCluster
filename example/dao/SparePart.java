package dao;

import org.jdbcluster.dao.Dao;

public class SparePart extends Dao{

	String lieferant;
	long id;

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLieferant() {
		return lieferant;
	}

	public void setLieferant(String lieferant) {
		this.lieferant = lieferant;
	}
}
