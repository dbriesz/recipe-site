package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Recipe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private SessionFactory sessionFactory;

    // Index of all recipes
    @SuppressWarnings("unchecked")
    @RequestMapping("/")
    public String listRecipes(Model model) {
        Session session = sessionFactory.openSession();
        List<Recipe> recipes = session.createCriteria(Recipe.class).list();

        model.addAttribute("recipes", recipes);
        return "index";
    }
}
