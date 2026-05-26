package com.iispl.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.iispl.dao.RoleDao;
import com.iispl.dao.UserDao;
import com.iispl.entity.Role;
import com.iispl.entity.User;
import com.iispl.enums.Status;
import com.iispl.util.PasswordUtil;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDao();
    private final RoleDao roleDao = new RoleDao();

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User findById(String id) {
        return userDao.findById(id);
    }

    public void addUser(String username, String password, String fullName,
                        String branch, String roleId) {
        if (userDao.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' already exists.");
        }
        Role role = roleDao.findById(roleId);
        if (role == null) throw new IllegalArgumentException("Selected role not found.");

        User user = new User(
            username.trim(),
            PasswordUtil.hash(password),
            fullName.trim(),
            branch.trim(),
            Status.Active,
            LocalDate.now(),
            null
        );
        user.setRole(role);
        userDao.save(user);
    }

    public void updateUser(String id, String fullName, String branch,
                           String roleId, String newPassword) {
        User user = userDao.findById(id);
        if (user == null) throw new IllegalArgumentException("User not found.");

        Role role = roleDao.findById(roleId);
        if (role == null) throw new IllegalArgumentException("Selected role not found.");

        user.setFullName(fullName.trim());
        user.setBranch(branch.trim());
        user.setRole(role);
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPasswordHash(PasswordUtil.hash(newPassword));
        }
        userDao.update(user);
    }

    public void toggleStatus(String id) {
        User user = userDao.findById(id);
        if (user == null) throw new IllegalArgumentException("User not found.");
        user.setStatus(user.getStatus() == Status.Active ? Status.Inactive : Status.Active);
        userDao.update(user);
    }
}