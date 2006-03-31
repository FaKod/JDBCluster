package org.jdbcluster.privilege;

import java.lang.reflect.Method;

import org.aspectj.lang.reflect.MethodSignature;
import org.jdbcluster.exception.PrivilegeException;
import org.jdbcluster.metapersistence.annotation.PrivilegesCluster;
import org.jdbcluster.metapersistence.cluster.ClusterBase;
;

/**
 * used to check privileges
 * @author FaKod
 */
public privileged aspect PrivilegeAspect {

	pointcut execMethod(ClusterBase c):
		execution(* @PrivilegesCluster PrivilegedCluster+.*(..)) &&
		target(c);
	
	before(ClusterBase c) : execMethod(c) {
		PrivilegeCheckerImpl pc = new PrivilegeCheckerImpl();
		
		MethodSignature sig = (MethodSignature) thisJoinPoint.getSignature();
		Method m = sig.getMethod();	
		
		if(!pc.userPrivilegeIntersect(m, (PrivilegedCluster) c)) 
			throw new PrivilegeException("unsufficient privileges on Cluster: " +
					c.getClass().getName()+
					" calling method: " +
					m.getName());
	}

}
