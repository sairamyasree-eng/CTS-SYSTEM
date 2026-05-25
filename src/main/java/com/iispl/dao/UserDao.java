package com.iispl.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.iispl.entity.User;
import com.iispl.enums.Status;
import com.iispl.util.HibernateUtil;

public class UserDao extends GenericDao<User, String> {

    // ── Find active user by username ──────────────────────────────────────
    public Optional<User> findActiveByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql =
                "FROM User u "
              + "JOIN FETCH u.role "
              + "WHERE u.username = :username "
              + "AND u.status = :status";

            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            query.setParameter("status", Status.Active);

            User user = query.uniqueResult();
            return Optional.ofNullable(user);
        } finally {
            session.close();
        }
    }

    // ── Check if username already exists ──────────────────────────────────
    public boolean existsByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("username", username);
            return query.uniqueResult() > 0;
        } finally {
            session.close();
        }
    }

    // ── Save new user ─────────────────────────────────────────────────────
    public boolean save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}