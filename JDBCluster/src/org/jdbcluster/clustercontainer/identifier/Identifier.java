package org.jdbcluster.clustercontainer.identifier;

import java.util.Vector;

public interface Identifier {

	public abstract void setFirstIdAttribute(Long value);
	public abstract Vector getIdVector();
	public abstract boolean isNull();
	public abstract boolean equals(Object other);
	public abstract int hashCode();
}
