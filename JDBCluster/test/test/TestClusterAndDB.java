package test;

import java.util.List;

import junit.framework.TestCase;
import mycluster.CCar;
import mycluster.ICar;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.filter.CCFilterFactory;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;
import org.jdbcluster.template.ConfigurationFactory;
import org.jdbcluster.template.ConfigurationTemplate;
import org.jdbcluster.template.QueryTemplate;
import org.jdbcluster.template.SessionFactoryTemplate;
import org.jdbcluster.template.SessionTemplate;
import org.jdbcluster.template.TransactionTemplate;

import test.testfilter.NameFilter;
import dao.Car;

public class TestClusterAndDB extends TestCase {
	
	static SessionTemplate session;
	
	static SessionFactoryTemplate sf = null;
	
	@Override
	protected void setUp() throws Exception {
		
		if(sf==null) {
			// configuring logging
			PropertyConfigurator.configure("xml/logging.properties");
			
			JDBClusterSimpleConfig.setConfiguration("xml/jdbcluster.conf.xml");
			JDBClusterSimpleConfig.setHibernateConfiguration(null, "mapping/hibernate.cfg.xml");
	
			////////using HIBERNATE means
			//get a Configuration instance
			ConfigurationTemplate cf = ConfigurationFactory.getInstance();
			//get a factory for sessions
			sf = cf.buildSessionFactory();
		}
		session = sf.openSession(null);
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		session.close();
		super.tearDown();
	}

	public void testCluster() {	
		//get a transaction
		TransactionTemplate tx = session.beginTransaction();
		
//		we use cluster auto now
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		ICar bmw = ClusterFactory.newInstance(cAutoType, null);
		assertEquals("MyDefaultAutoName", bmw.getName());
		
		bmw.setName("BMW");
		session.save(bmw);
		tx.commit();
		
		bmw.setName("BMW False");
		session.refresh(bmw);
		assertTrue(bmw.getName().equals("BMW"));
		
		//we want get our BMW back from DB
		NameFilter nameFilter = CCFilterFactory.newInstance(cAutoType, "name");
		nameFilter.setName("BMW");
		QueryTemplate nameFilterQuery = session.createQuery(nameFilter);
		
		List<ICar> list = nameFilterQuery.list();
		ICar myQueriedAuto = list.get(0);
		assertEquals("BMW", myQueriedAuto.getName());
	}
	
	public void testClusterLoad() {
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		ICar bmw = ClusterFactory.newInstance(cAutoType, null);
		bmw.setName("BMWforTest");
		session.save((Cluster)bmw);
		tx.commit();
		
		// test in a second session
		SessionTemplate session2 = sf.openSession(null);
		
		ICar car2 = (ICar) session2.load("car", bmw.getId());
		assertEquals("BMWforTest", car2.getName());
		session2.close();
		
		// test in a third session
		SessionTemplate session3 = sf.openSession(null);
		ICar car3 = ClusterFactory.newInstance(cAutoType, null);
		session3.load(car3, bmw.getId());
		assertEquals("BMWforTest", car3.getName());
		session3.close();
		
		// test in a forth session
		SessionTemplate session4 = sf.openSession(null);
		ICar car4 = ClusterFactory.newInstance(cAutoType, null);
		session4.get(car4, bmw.getId());
		assertEquals("BMWforTest", car4.getName());
		session4.close();
	}
	
	public void testSession() {
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		ICar bmw = ClusterFactory.newInstance(cAutoType, null);
		bmw.setName("BMWforTest");
		session.save(bmw);
		tx.commit();
		
		// test in a second session
		SessionTemplate session1 = sf.openSession(null);
		session1.beginTransaction();
		
		SessionTemplate session2 = sf.openSession(null);
		TransactionTemplate tx2 = session2.beginTransaction();
		ICar car2 = (ICar) session2.get("car", bmw.getId());
		car2.setName("changed");
		
		//session2.merge(car2);
		session2.saveOrUpdate(car2);
		tx2.commit();
		
		session2.close();
		
		session1.close();
	}
	
	public void testClusterRefresh() {
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		ICar bmw = ClusterFactory.newInstance(cAutoType, null);
		bmw.setName("BMWforTest");
		session.save(bmw);
		long id = bmw.getId();
		tx.commit();
		
		/*
		 * session two
		 */
		SessionTemplate session2 = sf.openSession(null);
		TransactionTemplate tx2 = session2.beginTransaction();
		
		ICar bmw2 = (ICar) session2.get("car", id);
		bmw2.setName("BMW Session 2");
		tx2.commit();
		session2.close();
		
		/*
		 * session one
		 */
		bmw.setName("This name is Wrong");
		
		session.refresh(bmw);
		
		assertEquals("BMW Session 2", bmw.getName());
	}
	
	public void testClusterClassFromDao() {
		Car car = new Car();
		Class<? extends Cluster> c = ClusterFactory.getClusterFromDao(car);
		Cluster cb = ClusterFactory.newInstance(c, car, null);
		assertSame(CCar.class, cb.getClass());
	}
	
	public void testTransaction() {
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		ICar bmw = ClusterFactory.newInstance(cAutoType, null);
		bmw.setName("BMWforTest");
		session.save(bmw);
		long id = bmw.getId();
		tx.commit();
		
		System.out.println(id);
	}
	
	public void testSessionFromCluster() {
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		ICar bmw = ClusterFactory.newInstance(cAutoType, null);
		bmw.setName("BMWforTest");
		session.save(bmw);
		long id = bmw.getId();
		tx.commit();
		
		/*
		 * session two
		 */
		SessionTemplate session2 = sf.openSession(null);
		TransactionTemplate tx2 = session2.beginTransaction();
		
		ICar bmw2 = (ICar) session2.get("car", id);
		bmw2.setName("BMW Session 2");
		tx2.commit();
		
		assertSame(session, sf.getSessionFromCluster(bmw));
		assertSame(session2,sf.getSessionFromCluster(bmw2));
		
		session2.close();
		assertSame(null, sf.getSessionFromCluster(bmw2));
		
		SessionTemplate session3 = sf.openSession(null);
		bmw2.setName("BMW Session 3");
		session3.refresh(bmw2);
		assertSame(session3,sf.getSessionFromCluster(bmw2));
		
		session2 = null;
		System.gc();
		System.runFinalization();
	}

}
