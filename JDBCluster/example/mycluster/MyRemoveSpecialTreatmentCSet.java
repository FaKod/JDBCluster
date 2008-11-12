package mycluster;

import org.jdbcluster.metapersistence.cluster.CSet;

public class MyRemoveSpecialTreatmentCSet<T extends Historical> extends CSet<T> {

	@Override
	public boolean remove(Object o) {
		T hist = (T) o;
		hist.setHistorical(true);
		return super.add(hist);
	}

}
