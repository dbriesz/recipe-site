package com.teamtreehouse.service;

import com.teamtreehouse.domain.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> findAll();
    Ingredient findById(Long id);
    void save(Ingredient ingredient);
    void delete(Ingredient ingredient);
}
