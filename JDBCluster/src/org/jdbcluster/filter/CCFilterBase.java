/*
 * Copyright 2002-2005 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jdbcluster.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.BindingException;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.template.hibernate.HibernateQuery;

/**
 * abstract class CCFilterBase provides information about a filter. Since it is
 * an abstract class, it cannot be instanciatet but specific filter extends that
 * class.
 * 
 * @author Philipp Noggler
 */

public abstract class CCFilterBase implements CCFilter {

	protected Logger logger = Logger.getLogger(this.getClass());

	static private ClusterSelect select;

	private HashMap<String, String> binding;
	
	private String filterName;

	private String className;

	private CCFilter appendedFilter = null;

	private String whereStatement;

	private String alias;

	private String ext;

	private String staticStatementAttribute;

	private String orderBy;
	
	private List<String> annotation;

	private String selectStatementDAO;
	
	private String fetch;

	private ClusterType clusterType;

	public CCFilterBase() {
	}

	public CCFilterBase(ClusterType ct) {
		this.clusterType = ct;
	}

	/**
	 * returns select object
	 * 
	 * @return ClusterSelect
	 */
	public static ClusterSelect getSelect() {
		return select;
	}

	/**
	 * sets the ClusterSelect
	 * 
	 * @param select
	 */
	public static void setFilterConfig(ClusterSelect select) {
		CCFilterBase.select = select;
	}

	/**
	 * returns the wherestatement as a String
	 * 
	 * @return String
	 */
	public String getWhereStatement() {
		CCFilterBase appendedFilter = (CCFilterBase) getAppendedFilter();
		if (appendedFilter != null) {
			String tmp = appendedFilter.getWhereStatement();
			if (tmp != null && tmp.length() > 0) {
				String tempWhere = "";
				if(whereStatement != null && whereStatement.length() > 0) {
					tempWhere = "(" + whereStatement + ") AND ";
				}
				return tempWhere + "(" + tmp + ")";
			}
		}
		return whereStatement;
	}

	/**
	 * sets the where statement as a String
	 * 
	 * @param hql
	 */
	protected void setWhereStatement(String hql) {
		this.whereStatement = hql;
	}

	/**
	 * returns the binding mapped in a HashMap
	 * 
	 * @return HashMap
	 */
	public HashMap<String, String> getBinding() {
		return binding;
	}

	/**
	 * set the binding mapped in a HashMap
	 * 
	 * @param binding the HashMap to be set
	 */
	protected void setBinding(HashMap<String, String> binding) {
		this.binding = binding;
	}

	/**
	 * returns the classname of a filter as a String
	 * 
	 * @return className the classname of that filter
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * set the classname of a filter
	 * 
	 * @param className the classname to be set
	 */
	protected void setClassName(String className) {
		this.className = className;
	}

	/**
	 * returns the filter which is appended to the root filter
	 * 
	 * @return CCFilter the appended filter
	 */
	public CCFilter getAppendedFilter() {
		return appendedFilter;
	}

	/**
	 * set a filter as an appended filter to the root filter
	 * 
	 * @param appendedFilter the filter to be set
	 */
	public void setAppendedFilter(CCFilter appendedFilter) {
		this.appendedFilter = appendedFilter;
	}

	/**
	 * returns the selectstatement (without the where clause) of a filter
	 * 
	 * @return selectStatement the select statement
	 */
	public String getSelectStatementDAO() {
		return selectStatementDAO;
	}

	/**
	 * set the select statement of a filter
	 * 
	 * @param selectStatement the statement to be set
	 */
	public void setSelectStatementDAO(String selectStatementDAO) {
		this.selectStatementDAO = selectStatementDAO;
	}

	/**
	 * returns the clustertype of a filter
	 * 
	 * @return ClusterType
	 */
	public ClusterType getClusterType() {
		return clusterType;
	}

