package org.jdbcluster.privilege;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.domain.DomainCheckerImpl;
import org.jdbcluster.domain.DomainPrivilegeList;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.PrivilegesCluster;
import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;

/**
 * 
 * @author FaKod
 *
 */
public class PrivilegeCheckerImpl extends PrivilegeBase implements PrivilegeChecker {
	
	/**
	 * maps: Class -> Method -> HashSet of privileges
	 */
	private static HashMap<Class<?>, HashMap<Method, HashSet<String>>> privileges = new HashMap<Class<?>, HashMap<Method, HashSet<String>>>();

	/**
	 * should do an intersection between the user privileges and
	 * the privileges given through requiredPrivileges
	 * @param requiredPrivileges the required privileges
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(Set<String> requiredPrivileges) {
		return getUserPrivilege().userPrivilegeIntersect(requiredPrivileges);
	}
	
	/**
	 * intersects required privileges against given privileges
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(Method calledMethod, PrivilegedCluster clusterObject) {
		String methodProperty = calledMethod.getName().substring(3);
		DomainCheckerImpl dc = new DomainCheckerImpl();
		HashSet<String> priv = new HashSet<String>(getPrivilegesCluster(calledMethod, clusterObject));
		PrivilegesCluster pcAnno = clusterObject.getClass().getAnnotation(PrivilegesCluster.class);
		for(String property : pcAnno.property() ) {
			if(!methodProperty.equalsIgnoreCase(property)) {
				Field f = JDBClusterUtil.getField(property, clusterObject);
				if(f==null)
					throw new ConfigurationException("property [" + property + "] does not exist");
				
				String domId = getDomainIdFromField(f);
				String value = (String) JDBClusterUtil.invokeGetPropertyMethod(property, clusterObject);
				DomainPrivilegeList dpl;
				try {
					dpl = (DomainPrivilegeList) dc.getDomainListInstance(domId);
				}
				catch(ClassCastException e) {
					throw new ConfigurationException("privileged domain needs implemented DomainPrivilegeList Interface",e);
				}
				
				priv.addAll(dpl.getDomainEntryPivilegeList(domId, value));
			}
		}
		return userPrivilegeIntersect(priv);
	}
	
	/**
	 * calculates and stores static required privileges
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return return reqired privileges
	 */
	private HashSet<String> getPrivilegesCluster(Method calledMethod, PrivilegedCluster clusterObject) {
		HashMap<Method, HashSet<String>> mp = privileges.get(clusterObject.getClass());
		if(mp==null) {
			mp = new HashMap<Method, HashSet<String>>();
			privileges.put(clusterObject.getClass(),mp);
		}
		HashSet<String> hs = mp.get(calledMethod);
		if(hs==null) {
			hs = calcClusterPrivileges(calledMethod, clusterObject);
			mp.put(calledMethod, hs);
		}
		return hs;	
	}
	
	/**
	 * calculates static required privileges
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return return reqired privileges
	 */
	private HashSet<String> calcClusterPrivileges(Method calledMethod, PrivilegedCluster clusterObject) {
		HashSet<String> hs = new HashSet<String>();
		PrivilegesMethod pmAnno = calledMethod.getAnnotation(PrivilegesMethod.class);
		PrivilegesCluster pcAnno = clusterObject.getClass().getAnnotation(PrivilegesCluster.class);
		
		if(pcAnno!=null) {
			for(String s : pcAnno.required()) {
				hs.add(s);
			}
		}
		if(pmAnno!=null) {
			for(String s : pmAnno.required()) {
				hs.add(s);
			}
		}
		return hs;
	}
	
	/**
	 * gets domain id from annotations
	 * @see org.jdbcluster.domain.DomainBase
	 * there is nearly the same method
	 * @param f the field instance
	 * @return the domain id
	 */
	private String getDomainIdFromField(Field f) {
		DomainDependancy dd = f.getAnnotation(DomainDependancy.class);
		if(dd!=null)
			return dd.domainId();
		Domain d = f.getAnnotation(Domain.class);
		if(d==null)
			throw new ConfigurationException("no annotation @Domain or @DomainDependancy found on: " + f.getName());
		return d.domainId();
	}
}
