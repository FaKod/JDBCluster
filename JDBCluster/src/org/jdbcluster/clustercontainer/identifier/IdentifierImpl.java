package org.jdbcluster.clustercontainer.identifier;

import java.util.Vector;

public class IdentifierImpl implements Identifier {

	private Vector<Long> identifiers;
	
	public void setFirstIdAttribute(Long value) {
		identifiers.add(value);	
	}

	public Vector getIdVector() {
		return identifiers;
	}

	public boolean isNull() {
		return identifiers.isEmpty();
	}

	public boolean equals(Object o) {
		return this.equals(o);
	}

	public int hashCode() {
		return identifiers.hashCode();
	}
	
}
