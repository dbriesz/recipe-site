package com.teamtreehouse.dao;

import com.teamtreehouse.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface UserDao extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
}
