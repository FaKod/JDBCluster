package test.clusterinterceptor;

import mycluster.CBicycle;

import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.cluster.ClusterInterceptor;

public class MyClusterInterceptor implements ClusterInterceptor {

	public boolean clusterNew(Cluster cluster) {
		if(cluster instanceof CBicycle) {
			CBicycle b = (CBicycle) cluster;
			b.setDurchmesser(123);
			return true;
		}
		return false;
	}

}
