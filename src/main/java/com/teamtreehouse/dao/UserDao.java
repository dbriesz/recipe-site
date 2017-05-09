package com.teamtreehouse.dao;

import com.teamtreehouse.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
}
