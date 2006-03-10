package test;

import junit.framework.TestCase;

import org.jdbcluster.domain.DomainBase;
import org.jdbcluster.domain.DomainConfigImpl;

public class TestDomainConfig extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testDomain() {
		new DomainConfigImpl("xml/testDomainCheck.xml");
	}

}
