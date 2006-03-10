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

import org.jdbcluster.clustercontainer.identifier.Identifier;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.metapersistence.cluster.ICluster;
import org.jdbcluster.template.SessionTemplate;

public class ClusterContainerImpl2 implements ClusterContainer, Notification {

	public SessionTemplate getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSession(SessionTemplate session) {
		// TODO Auto-generated method stub

	}

	public void fill(CCFilter iccf) {
		// TODO Auto-generated method stub

	}

	public void fill() {
		// TODO Auto-generated method stub

	}

	public void dBUpdate(Identifier id) {
		// TODO Auto-generated method stub

	}

	public void dBUpdateAt(int index) {
		// TODO Auto-generated method stub

	}

	public void dBDelete(Identifier id) {
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

	public int indexOf(Identifier id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int indexOf(ICluster cluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public CCFilter getICCFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClusterType getClusterType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addClusterNotificationListener(ClusterNotificationListener l) {
		// TODO Auto-generated method stub

	}

	public void removeClusterNotificationListener(ClusterNotificationListener l) {
		// TODO Auto-generated method stub

	}

	public void clusterNotification(ClusterNotificationEvent e) {
		// TODO Auto-generated method stub
		
	}

}
