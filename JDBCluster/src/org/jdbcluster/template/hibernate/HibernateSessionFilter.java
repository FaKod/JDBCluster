package org.jdbcluster.template.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.jdbcluster.exception.CCFilterException;
import org.jdbcluster.template.SessionFilter;

/**
 * HibernateSessionFilter represents the implementation for a JDBCluster SessionFilter implementation and
 * relates to the Hibernate's Session Filter class <code>org.hibernate.Filter</code>
 * @author phil
 *
 */
public class HibernateSessionFilter implements SessionFilter {
	
	//the filter name of the Session filter
	private String filterName;
	//the name of the parameter binding
	private String parameterName;
	//0 or more values for parameter binding(s) if specified
	private List<Object> values = new ArrayList<Object>();
	

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public void setValue(Object value) {
		values.clear();
		values.add(value);
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public String getFilterName() {
		return filterName;
	}

	public Object getValue() {
		if (values.size() == 1) {
			return values.get(0);
		} else if (values.size() == 0) {
			return null;
		} else {
			throw new CCFilterException("Value Size is greater than 1 - Use getValues() to get several values!");
		}
	}

	public List<Object> getValues() {
		return values;
	}

}
