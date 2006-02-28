package dao;

public class Fahrrad extends Fahrzeug {

	private long id;
	private double durchmesser;
	
	public double getDurchmesser() {
		return durchmesser;
	}
	public void setDurchmesser(double durchmesser) {
		this.durchmesser = durchmesser;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	
}
