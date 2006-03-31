package org.jdbcluster.metapersistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adding additional master dependancies
 * @author FaKod
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface AddDomainDependancy {
	String[] addDepFromProp();
}
