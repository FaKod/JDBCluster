package dao;

import java.util.HashSet;
import java.util.Set;

public class Car extends Vehicle {

	Set sparePart = new HashSet();
	Set besitzer = new HashSet();
	double longitude;
	double latitude;
	long id;
	
	

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set getSparePart() {
		return sparePart;
	}

	public void setSparePart(Set ersatzteile) {
		this.sparePart = ersatzteile;
	}

	public Set getBesitzer() {
		return besitzer;
	}

	public void setBesitzer(Set besitzer) {
		this.besitzer = besitzer;
	}
}
