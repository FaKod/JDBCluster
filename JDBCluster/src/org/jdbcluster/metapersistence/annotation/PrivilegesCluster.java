package org.jdbcluster.metapersistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * defines required privileges for method access on a 
 * implementation of interface PrivilegedCluster
 * @see org.jdbcluster.privilege.PrivilegedCluster
 * @author FaKod
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PrivilegesCluster {
	/**
	 * @return array of required privileges
	 */
	String[] required();
	
	/**
	 * array of properties. Their contents will be used to
	 * calculate the required privileges
	 * @return array of property names
	 */
	String[] property();
}
