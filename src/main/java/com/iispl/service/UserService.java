package com.iispl.service;

import java.util.List;

import com.iispl.entity.User;

public interface UserService {

    List<User> getAllUsers();

    User findById(String id);

    void addUser(String username, String password, String fullName,
                 String branch, String roleId);

    void updateUser(String id, String fullName, String branch,
                    String roleId, String newPassword);

    void toggleStatus(String id);
}