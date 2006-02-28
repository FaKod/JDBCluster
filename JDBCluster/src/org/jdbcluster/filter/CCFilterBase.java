package org.jdbcluster.filter;

import java.util.HashMap;

import org.jdbcluster.clustertype.ClusterType;

/**
 * 
 * @author Philipp Noggler
 * abstract class CCFilterBase provides information about a filter. Since it is an
 * abstract class, it cannot be instanciatet but specific filter extends that class.
 */

public abstract class CCFilterBase {

	static private ClusterSelect select;
	private HashMap<String, String> binding;
	private String className;
	private CCFilter appendedFilter = null;
	private String whereStatement;
	private String selectStatementDAO;
	private ClusterType clusterType;

	public CCFilterBase() {}
	
	public CCFilterBase(ClusterType ct) {
		this.clusterType = ct;
	}
	
	/**
	 * returns select object
	 * @return ClusterSelect
	 */
	public static ClusterSelect getSelect() {
		return select;
	}
	
	/**
	 * sets the ClusterSelect
	 * @param select
	 */
	public static void setFilterConfig(ClusterSelect select) {
		CCFilterBase.select = select;
	}

	/**
	 * returns the wherestatement as a String
	 * @return String
	 */
	public String getWhereStatement() {
		return whereStatement;
	}

	/**
	 * sets the where statement as a String
	 * @param hql
	 */
	protected void setWhereStatement(String hql) {
		this.whereStatement = hql;
	}

	/**
	 * returns the binding mapped in a HashMap
	 * @return HashMap
	 */
	public HashMap<String, String> getBinding() {
		return binding;
	}

	/**
	 * set the binding mapped in a HashMap
	 * @param binding the HashMap to be set
	 */
	protected void setBinding(HashMap<String, String> binding) {
		this.binding = binding;
	}

	/**
	 * returns the classname of a filter as a String
	 * @return className the classname of that filter
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * set the classname of a filter
	 * @param className the classname to be set
	 */
	protected void setClassName(String className) {
		this.className = className;
	}

	/**
	 * returns the filter which is appended to the root filter
	 * @return CCFilter the appended filter
	 */
	public CCFilter getAppendedFilter() {
		return appendedFilter;
	}

	/**
	 * set a filter as an appended filter to the root filter
	 * @param appendedFilter the filter to be set
	 */
	public void setAppendedFilter(CCFilter appendedFilter) {
		this.appendedFilter = appendedFilter;
	}

	/**
	 * returns the selectstatement (without the where clause) of a filter
	 * @return selectStatement the select statement
	 */
	public String getSelectStatementDAO() {
		return selectStatementDAO;
	}

	/**
	 * set the select statement of a filter
	 * @param selectStatement the statement to be set
	 */
	public void setSelectStatementDAO(String selectStatementDAO) {
		this.selectStatementDAO = selectStatementDAO;
	}

	/**
	 * returns the clustertype of a filter
	 * @return ClusterType
	 */
	public ClusterType getClusterType() {
		return clusterType;
	}

	/**
	 * set the clustertype of a filter
	 * @param clusterType the ClusterType to be set
	 */
	protected void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}
}