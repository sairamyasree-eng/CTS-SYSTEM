package com.iispl.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * BUG-FIX: Eagerly initialises HibernateUtil when the WAR is deployed.
 *
 * Without this, SessionFactory (and therefore hbm2ddl.auto = update)
 * is only triggered the first time a DAO is called — i.e. after a
 * successful login.  Because login itself was broken (missing apply=
 * attribute in login.zul), HibernateUtil was never loaded and the
 * tables were never created.
 *
 * Registering this listener in web.xml guarantees Hibernate runs DDL
 * (CREATE TABLE IF NOT EXISTS) immediately on startup, before any
 * user interaction.
 */
@WebListener
public class HibernateInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("[CTS] HibernateUtil SessionFactory initialised — tables ready.");
        } catch (Exception e) {
            System.err.println("[CTS] FATAL: SessionFactory failed to initialise: " + e.getMessage());
            throw new RuntimeException("Hibernate init failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
        System.out.println("[CTS] HibernateUtil SessionFactory closed.");
    }
}
