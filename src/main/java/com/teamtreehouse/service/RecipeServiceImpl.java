package com.teamtreehouse.service;

import com.teamtreehouse.dao.RecipeDao;
import com.teamtreehouse.domain.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeDao recipeDao;

    @Override
    public List<Recipe> findAll() {
        return (List<Recipe>) recipeDao.findAll();
    }

    @Override
    public Recipe findById(Long id) {
        return recipeDao.findOne(id);
    }

    @Override
    public void save(Recipe recipe) {
        recipeDao.save(recipe);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeDao.delete(recipe);
    }

    @Override
    public void toggleFavorite(Recipe recipe) {
        recipe.setFavorite(!recipe.isFavorite());
        recipeDao.save(recipe);
    }
}
