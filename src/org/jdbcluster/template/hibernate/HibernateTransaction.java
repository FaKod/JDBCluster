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
package org.jdbcluster.template.hibernate;

import org.hibernate.Transaction;

/**
 * 
 * @author Philipp Noggler
 * @author FaKod
 */
public class HibernateTransaction implements org.jdbcluster.template.TransactionTemplate {

	private Transaction tx;
	
	public void commit() {
		tx.commit();
	}
	public void setTransaction(Transaction transaction) {
		tx = transaction;	
	}
	
	public Transaction getTransaction() {
		return tx;
	}
	
	public void rollback() {
		tx.rollback();
	}
	
	public void begin() {
		tx.begin();
	}
}
