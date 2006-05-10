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
package org.jdbcluster.metapersistence.cluster;

import org.jdbcluster.metapersistence.annotation.DaoLink;

/**
 * aspect to prevent new(...)
 */
public aspect ClusterUsage {
	pointcut newCluster(): 
		call(Cluster+.new(..)) && 
		!withincode(* ClusterFactory.*(..));
	
	pointcut clusterWithoutDAOAnno(): 
		execution(* *(..)) && 
		within(!@DaoLink Cluster+);

	declare error: newCluster(): 
		"please use static factory method ClusterFactory.newInstance(ClusterType)";
	
	declare error: clusterWithoutDAOAnno(): 
		"please use @DaoLink Annotation for Cluster Objects";
}