	/**
	 * set the clustertype of a filter
	 * 
	 * @param clusterType the ClusterType to be set
	 */
	protected void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}

	public String getAlias() {
		CCFilterBase appendedFilter = (CCFilterBase) getAppendedFilter();
		if (appendedFilter != null) {
			String tmp = appendedFilter.getAlias();
			if (tmp != null && tmp.length() > 0) {
				if(alias == null || alias.length() == 0) {
					return tmp;
				} else if(!alias.equals(tmp)) {
					throw new ConfigurationException("Alias of filter and appended filter must be equal!!");
				}
			}
		}
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getExt() {
		CCFilterBase appendedFilter = (CCFilterBase) getAppendedFilter();
		if (appendedFilter != null) {
			String tmp = appendedFilter.getExt();
			if (tmp != null && tmp.length() > 0) {
				if(ext != null && ext.length() > 0) {	
					return ext + ", " + tmp;
				}else {
					return tmp;
				}
			} 
		}
		return ext;
	}
	
	public List<String> getAnnotation() {
		List<String> annotations = new ArrayList<String>();
		
		CCFilterBase appendedFilter = (CCFilterBase) getAppendedFilter();
		if (appendedFilter != null) {
			annotations.addAll(appendedFilter.getAnnotation()); 
		}
		
		if (annotation != null){			
			for (String tmp : annotation){
				if (!annotations.contains(tmp)){
					annotations.add(tmp);
				}
			}
		}
		return annotations;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getStaticStatementAttribute() {
		return staticStatementAttribute;
	}

	public void setStaticStatementAttribute(String staticStatementAttribute) {
		this.staticStatementAttribute = staticStatementAttribute;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getFetch() {
		CCFilterBase appendedFilter = (CCFilterBase) getAppendedFilter();
		if (appendedFilter != null) {
			String tmp = appendedFilter.getFetch();
			if (tmp != null && tmp.length() > 0) {
				if(fetch != null && fetch.length() > 0) {	
					return fetch + " " + tmp;
				}else {
					return tmp;
				}
			} 
		}
		return fetch;
	}

	public void setFetch(String fetch) {
		this.fetch = fetch;
	}

	/**
	 * value of static statement attribute
	 * 
	 * @param ccf Filter instance
	 * @return property value
	 */
	public String getStaticStatement() {

		String attr = getStaticStatementAttribute();
		if (attr == null || attr.length() == 0)
			return null;

		if (logger.isDebugEnabled())
			logger.debug("using static statement in " + attr);

		Object o = null;
		try {
			o = JDBClusterUtil.invokeGetPropertyMethod(attr, this);
		} catch (ConfigurationException ce) {
			throw new BindingException("cannot find getter for Property Path [" + attr + "] in Class [" + this.getClassName() + "]", ce);
		}

		if (o == null) {
			if (logger.isDebugEnabled())
				logger.debug("static statement is null");
			return null;
		}

		if (logger.isDebugEnabled())
			logger.debug("returning value [" + o.toString() + "]");

		CCFilter appendedFilter = getAppendedFilter();
		if (appendedFilter != null) {
			String tmp = appendedFilter.getStaticStatement();
			if (tmp != null && tmp.length() > 0)
				return "(" + o.toString() + ") AND (" + tmp + ")";
		}
		return o.toString();
	}

	/**
	 * recursive method that retrieves all appended filters and their bindings
	 * 
	 * @param queryTemplate HibernateQuery Query to bind parameter
	 */
	public void doBindings(HibernateQuery queryTemplate) {

		if (logger.isDebugEnabled())
			logger.debug("binding filter class name [" + this.getClass().getName() + "]");

		HashMap<String, String> binding = getBinding();
		if (binding != null) {

			for (String paramName : binding.keySet()) {
				String propPath = binding.get(paramName);

				if (logger.isDebugEnabled())
					logger.debug("binding property [" + propPath + "] to parameter [" + paramName);

				Object val = null;
				try {
					val = JDBClusterUtil.invokeGetPropertyMethod(propPath, this);
				} catch (ConfigurationException ce) {
					throw new BindingException("cannot find getter for Property Path [" + propPath + "] in Class [" + this.getClassName() + "]", ce);
				}
				if (logger.isDebugEnabled())
					logger.debug("using value = " + val);
				// set the binding to the query
				if(val instanceof Collection)
					queryTemplate.getQuery().setParameterList(paramName, (Collection)val);
				else
					queryTemplate.getQuery().setParameter(paramName, val);
			}
		}
		// call the method recursive with the appended filter as an argument
		CCFilter appendedFilter = getAppendedFilter();
		if (appendedFilter != null)
			appendedFilter.doBindings(queryTemplate);
		return;

	}

	/**
	 * returns a HasMap which contains the key mapped to an attribute
	 * 
	 * @param ct specifies the ClusterType
	 * @param selID specifies the select ID
	 * @return HashMap
	 */
	public HashMap<String, String> getBinding(ClusterType ct, String selID) {
		String className = getSelect().getClassName(ct, selID);
		HashMap<String, String> binding = getSelect().getBinding(ct, selID, className);
		return binding;
	}

	/**
	 * appends a Filter to another filter
	 * 
	 * @param CCFilter the Filter to be appended
	 */
	public void append(CCFilter filter) {
		setAppendedFilter(filter);
	}

	/**
	 * removes appended Filter
	 */
	public void remove() {
		setAppendedFilter(null);
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	
	public void setAnnotation(List<String> annotation) {
		this.annotation = annotation;
	}
}