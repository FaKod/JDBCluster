package org.jdbcluster.clustercontainer;

import org.jdbcluster.clustercontainer.identifier.Identifier;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.template.SessionTemplate;

/**
 * central container for cluster objects
 * holds cluster as a resultset defined through CCFiler
 * keeps objects actual through notification
 * @author Christopher Schmidt
 *
 */
public interface ClusterContainer {

	/**
	 * returns actual stored session
	 * @return actual stored session
	 */
	public SessionTemplate getSession();

	/**
	 * sets actual sessionTemplate to use
	 * @param session session to use
	 */
	public void setSession(SessionTemplate session);

	/**
	 * removes all elements from container and queries for
	 * the through CCFilter defines resultset
	 * @param iccf
	 */
	public void fill(CCFilter iccf);

	/**
	 * fills container with all available ClusterType objects
	 *
	 */
	public void fill();

	/**
	 * updated object identified through id
	 * @param id primary key of Cluster object
	 */
	public void dBUpdate(Identifier id);

	/**
	 * delete object identified through id
	 * @param id primary key of Cluster object
	 */
	public void dBDelete(Identifier id);

	public void dBInsert(Cluster cluster);

	public void dBInsertAt(Cluster cluster, int index);

	public Cluster get();

	public Cluster getElementAt(int index);

	public int indexOf(Identifier id);

	public int size();

	public void addClusterNotificationListener(ClusterNotificationListener l);

	public void removeClusterNotificationListener(ClusterNotificationListener l);

	public CCFilter getICCFilter();

	public int getClusterTypeIndex();

	public ClusterType getClusterType();

}
