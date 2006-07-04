package test;

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import mycluster.CCar;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.filter.CCFilterFactory;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;
import org.jdbcluster.template.ConfigurationFactory;
import org.jdbcluster.template.ConfigurationTemplate;
import org.jdbcluster.template.QueryTemplate;
import org.jdbcluster.template.SessionFactoryTemplate;
import org.jdbcluster.template.SessionTemplate;
import org.jdbcluster.template.TransactionTemplate;
import org.jdbcluster.template.hibernate.HibernateQuery;

import test.testfilter.NameFilter;
import test.testfilter.PositionFilter;
import dao.Car;

/**
 * @author Philipp Noggler JUnit test to determine if returning select string is
 *         the same as the given one
 */

public class TestFilter extends TestCase {

	SessionTemplate session;

	SessionFactoryTemplate sf;

	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");

		JDBClusterSimpleConfig.setConfiguration("xml/jdbcluster.conf.xml");
		JDBClusterSimpleConfig.setHibernateConfiguration(null, "mapping/hibernate.cfg.xml");

		// //////using HIBERNATE means
		// get a Configuration instance
		ConfigurationTemplate cf = ConfigurationFactory.getInstance();
		// get a factory for sessions
		sf = cf.buildSessionFactory();
		session = sf.openSession();
		super.setUp();
	}

	public void testWhere() {
		// this block creates some test objects, gets a select String from XML file
		// and compares the returned select String to the given one

		ClusterType type = ClusterTypeFactory.newInstance("car");
		PositionFilter impl = CCFilterFactory.newInstance(type, "position");
		impl.setLatitude(1.0);
		impl.setLongitude(2.0);
		String where = impl.getSelectString("position");
		assertEquals(where, "LATITUDE=:LAT and LONGITUDE=:LONG");

	}

	public void testBinding() {
		// this block creates some test objects, gets a select String from XML file
		// and compares the returned select String to the given one

		ClusterType type = ClusterTypeFactory.newInstance("car");
		PositionFilter impl = CCFilterFactory.newInstance(type, "position");
		impl.setLatitude(1.0);
		impl.setLongitude(2.0);
		HashMap<String, String> binding = new HashMap<String, String>();

		binding = impl.getBinding(type, "position");
		System.out.println("binding: " + binding.toString());

	}
	
	public void testAppendedFilter() {
	
		/*
		 * test appended Filter
		 */
		PositionFilter pos = CCFilterFactory.newInstance("car", "position");
		pos.setLatitude(1.0);
		pos.setLongitude(2.0);
		
		NameFilter name = CCFilterFactory.newInstance("car", "name");
		name.setName("BMW");
		
		pos.append(name);
		
		assertEquals("LATITUDE=:LAT and LONGITUDE=:LONG AND NAME=:UNITNAME", pos.getWhereStatement());
		
		QueryTemplate q = session.createQuery(pos);
		List list = q.list();
		
		/*
		 * and vice versa
		 */
		PositionFilter pos2 = CCFilterFactory.newInstance("car", "position");
		pos2.setLatitude(1.0);
		pos2.setLongitude(2.0);
		
		NameFilter name2 = CCFilterFactory.newInstance("car", "name");
		name2.setName("BMW");
		
		name2.append(pos2);
		
		assertEquals("NAME=:UNITNAME AND LATITUDE=:LAT and LONGITUDE=:LONG", name2.getWhereStatement());
		
		QueryTemplate q2 = session.createQuery(name2);
		List list2 = q2.list();
	}
	
	public void testAppendedAndRemovedFilter() {
		
		/*
		 * test appended Filter
		 */
		PositionFilter pos = CCFilterFactory.newInstance("car", "position");
		pos.setLatitude(1.0);
		pos.setLongitude(2.0);
		
		NameFilter name = CCFilterFactory.newInstance("car", "name");
		name.setName("BMW");
		
		pos.append(name);
		
		assertEquals("LATITUDE=:LAT and LONGITUDE=:LONG AND NAME=:UNITNAME", pos.getWhereStatement());
		
		QueryTemplate q = session.createQuery(pos);
		List list = q.list();
		
		pos.remove();
		
		assertEquals("LATITUDE=:LAT and LONGITUDE=:LONG", pos.getWhereStatement());
		
		q = session.createQuery(pos);
		list = q.list();
	}

	public void testCluster() {
		
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		CCar bmw = ClusterFactory.newInstance(cAutoType);

		bmw.setName("BMW");

		session.save(bmw);
		tx.commit();

		NameFilter nameFilter = CCFilterFactory.newInstance(cAutoType, "name");
		nameFilter.setName("BMW");
		QueryTemplate nameFilterQuery = session.createQuery(nameFilter);

		List list = ((HibernateQuery) nameFilterQuery).getQuery().list();

		for (int i = 0; i < list.size(); i++) {
			System.out.println(((Car) list.get(i)).getName());
		}

		session.close();
	}

	public void testFilterExecutionTime() {
		long start, end;
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		NameFilter nameFilter;

		start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++)
			nameFilter = CCFilterFactory.newInstance(cAutoType, "name");
		System.out.println("Exec time: " + (System.currentTimeMillis() - start));
	}

}