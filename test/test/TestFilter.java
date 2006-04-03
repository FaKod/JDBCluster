package test;

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import mycluster.CCar;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.clustercontainer.ClusterContainerImpl;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeBase;
import org.jdbcluster.clustertype.ClusterTypeConfigImpl;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.filter.CCFilterBase;
import org.jdbcluster.filter.CCFilterFactory;
import org.jdbcluster.filter.ClusterSelectImpl;
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
import dao.Bicycle;

/**
 * 
 * @author Philipp Noggler
 * JUnit test to determine if returning select string is the same
 * as the given one
 *
 */

public class TestFilter extends TestCase {

	@Override
	protected void setUp() throws Exception {
		PropertyConfigurator.configure("xml/logging.properties");
		CCFilterBase.setFilterConfig(new ClusterSelectImpl("xml/selects.xml"));
		ClusterTypeBase.setClusterTypeConfig(new ClusterTypeConfigImpl("xml/clustertype.xml"));
		
		ConfigurationFactory.setConfigurationClass("org.jdbcluster.template.hibernate.HibernateConfiguration");
		ConfigurationFactory.getInstance().setConfiguration("mapping/hibernate.cfg.xml");

		super.setUp();
	}

	public void testWhere() {
		//this block creates some test objects, gets a select String from XML file
		//and compares the returned select String to the given one
		
		ClusterType type = ClusterTypeFactory.newInstance("car");
		PositionFilter impl = CCFilterFactory.newInstance(type, "position");
		impl.setLatitude(1.0);
		impl.setLongitude(2.0);
		String where = impl.getSelectString("position");
		assertEquals(where, "LATITUDE=:LAT and LONGITUDE=:LONG");
		
	}
	
	public void testBinding() {
		//this block creates some test objects, gets a select String from XML file
		//and compares the returned select String to the given one
		
		ClusterType type = ClusterTypeFactory.newInstance("car");
		PositionFilter impl = CCFilterFactory.newInstance(type, "position");
		impl.setLatitude(1.0);
		impl.setLongitude(2.0);
		HashMap<String,String> binding = new HashMap<String, String>();
		
		binding = impl.getBinding(type, "position");
		System.out.println("binding: " + binding.toString());

	}
	
	public void testTemplates() {
		//this block creates some test objects, gets a select String from XML file
		//and compares the returned select String to the given one
		
		ClusterType type = ClusterTypeFactory.newInstance("car");
		String selectID = "positionLatOnly";
		String SelectIDName = "name";
		PositionFilter position1 = CCFilterFactory.newInstance(type, selectID);
		NameFilter namefilter = CCFilterFactory.newInstance(type, SelectIDName);
		PositionFilter position2 = CCFilterFactory.newInstance(type, selectID);

		//select statement und where sind getrennt
//		namefilter.setSelectStatementDAO("from Auto where ");
		
		//append 2 filter
//		position2.append(namefilter);
		namefilter.append(position1);
		
		
		Car bmw = new Car();
		Bicycle fahrrad = new Bicycle();
		Car audi = new Car();
		Car opel = new Car();
		
		
		SessionTemplate session = ConfigurationFactory.getInstance().buildSessionFactory().openSession();

		System.out.println("Session" + session);
		TransactionTemplate tx = null;
		tx = session.beginTransaction();
		session.save(bmw);
		session.save(fahrrad);
		session.save(audi);
		session.save(opel);
		tx.commit();
		
		
		tx = session.beginTransaction();
		opel.setName("opel");
		audi.setName("audi");
		bmw.setName("bmw");
		opel.setLatitude(235435.465);
		opel.setLongitude(3435.3255);
		bmw.setLatitude(235435.465);
		bmw.setLongitude(3435.3255);
		audi.setLatitude(235435.465);
		audi.setLongitude(3435.3255);
		position1.setLatitude(235435.465);
		position1.setLongitude(3435.3255);
		position2.setLatitude(235435.465);
		position2.setLongitude(3435.3255);
		namefilter.setName("bmw");
		
		QueryTemplate temp = session.createQuery(namefilter);
		List list = ((HibernateQuery) temp).getQuery().list();
				
		tx.commit();
		session.close();
		
		for (int i = 0; i < list.size();i++) {
			System.out.println(((Car)list.get(i)).getName());
		}
		
	}

	
	public void testCluster() {
		ConfigurationTemplate cf = ConfigurationFactory.getInstance();
		SessionFactoryTemplate sf = cf.buildSessionFactory();
		SessionTemplate session = sf.openSession();

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
		
		for (int i = 0; i < list.size();i++) {
			System.out.println(((Car)list.get(i)).getName());
		}
		
		session.close();
	}
	
	public void testClusterContainer() {
		List list;
		ClusterType type = ClusterTypeFactory.newInstance("car");
		ClusterContainerImpl ccImpl = new ClusterContainerImpl(type);
		ccImpl.fill();
		System.out.println("Cluster Container liste: " + ccImpl.getClusterList().toString());
		list = ccImpl.getClusterList();
		
		for (int i = 0; i < list.size();i++) {
			System.out.println(((Car)list.get(i)).getName());
		}
		
	}

}