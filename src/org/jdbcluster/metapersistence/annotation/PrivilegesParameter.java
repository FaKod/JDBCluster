package org.jdbcluster.metapersistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * defines required privileges for method parameter access
 * in a service
 * @author FaKod
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface PrivilegesParameter {

	/**
	 * array of properties. Their contents will be used to
	 * calculate the required privileges
	 * 
	 * Path separator for nested properties.
	 * Follows normal Java conventions: getFoo().getBar() would be "foo.bar".
	 * 
	 * Marker that indicates the start of a property key for an
	 * indexed or mapped property like "person.addresses[0]".
	 * 
	 * Marker that indicates the end of a property key for an
	 * indexed or mapped property like "person.addresses[0]".
	 * 
	 * @return array of property names
	 */
	String[] property() default {""};
}
