package com.teamtreehouse.dao;

import com.teamtreehouse.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends PagingAndSortingRepository<User, Long> {

}
