package com.teamtreehouse.service;

import com.teamtreehouse.domain.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    Recipe findById(Long id);
    void save(Recipe recipe);
    void delete(Recipe recipe);
}
