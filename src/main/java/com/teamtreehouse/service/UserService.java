package com.teamtreehouse.service;

import com.teamtreehouse.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    void save(User user);
    List<User> findAll();
}
