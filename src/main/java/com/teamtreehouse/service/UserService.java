package com.teamtreehouse.service;

import com.teamtreehouse.domain.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    void save(User user);
    void delete(User user);
}
