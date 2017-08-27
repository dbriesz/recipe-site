package com.teamtreehouse.service;

import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.domain.User;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    List<Recipe> findByUser(Long id);
    Recipe findById(Long id);
    void save(Recipe recipe);
    boolean delete(Recipe recipe, User user);
    List<Recipe> findByCategoryName(String categoryName);
    List<Recipe> findByDescriptionContaining(String searchTerm);
}
