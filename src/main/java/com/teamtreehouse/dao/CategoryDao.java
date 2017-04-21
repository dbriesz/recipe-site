package com.teamtreehouse.dao;

import com.teamtreehouse.domain.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryDao extends PagingAndSortingRepository<Category, Long> {
}
