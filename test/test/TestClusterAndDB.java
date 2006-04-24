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
		SessionFactoryTemplate sf = cf.buildSessionFactory();
		//and open one
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
}
