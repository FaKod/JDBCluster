package org.jdbcluster.clustercontainer;

import org.jdbcluster.clustercontainer.identifier.Identifier;

public interface ClusterNotificationEvent {

	public Identifier[] getEventType();
	public int clusterTypeIndex();
	public Identifier[] getChangedIds();
}
