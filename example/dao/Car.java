package dao;

import java.util.HashSet;
import java.util.Set;

public class Car extends Vehicle {

	Set ersatzteile = new HashSet();
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

	public Set getErsatzteile() {
		return ersatzteile;
	}

	public void setErsatzteile(Set ersatzteile) {
		this.ersatzteile = ersatzteile;
	}

	public Set getBesitzer() {
		return besitzer;
	}

	public void setBesitzer(Set besitzer) {
		this.besitzer = besitzer;
	}
}
