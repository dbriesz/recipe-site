package com.teamtreehouse.dao;

import com.teamtreehouse.domain.Ingredient;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IngredientDao extends PagingAndSortingRepository<Ingredient, Long> {
}
