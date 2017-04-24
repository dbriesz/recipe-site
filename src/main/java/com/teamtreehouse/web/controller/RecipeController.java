package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    // Index of all recipes
    @SuppressWarnings("unchecked")
    @RequestMapping("/")
    public String listRecipes(Model model) {
        List<Recipe> recipes = recipeService.findAll();

        model.addAttribute("recipes", recipes);
        return "index";
    }
}
