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
package org.jdbcluster.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper class for DomainChecker mainly for special nullAllowed value
 * 
 * @author FaKod
 * 
 */
public final class ValidDomainEntries<T> extends ArrayList<T> {

	private boolean nullAllowed = false;

	/**
	 * prefilles entries a list containing the elements of the specified 
	 * collection, in the order they are returned by the collection's iterator.
	 * 
	 * @param c valid list elements (you can use null element)
	 * @throws NullPointerException if the specified collection is null.
	 */
	public ValidDomainEntries(Collection<? extends T> c) {
		super(c);
		if (this.contains(null)) {
			setNullAllowed(true);
			this.remove(null);
		}
	}

	/**
	 * prefilles entries with list elememts
	 * 
	 * @param list valid list elements
	 * @param nullAllowed overwrites existing null element in list parameter
	 */
	public ValidDomainEntries(List<T> list, boolean nullAllowed) {
		this(list);
		setNullAllowed(nullAllowed);
	}

	/**
	 * prefilles entries with list elememts
	 * 
	 * @param arg valid list elements(you can use null element)
	 */
	public ValidDomainEntries(T... arg) {
		for (T o : arg) {
			if (o != null)
				this.add(o);
			else
				setNullAllowed(true);
		}
	}

	/**
	 * prefilles entries with list elememts
	 * 
	 * @param nullAllowed overwrites existing null element in list parameter
	 * @param arg valid list elements
	 */
	public ValidDomainEntries(boolean nullAllowed, T... arg) {
		this(arg);
		setNullAllowed(nullAllowed);
	}

	/**
	 * creates empty object
	 * 
	 */
	public ValidDomainEntries() {
	}

	/**
	 * is null value a allowed value
	 * 
	 * @return true if domain includes null value
	 */
	public boolean isNullAllowed() {
		return nullAllowed;
	}

	/**
	 * sets allow null value
	 * 
	 * @param nullAllowed true if domain includes null value
	 */
	public void setNullAllowed(boolean nullAllowed) {
		this.nullAllowed = nullAllowed;
	}

}
