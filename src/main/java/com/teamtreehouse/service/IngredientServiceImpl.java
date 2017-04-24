package com.teamtreehouse.service;

import com.teamtreehouse.dao.IngredientDao;
import com.teamtreehouse.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientDao ingredientDao;

    @Override
    public List<Ingredient> findAll() {
        return (List<Ingredient>) ingredientDao.findAll();
    }

    @Override
    public Ingredient findById(Long id) {
        return ingredientDao.findOne(id);
    }

    @Override
    public void save(Ingredient ingredient) {
        ingredientDao.save(ingredient);
    }

    @Override
    public void delete(Ingredient ingredient) {
        ingredientDao.delete(ingredient);
    }
}
