package test.clusterinterceptor;

import mycluster.CBicycle;

import org.jdbcluster.metapersistence.cluster.ClusterInterceptor;
import org.jdbcluster.metapersistence.cluster.ICluster;

public class MyClusterInterceptor implements ClusterInterceptor {

	public boolean clusterNew(ICluster cluster) {
		if(cluster instanceof CBicycle) {
			CBicycle b = (CBicycle) cluster;
			b.setDurchmesser(123);
			return true;
		}
		return false;
	}

	public boolean clusterRefresh(ICluster cluster) {
		return true;
	}

}
