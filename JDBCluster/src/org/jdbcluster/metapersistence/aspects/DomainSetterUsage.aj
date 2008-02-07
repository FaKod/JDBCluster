package org.jdbcluster.metapersistence.aspects;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.annotation.DomainSetterReplaced;
import org.jdbcluster.domain.DomainCheck;

/**
 * declares an error if someone uses with @DomainSetterReplaced
 * annotated setter methods
 * @author FaKod
 */
public aspect DomainSetterUsage {
	
	declare precedence: DomainSetterUsage, DomainCheck, ClusterAttribute, *;

	@SuppressAjWarnings("adviceDidNotMatch")
	pointcut useDomainSetter(): 
		call(* Cluster+.set*(..)) && 
		!within(Cluster+) &&
		@annotation(DomainSetterReplaced);

	declare error: useDomainSetter(): 
		"usage of this setter is nott allowed. See @DomainSetterReplaced annotation";
}
