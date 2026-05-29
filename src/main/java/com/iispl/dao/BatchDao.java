package com.iispl.dao;

import org.hibernate.Session;

import com.iispl.entity.Batch;
import com.iispl.util.HibernateUtil;

public class BatchDao extends GenericDao<Batch, Long> {

	// Fetch Batch by its String ID (e.g. "B358062")
	// Used by parseAndPersist to set the Batch FK on InwardCheque
	public Batch findById(String batchId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(Batch.class, batchId);
		}
	}
}
