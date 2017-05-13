package com.teamtreehouse.service;

import com.teamtreehouse.dao.RecipeDao;
import com.teamtreehouse.domain.Category;
import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.domain.User;
import com.teamtreehouse.web.exceptions.CategoryNotFoundException;
import com.teamtreehouse.web.exceptions.SearchTermNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private UserService userService;

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
        List<User> users = userService.findAll();
        users.forEach(user -> {
            user.removeFavorite(recipe);
            userService.save(user);
        });
        recipeDao.delete(recipe);
    }

    @Override
    public List<Recipe> findByCategoryName(String categoryName) {
        List<Recipe> recipes = recipeDao.findByCategoryName(categoryName);
        if (recipes.isEmpty()) {
            throw new CategoryNotFoundException();
        } else {
            return recipes;
        }
    }

    @Override
    public List<Recipe> findByDescriptionContaining(String searchTerm) {
        List<Recipe> recipes = recipeDao.findByDescriptionContaining(searchTerm);
        if (recipes.isEmpty()) {
            throw new SearchTermNotFoundException();
        } else {
            return recipes;
        }
    }


}
