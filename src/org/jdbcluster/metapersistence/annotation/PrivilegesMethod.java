package org.jdbcluster.metapersistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * additional required privileges for method access
 * @see org.jdbcluster.metapersistence.annotation.PrivilegesCluster
 * @author FaKod
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PrivilegesMethod {
	/**
	 * @return array of required privileges
	 */
	String[] required();
}
