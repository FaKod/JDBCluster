package org.jdbcluster.metapersistence.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * If a domain setter is replaced a usage of a setter annotated
 * with DomainSetterReplaced is not allowed
 * @author FaKod
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainSetterReplaced {
}
