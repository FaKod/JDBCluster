package test;

import org.jdbcluster.dao.DaoConfigImpl;

import junit.framework.TestCase;

public class TestConfig extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDaoConfig() {
		DaoConfigImpl daoConf = new DaoConfigImpl("xml/daotype.xml");
		String ret;
		ret = daoConf.getDaoClass("Car");
		ret = daoConf.getDaoId("dao.Car");
		String[] retArr = daoConf.getPropertyName("Car");
		String[] retArr2 = new String[retArr.length];
		String[] retArr3 = new String[retArr.length];
		int i = 0;
		for(String prop : retArr) {
			retArr2[i] = daoConf.getPropertieValue("Car", prop);
			retArr3[i++] = daoConf.getPropertyClass("Car",prop);
		}
	}

}
