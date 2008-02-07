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

import org.jdbcluster.clustertype.ClusterType;

/**
 * class CCFilterImpl implements functionality to get bindings from XML file, to
 * get a HQL String from XML file and to append a filter.
 * 
 * @author Philipp Noggler
 * @author FaKod
 * @author Tobi
 */

public class CCFilterImpl extends CCFilterBase {

	public CCFilterImpl() {
	}

	protected CCFilterImpl(ClusterType ct) {
		super(ct);
	}

	/**
	 * calls getWhere in ClusterSelectImpl and returns a String to generate a
	 * query
	 * 
	 * @param clusterType specifies the ClusterType
	 * @param selectID specifies the select ID
	 */
	public String getSelectString(String selectID) {
		String hql = getSelect().getWhere(getClusterType(), selectID);
		return hql;
	}

	public String getClassName(ClusterType ct, String selID) {
		String className = getSelect().getClassName(ct, selID);
		return className;
	}

}