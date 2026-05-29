package com.iispl.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.iispl.entity.InwardCheque;
import com.iispl.util.HibernateUtil;

public class InwardChequeDao extends GenericDao<InwardCheque, Long> {

	// Persists a single InwardCheque row
	public void insert(InwardCheque cheque) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			session.persist(cheque);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuntimeException("InwardChequeDao.insert failed", e);
		}
	}

	// Batch insert — persists all cheques in a single transaction
	public void insertAll(List<InwardCheque> cheques) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			for (int i = 0; i < cheques.size(); i++) {

				// flush and clear every 20 to avoid memory pressure
				if (i % 20 == 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuntimeException("InwardChequeDao.insertAll failed", e);
		}
	}
}
