package org.jdbcluster.metapersistence.aspects;

import java.util.Hashtable;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * implementation of a singleton pattern
 */
public abstract aspect SingletonPattern issingleton( )
{
	private Hashtable<Class<Singleton>, Object> singletons = new Hashtable<Class<Singleton>, Object>();

	public interface Singleton {
	}

	// Pointcut to define specify an interest in all creations
	// of all Classes that extend Singleton
	pointcut selectSingletons() : call((Singleton +).new (..));

	@SuppressAjWarnings("adviceDidNotMatch")
	Object around() : selectSingletons( )
	{
		Class<Singleton> singleton = thisJoinPoint.getSignature().getDeclaringType();
		synchronized (singletons) {
			if (singletons.get(singleton) == null) {
				singletons.put(singleton, proceed());
			}
		}
		return (Object) singletons.get(singleton);
	}
}