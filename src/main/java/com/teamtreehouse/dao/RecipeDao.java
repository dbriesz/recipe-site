package com.teamtreehouse.dao;

import com.teamtreehouse.domain.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipeDao extends PagingAndSortingRepository<Recipe, Long> {
}
