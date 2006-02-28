package org.jdbcluster.template;

import org.jdbcluster.filter.CCFilter;

public interface SessionTemplate {

	public TransactionTemplate beginTransaction();
	public void cancelQuery();
	public QueryTemplate createQuery(String queryString);
	/**
	 * creates a query with given parameters and saves the query object
	 * (to get the result later on)
	 * @param ccf the filter that contains the criteria for the select statement
	 * @return QueryTemplate the query object
	 */
	public QueryTemplate createQuery(CCFilter ccf);
	public void delete(Object o);
	public void saveOrUpdate(Object o);
	public void save(Object o);
	public void close();
	public void update(Object o);
	public void save(String id, Object o);
}
