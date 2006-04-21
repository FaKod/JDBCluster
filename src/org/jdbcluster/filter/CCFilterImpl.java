/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.filter;

import java.util.HashMap;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.BindingException;

/**
 * class CCFilterImpl implements functionality to
 * get bindings from XML file, to get a HQL String from XML file and to
 * append a filter.
 * @author Philipp Noggler
 * @author FaKod
 * @author Tobi 
 */

public class CCFilterImpl extends CCFilterBase implements CCFilter {

	public CCFilterImpl() {
	}

	protected CCFilterImpl(ClusterType ct) {
		super(ct);
	}

	/**
	 * calls getWhere in ClusterSelectImpl and returns a String to generate a
	 * query
	 * 
	 * @param clusterType
	 *            specifies the ClusterType
	 * @param selectID
	 *            specifies the select ID
	 */
	public String getSelectString(String selectID) {
		String hql = getSelect().getWhere(getClusterType(), selectID);
		return hql;
	}

	/**
	 * appends a Filter to another filter
	 * 
	 * @param CCFilter
	 *            the Filter to be appended
	 */
	public void append(CCFilter filter) {
		HashMap<String, String> temp = new HashMap<String, String>();

		while (filter != null) {
			HashMap<String, String> hm = getBinding();
			if(hm == null) {
				setBinding(new HashMap<String, String>());
				hm = getBinding();
			}
			temp.putAll(hm);
			HashMap<String, String> filterHm = filter.getBinding();
			if(filterHm != null) {
				temp.putAll(filterHm);
				// add the binding from appended filter to root-filter
				if (temp.size() == (getBinding().size() + filter.getBinding().size())) {
					getBinding().putAll(filter.getBinding());
				} else {
					throw new BindingException(
							"Cannot bind variables with the same name! Change binding 'var' in 'selects.xml'!",
							new Throwable());
				}
			}
			if(filter.getWhereStatement() != null && filter.getWhereStatement().length() > 0) {
				if(getWhereStatement() != null && getWhereStatement().length() < 0) {
					setWhereStatement(getWhereStatement() + " and "
							+ filter.getWhereStatement());
				} else {
					setWhereStatement(filter.getWhereStatement());
				}
			}
			setAppendedFilter(filter);
			System.out.println("Filter appended");

			append(filter.getAppendedFilter());
			System.out.println(getBinding());
			return;
		}
	}

	/**
	 * returns a HasMap which contains the key mapped to an attribute
	 * 
	 * @param ct
	 *            specifies the ClusterType
	 * @param selID
	 *            specifies the select ID
	 * @return HashMap
	 */
	public HashMap<String, String> getBinding(ClusterType ct, String selID) {
		String className = getSelect().getClassName(ct, selID);
		HashMap<String, String> binding = getSelect().getBinding(ct, selID,
				className);
		return binding;
	}

	public String getClassName(ClusterType ct, String selID) {
		String className = getSelect().getClassName(ct, selID);
		return className;
	}

}