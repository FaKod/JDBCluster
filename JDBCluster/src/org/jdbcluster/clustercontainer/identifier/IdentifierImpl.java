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
package org.jdbcluster.clustercontainer.identifier;

import java.util.Vector;

public class IdentifierImpl implements Identifier {

	private Vector<Long> identifiers;
	
	public void setFirstIdAttribute(Long value) {
		identifiers.add(value);	
	}

	public Vector getIdVector() {
		return identifiers;
	}

	public boolean isNull() {
		return identifiers.isEmpty();
	}

	public boolean equals(Object o) {
		return this.equals(o);
	}

	public int hashCode() {
		return identifiers.hashCode();
	}
	
}
