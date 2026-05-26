package com.iispl.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.iispl.entity.Role;
import com.iispl.enums.Status;
import com.iispl.util.HibernateUtil;

public class RoleDao extends GenericDao<Role, String> {

    // ── Find all active roles ─────────────────────────────────────────────
    public List<Role> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                "FROM Role r WHERE r.status = :status ORDER BY r.roleName", Role.class)
                .setParameter("status", Status.Active)
                .list();
        } finally {
            session.close();
        }
    }

    // ── Find by id ────────────────────────────────────────────────────────
    public Role findById(String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Role.class, id);
        } finally {
            session.close();
        }
    }

    // ── Find active role by roleName ──────────────────────────────────────
    public Optional<Role> findByRoleName(String roleName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Role> query = session.createQuery(
                "FROM Role r WHERE r.roleName = :roleName AND r.status = :status", Role.class);
            query.setParameter("roleName", roleName.toUpperCase().trim());
            query.setParameter("status", Status.Active);
            return Optional.ofNullable(query.uniqueResult());
        } finally {
            session.close();
        }
    }
}

//package com.iispl.dao;
//
//import java.util.Optional;
//
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//
//import com.iispl.entity.Role;
//import com.iispl.enums.Status;
//import com.iispl.util.HibernateUtil;
//
//public class RoleDao extends GenericDao<Role, String> {
//
//    // ── Find active role by roleName ──────────────────────────────────────
//    public Optional<Role> findByRoleName(String roleName) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        try {
//            String hql =
//                "FROM Role r "
//              + "WHERE r.roleName = :roleName "
//              + "AND r.status = :status";
//
//            Query<Role> query = session.createQuery(hql, Role.class);
//            query.setParameter("roleName", roleName.toUpperCase().trim());
//            query.setParameter("status", Status.Active);
//
//            Role role = query.uniqueResult();
//            return Optional.ofNullable(role);
//        } finally {
//            session.close();
//        }
//    }
//}