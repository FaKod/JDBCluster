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

/**
 * Interface of the session factory 
 * used to create sessions
 * @author Philipp Noggler
 * @author FaKod
 *
 */
public interface SessionFactoryTemplate {

	/**
	 * Opens a new standard Session.
	 * 
	 * @return a new instance of SessionTemplate.
	 */
	public SessionTemplate openSession();
	/**
	 * Opens a new stateless Session. This session can be used for bulk operations. All retrieved objects are not bounded to the session.
	 *  
	 * @return a new instance of StatelessSessionTemplate.
	 */
	public StatelessSessionTemplate openStatelessSession();
	/**
	 * Returns the current session.
	 * 
	 * @return a new instance of SessionTemplate with underlying current Session.
	 */
	public SessionTemplate getSession();
	/**
	 * Returns the native SessionFactory.
	 * 
	 * @param <T> a trick to avoid the necessary cast.
	 * @return the SessionFactory of the underlying 
	 */
	public <T> T getNativeSessionFactory();

}
