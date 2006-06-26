package test;

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

import test.testfilter.NameFilter;

public class TestClusterAndDB extends TestCase {
	
	SessionTemplate session;
	
	SessionFactoryTemplate sf;
	
	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");
		
		JDBClusterSimpleConfig.setConfiguration("xml/jdbcluster.conf.xml");
		JDBClusterSimpleConfig.setHibernateConfiguration(null, "mapping/hibernate.cfg.xml");

		////////using HIBERNATE means
		//get a Configuration instance
		ConfigurationTemplate cf = ConfigurationFactory.getInstance();
		//get a factory for sessions
		sf = cf.buildSessionFactory();
		session = sf.openSession();
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
		CCar bmw = ClusterFactory.newInstance(cAutoType);
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
		
		List<CCar> list = nameFilterQuery.list();
		CCar myQueriedAuto = list.get(0);
		assertEquals("BMW", myQueriedAuto.getName());
	}
	
	public void testClusterLoad() {
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		CCar bmw = ClusterFactory.newInstance(cAutoType);
		bmw.setName("BMWforTest");
		session.save(bmw);
		tx.commit();
		
		// test in a second session
		SessionTemplate session2 = sf.openSession();
		
		CCar car2 = (CCar) session2.load(CCar.class, bmw.getId());
		assertEquals("BMWforTest", car2.getName());
		session2.close();
		
		// test in a third session
		SessionTemplate session3 = sf.openSession();
		CCar car3 = ClusterFactory.newInstance(cAutoType);
		session3.load(car3, bmw.getId());
		assertEquals("BMWforTest", car3.getName());
		session3.close();
		
		// test in a forth session
		SessionTemplate session4 = sf.openSession();
		CCar car4 = ClusterFactory.newInstance(cAutoType);
		session4.get(car4, bmw.getId());
		assertEquals("BMWforTest", car4.getName());
		session4.close();
	}
	
	public void testSession() {
		TransactionTemplate tx = session.beginTransaction();
		
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		
		//create a Cluster and persist it
		CCar bmw = ClusterFactory.newInstance(cAutoType);
		bmw.setName("BMWforTest");
		session.save(bmw);
		tx.commit();
		
		// test in a second session
		SessionTemplate session1 = sf.openSession();
		session1.beginTransaction();
		
		SessionTemplate session2 = sf.openSession();
		TransactionTemplate tx2 = session2.beginTransaction();
		CCar car1 = (CCar) session1.get(CCar.class, bmw.getId());
		CCar car2 = (CCar) session2.get(CCar.class, bmw.getId());
		car2.setName("changed");
		
		//session2.merge(car2);
		session2.saveOrUpdate(car2);
		tx2.commit();
		
		session2.close();
		
		session1.close();
	}
}
