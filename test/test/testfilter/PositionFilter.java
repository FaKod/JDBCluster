package test.testfilter;

import org.jdbcluster.filter.CCFilterImpl;

/**
 * 
 * @author Philipp Noggler
 * class PositionFilter represents a specific Filter that
 * has 2 attributes to set coordinates
 *
 */

public class PositionFilter extends CCFilterImpl {

	private double longitude;
	private double latitude;
	
	
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
	
	
}


