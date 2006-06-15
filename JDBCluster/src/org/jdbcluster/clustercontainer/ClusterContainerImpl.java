/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.clustercontainer;

import java.util.ArrayList;
import java.util.List;

import org.jdbcluster.clustercontainer.identifier.Identifier;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.filter.CCFilterFactory;
import org.jdbcluster.filter.CCFilterImpl;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.cluster.ICluster;
import org.jdbcluster.template.QueryTemplate;
import org.jdbcluster.template.SessionTemplate;

/**
 * 
 * @author Philipp Noggler
 * class ClusterContainer holds all the Cluster Object of a specific ClusterType.
 * ....
 */

public class ClusterContainerImpl implements ClusterContainer, Notification {
	
	private List<Cluster> clusterList;
	private ClusterType clusterType;
	private CCFilter filter;
	private SessionTemplate session;
	private Identifier identifier;
	private ArrayList<ClusterNotificationListener> listeners;
	
	public SessionTemplate getSession() {
		return session;
	}

	public void setSession(SessionTemplate session) {
		this.session = session;
	}

	public ClusterContainerImpl() {
		new ClusterContainerImpl(null);
	}
	
	@SuppressWarnings("unchecked")
	public ClusterContainerImpl(ClusterType ct) {
		if (ct != null) {
			clusterType = ct;
		}
		listeners = new ArrayList();
	}

	@SuppressWarnings("unchecked")
	public void fill(CCFilter iccf) {
		QueryTemplate filterQuery = session.createQuery(iccf);
		clusterList = filterQuery.list();
		filter = iccf;
	}

	public void fill() {
		CCFilterImpl impl = CCFilterFactory.newInstance(clusterType, "");
		fill(impl);
	}

	//noch unklar
	public void dBUpdate(Identifier id) {
//		session.update(id);
	}

	//noch unklar
	public void dBDelete(Identifier id) {
//		session.delete(id);
	}

	public void dBInsert(Cluster cluster) {
		session.save(cluster);
		clusterList.add(cluster);
	}

	//noch unklar
	public void dBInsertAt(Cluster cluster, int index) {
		session.save(new Integer(index).toString(), cluster);
		clusterList.add(index, cluster);	
	}

	public Cluster get() {
		// TODO Auto-generated method stub
		return null;
	}

	public Cluster getElementAt(int index) {
		return clusterList.get(index); 
	}

	public int indexOf(Identifier id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int size() {
		return clusterList.size();
	}

	public void addClusterNotificationListener(ClusterNotificationListener l) {
		listeners.add(l);
		
	}

	public void removeClusterNotificationListener(ClusterNotificationListener l) {
		listeners.remove(l);
		
	}

	public CCFilter getICCFilter() {
		return filter;
	}

	//???
	public int getClusterTypeIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void clusterNotification(ClusterNotificationEvent e) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).notify(e);
		}
		
	}


	public ClusterType getClusterType() {
		return clusterType;
	}

	public void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}

	public ArrayList<ClusterNotificationListener> getListeners() {
		return listeners;
	}

	public void setListeners(ArrayList<ClusterNotificationListener> listeners) {
		this.listeners = listeners;
	}

	public List<Cluster> getClusterList() {
		return clusterList;
	}

	public void setClusterList(List<Cluster> clusterList) {
		this.clusterList = clusterList;
	}

	public void dBUpdateAt(int index) {
		// TODO Auto-generated method stub
		
	}

	public void dBDeleteAt(int index) {
		// TODO Auto-generated method stub
		
	}

	public void dBInsert(ICluster cluster) {
		// TODO Auto-generated method stub
		
	}

	public void dBInsertAt(ICluster cluster, int index) {
		// TODO Auto-generated method stub
		
	}

	public ICluster getCluster(Identifier id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ICluster getClusterAt(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public int indexOf(ICluster cluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
