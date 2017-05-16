package com.teamtreehouse.service;

import com.teamtreehouse.domain.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    void save(User user);
    List<User> findAll();
    User findById(Long id);
}
