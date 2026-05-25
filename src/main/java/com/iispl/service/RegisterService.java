package com.iispl.service;

import java.time.LocalDate;
import java.util.Optional;

import com.iispl.dao.RoleDao;
import com.iispl.dao.UserDao;
import com.iispl.entity.Role;
import com.iispl.entity.User;
import com.iispl.enums.Status;
import com.iispl.exceptions.AuthException;
import com.iispl.util.PasswordUtil;

public class RegisterService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public RegisterService() {
        this.userDao = new UserDao();
        this.roleDao = new RoleDao();
    }

    public void register(String username, String plainPassword,
                         String confirmPassword, String roleName) {

        if (isBlank(username) || isBlank(plainPassword)
                || isBlank(confirmPassword) || isBlank(roleName)) {
            throw new AuthException("All fields are mandatory");
        }

        if (!plainPassword.equals(confirmPassword)) {
            throw new AuthException("Passwords do not match");
        }

        if (userDao.existsByUsername(username.trim())) {
            throw new AuthException("Username already exists");
        }

        Optional<Role> roleOpt = roleDao.findByRoleName(roleName);
        if (roleOpt.isEmpty()) {
            throw new AuthException("Selected role is invalid or inactive");
        }

        User user = new User(
            username.trim(),
            PasswordUtil.hash(plainPassword),
            null,           // fullName
            null,           // branch
            Status.Active,
            LocalDate.now(),
            null            // lastLoginAt
        );
        user.setRole(roleOpt.get());  // set via setter

        userDao.save(user);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}