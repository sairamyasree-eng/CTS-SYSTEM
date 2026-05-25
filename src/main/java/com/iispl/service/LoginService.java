package com.iispl.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.iispl.dao.UserDao;
import com.iispl.dto.SessionUserDTO;
import com.iispl.entity.User;
import com.iispl.exceptions.AuthException;
import com.iispl.util.HibernateUtil;
import com.iispl.util.PasswordUtil;

public class LoginService {

    private final UserDao userDao;

    public LoginService() {
        this.userDao = new UserDao();
    }

    // ── Original 3-param (role check included) — kept intact ─────────────
    public SessionUserDTO authenticate(String username, String plainPassword, String selectedRole) {
        if (isBlank(username) || isBlank(plainPassword) || isBlank(selectedRole)) {
            throw new AuthException("All fields are mandatory");
        }

        User user = resolveUser(username, plainPassword);

        String dbRoleName = (user.getRole() != null) ? user.getRole().getRoleName() : "";
        if (!dbRoleName.equalsIgnoreCase(selectedRole.trim())) {
            throw new AuthException("Selected role does not match your assigned role.");
        }

        updateLastLogin(user);
        return buildSessionDTO(user);
    }

    // ── 2-param overload (auto-detect role, no role check) ────────────────
    public SessionUserDTO authenticate(String username, String plainPassword) {
        if (isBlank(username) || isBlank(plainPassword)) {
            throw new AuthException("Username and password are mandatory");
        }

        User user = resolveUser(username, plainPassword);
        updateLastLogin(user);
        return buildSessionDTO(user);
    }

    // ── Shared: fetch + verify user ───────────────────────────────────────
    private User resolveUser(String username, String plainPassword) {
        Optional<User> opt = userDao.findActiveByUsername(username.trim());

        if (opt.isEmpty()) {
            throw new AuthException("Invalid username or password");
        }

        User user = opt.get();

        if (!PasswordUtil.hash(plainPassword).equals(user.getPasswordHash())) {
            throw new AuthException("Invalid username or password");
        }

        return user;
    }

    // ── Shared: update lastLoginAt ────────────────────────────────────────
    private void updateLastLogin(User user) {
        try (Session dbSession = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = dbSession.beginTransaction();
            User managed = dbSession.get(User.class, user.getId());
            if (managed != null) {
                managed.setLastLoginAt(LocalDateTime.now());
            }
            tx.commit();
        }
    }

    // ── Shared: build SessionUserDTO ──────────────────────────────────────
    private SessionUserDTO buildSessionDTO(User user) {
        return new SessionUserDTO(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getBranch(),
            user.getRole().getId(),
            user.getRole().getRoleName(),
            toDisplayName(user.getRole().getRoleName())
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String toDisplayName(String roleName) {
        if (roleName == null) return "";
        switch (roleName.toUpperCase()) {
            case "ADMIN":           return "Admin";
            case "MAKER_OUTWARD":   return "Maker Outward";
            case "CHECKER_OUTWARD": return "Checker Outward";
            case "MAKER_INWARD":    return "Maker Inward";
            case "CHECKER_INWARD":  return "Checker Inward";
            default:                return roleName;
        }
    }
}