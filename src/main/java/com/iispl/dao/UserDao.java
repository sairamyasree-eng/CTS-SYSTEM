//package com.iispl.dao;
//
//import java.util.Optional;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.hibernate.query.Query;
//
//import com.iispl.entity.User;
//import com.iispl.enums.Status;
//import com.iispl.util.HibernateUtil;
//
//public class UserDao extends GenericDao<User, String> {
//
//    // ── Find active user by username ──────────────────────────────────────
//    public Optional<User> findActiveByUsername(String username) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        try {
//            String hql =
//                "FROM User u "
//              + "JOIN FETCH u.role "
//              + "WHERE u.username = :username "
//              + "AND u.status = :status";
//
//            Query<User> query = session.createQuery(hql, User.class);
//            query.setParameter("username", username);
//            query.setParameter("status", Status.Active);
//
//            User user = query.uniqueResult();
//            return Optional.ofNullable(user);
//        } finally {
//            session.close();
//        }
//    }
//
//    // ── Check if username already exists ──────────────────────────────────
//    public boolean existsByUsername(String username) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        try {
//            String hql = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
//            Query<Long> query = session.createQuery(hql, Long.class);
//            query.setParameter("username", username);
//            return query.uniqueResult() > 0;
//        } finally {
//            session.close();
//        }
//    }
//
//    // ── Save new user ─────────────────────────────────────────────────────
//    public boolean save(User user) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            session.persist(user);
//            tx.commit();
//            return true;
//        } catch (Exception e) {
//            if (tx != null) tx.rollback();
//            throw e;
//        } finally {
//            session.close();
//        }
//    }
//}
package com.iispl.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.iispl.entity.User;
import com.iispl.enums.Status;
import com.iispl.util.HibernateUtil;

public class UserDao extends GenericDao<User, String> {

    // ── Find all users (with role eagerly loaded) ─────────────────────────
    public List<User> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.role ORDER BY u.createdDate", User.class).list();
        } finally {
            session.close();
        }
    }

    // ── Find by id ────────────────────────────────────────────────────────
    public User findById(String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .uniqueResult();
        } finally {
            session.close();
        }
    }

    // ── Find active user by username ──────────────────────────────────────
    public Optional<User> findActiveByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<User> query = session.createQuery(
                "FROM User u JOIN FETCH u.role WHERE u.username = :username AND u.status = :status",
                User.class);
            query.setParameter("username", username);
            query.setParameter("status", Status.Active);
            return Optional.ofNullable(query.uniqueResult());
        } finally {
            session.close();
        }
    }

    // ── Check if username already exists ──────────────────────────────────
    public boolean existsByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
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

    // ── Update existing user ──────────────────────────────────────────────
    public boolean update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.merge(user);
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