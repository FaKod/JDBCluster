package org.jdbcluster.metapersistence.aspects;

import org.jdbcluster.template.*;
import org.jdbcluster.metapersistence.cluster.*;

/**
 * @author Christopher Schmidt
 * calles to save(), update() etc. have to use cluster objects
 */
public aspect ClusterSessionAspect extends CAspect {

	pointcut sessionOperation(Object o): 
		(call(* SessionTemplate+.*(Object)) && args(o));
	
	Object around(Object o): sessionOperation(o) {
		if(o instanceof ClusterBase) {
			return proceed(((ClusterBase)o).dao);
		}
		throw new IllegalArgumentException("Please use only ClusterBase Objects"); 
    }
}
