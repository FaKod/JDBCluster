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
package org.jdbcluster.template;

import java.util.List;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.JDBClusterException;


/**
 * 
 * @author Philipp Noggler
 * @author Christopher Schmidt
 * QueryTemplate is an interface which provides methods to execute a Query.
 *
 */
public interface QueryTemplate {
	
	/**
	 * Return the query results as a <tt>List</tt>. If the query contains
	 * multiple results pre row, the results are returned in an instance
	 * of <tt>Object[]</tt>.
	 *
	 * @return the result list
	 * @throws JDBClusterException
	 */
	public List list() throws JDBClusterException;
	
	/**
	 * stores instance of corresponding ClusterType
	 * @param clusterType
	 */
	public void setClusterType(ClusterType clusterType);
}
