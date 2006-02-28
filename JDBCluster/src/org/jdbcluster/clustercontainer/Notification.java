package org.jdbcluster.clustercontainer;

public interface Notification {

	 public void clusterNotification(ClusterNotificationEvent e);
	 public int getClusterTypeIndex();
}
