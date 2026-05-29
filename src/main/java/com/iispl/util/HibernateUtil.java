package com.iispl.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.iispl.entity.AuditLog;
import com.iispl.entity.Batch;
import com.iispl.entity.BpxfFile;
import com.iispl.entity.Cheque;
import com.iispl.entity.CtsSession;
import com.iispl.entity.InwardCheque;
import com.iispl.entity.OutwardCheque;
import com.iispl.entity.Role;
import com.iispl.entity.User;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = build();

    private static SessionFactory build() {
        try {
            Configuration cfg = new Configuration();

            // ── Database connection ──────────────────────────────────────────
            cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/finalproject");
            cfg.setProperty("hibernate.connection.username", "postgres");
            cfg.setProperty("hibernate.connection.password", "password");

            // ── Dialect ──────────────────────────────────────────────────────
            cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

            // ── DDL auto ─────────────────────────────────────────────────────
            // "update" → creates/alters tables to match entity mappings; never drops data.
            cfg.setProperty("hibernate.hbm2ddl.auto", "update");

            // ── SQL logging: OFF ─────────────────────────────────────────────
            // Set to "true" only when debugging Hibernate SQL behaviour.
            cfg.setProperty("hibernate.show_sql",   "false");
            cfg.setProperty("hibernate.format_sql", "false");

            // ── BUG-FIX: Register all entity classes ─────────────────────────
            // Without these, Hibernate cannot discover the entities and will
            // throw "Unknown entity" / "Table not found" errors at runtime.
            cfg.addAnnotatedClass(Role.class);
            cfg.addAnnotatedClass(User.class);
            cfg.addAnnotatedClass(CtsSession.class);
            cfg.addAnnotatedClass(Batch.class);
            cfg.addAnnotatedClass(BpxfFile.class);
            cfg.addAnnotatedClass(Cheque.class);
            cfg.addAnnotatedClass(OutwardCheque.class);
            cfg.addAnnotatedClass(InwardCheque.class);
            cfg.addAnnotatedClass(AuditLog.class);

            return cfg.buildSessionFactory();

        } catch (Exception e) {
            System.err.println("✗ SessionFactory creation failed: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    /** Call once on application shutdown to release DB connection pool resources. */
    public static void shutdown() {
        SESSION_FACTORY.close();
    }
}
