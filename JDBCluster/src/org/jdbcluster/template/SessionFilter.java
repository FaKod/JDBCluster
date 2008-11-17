package org.jdbcluster.template;

import java.util.Collection;

/**
 * SessionFilter class represents a Session specific filter which can be enabled and disabled on the session. When enabled, result
 * sets are returned depending on the defined filter query (e.g. defined in the mapping file).
 * 
 * The following example shows a valid filter declaration:
 * <pre>
 * {@code
 * <filter-def name="contextFilter">
 *    <filter-param name="contextId" type="java.lang.Long"/>
 * </filter-def>
 *  }
 * </pre>
 * @author phil
 *
 */
public interface SessionFilter {
	
	/**
	 * returns the filter's name which is defined in a mapping file.
	 * @return the session filter's name.
	 */
	public String getFilterName();
	
	/**
	 * returns the name of the parameter binding. The parameter is not optional.
	 * @return the parameter's name.
	 */
	public String getParameterName();
	
	/**
	 * returns the value of the parameter binding. Make sure to use getValue() if there is just
	 * one parameter set. For more than 1 values use getValues() instead!
	 * @return the value of the parameter binding or null if there is no value set.
	 */
	public Object getValue();
	
	/**
	 * returns a Collection of parameter binding values.
	 * @return the values of the parameter binding or an empty Collection if there are no values set on the filter binding.
	 */
	public Collection<Object>getValues();

}
