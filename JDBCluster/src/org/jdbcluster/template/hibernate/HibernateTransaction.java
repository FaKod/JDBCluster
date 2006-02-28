package org.jdbcluster.template.hibernate;

import org.hibernate.Transaction;

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


}
