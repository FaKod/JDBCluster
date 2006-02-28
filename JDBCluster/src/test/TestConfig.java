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
		ret = daoConf.getDaoClass("Auto");
		ret = daoConf.getDaoId("dao.Auto");
		String[] retArr = daoConf.getPropertyName("Auto");
		String[] retArr2 = new String[retArr.length];
		String[] retArr3 = new String[retArr.length];
		int i = 0;
		for(String prop : retArr) {
			retArr2[i] = daoConf.getPropertieValue("Auto", prop);
			retArr3[i++] = daoConf.getPropertyClass("Auto",prop);
		}
	}

}
