package test.testfilter;

import org.jdbcluster.filter.CCFilterImpl;

public class NameFilter extends CCFilterImpl{

	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}


