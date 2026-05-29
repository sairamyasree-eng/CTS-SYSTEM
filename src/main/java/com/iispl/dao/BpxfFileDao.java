package com.iispl.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.iispl.entity.BpxfFile;
import com.iispl.util.HibernateUtil;

public class BpxfFileDao {

	// Persist a new BpxfFile record (called by WatchService when new file detected)
	public void insert(BpxfFile file) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			session.persist(file);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuntimeException("BpxfFileDao.insert failed", e);
		}
	}

	// Called by scanWatchFolder() — returns all bpxf_file records
	public List<BpxfFile> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM BpxfFile ORDER BY id DESC", BpxfFile.class).list();
		}
	}

	// Called after successful parse — mark file as PARSED
	public void updateStatusParsed(Long id) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			BpxfFile file = session.get(BpxfFile.class, id);
			if (file != null) {
				file.setStatus("PARSED");
				file.setParsedAt(LocalDateTime.now());
				session.merge(file);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuntimeException("BpxfFileDao.updateStatusParsed failed", e);
		}
	}

	// Called when parse fails — mark file as FAILED
	public void updateStatusFailed(Long id) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			BpxfFile file = session.get(BpxfFile.class, id);
			if (file != null) {
				file.setStatus("FAILED");
				session.merge(file);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new RuntimeException("BpxfFileDao.updateStatusFailed failed", e);
		}

	}

	// Check if a bpxf_file record already exists for a given batchNo
	// Used by WatchService to avoid duplicate inserts
	public boolean existsByBatchNo(String batchNo) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Long count = session.createQuery("SELECT COUNT(b) FROM BpxfFile b WHERE b.batchNo = :batchNo", Long.class)
					.setParameter("batchNo", batchNo).uniqueResult();
			return count != null && count > 0;

		}
	}
	
	public BpxfFile findByFileName(String fileName) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        return session.createQuery(
	                "FROM BpxfFile b WHERE b.fileName = :fileName",
	                BpxfFile.class)
	            .setParameter("fileName", fileName)
	            .uniqueResult();
	    }
	}

}
