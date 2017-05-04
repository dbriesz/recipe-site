package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Category;
import com.teamtreehouse.domain.Ingredient;
import com.teamtreehouse.domain.Instruction;
import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.service.CategoryService;
import com.teamtreehouse.service.IngredientService;
import com.teamtreehouse.service.InstructionService;
import com.teamtreehouse.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private InstructionService instructionService;

    // Home page - index of all recipes
    @SuppressWarnings("unchecked")
    @RequestMapping("/")
    public String listRecipes(Model model) {
        List<Recipe> recipes = recipeService.findAll();
        List<Category> categories = categoryService.findAll();

        model.addAttribute("recipes", recipes);
        model.addAttribute("action", "/recipes/add");
        model.addAttribute("categories", categories);

        return "index";
    }

    // List of recipes based on selected category
    @RequestMapping("/category")
    public String category(@RequestParam String category, Model model) {
        List<Category> categories = categoryService.findAll();
        List<Recipe> recipes;
        if (category.equals("")) {
            recipes = recipeService.findAll();
        } else {
            recipes = recipeService.findByCategoryName(category);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("recipes", recipes);
        model.addAttribute("action", "/category");

        return "index";
    }

    // Single recipe page
    @RequestMapping("recipes/{recipeId}")
    public String recipe(@PathVariable Long recipeId, Model model) {
        // Get the recipe given by recipeId
        Recipe recipe = recipeService.findById(recipeId);

        model.addAttribute("recipe", recipe);

        return "detail";
    }

    // Form for adding a new recipe
    @RequestMapping("recipes/add")
    public String formNewRecipe(Model model) {
        // Add model attributes needed for new form
        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", new Recipe());
        }
        model.addAttribute("action", "/recipes/add");
        List<Category> categories = categoryService.findAll();
        model.addAttribute("heading", "New Recipe");
        model.addAttribute("categories", categories);
        List<Ingredient> ingredients = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("instructions", instructions);

        return "edit";
    }

    // Edit an existing recipe
    @RequestMapping("recipes/{recipeId}/edit")
    public String editRecipe(@PathVariable Long recipeId, Model model) {
        // Add model attributes needed for edit form
        Recipe recipe = recipeService.findById(recipeId);
        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", recipe);
        }
        model.addAttribute("action", String.format("/recipes/%s/edit", recipeId));
        model.addAttribute("heading", "Recipe Editor");
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("instructions", instructionService.findAll());

        return "edit";
    }

    // Add a recipe
    @RequestMapping(value = "recipes/add", method = RequestMethod.POST)
    public String addRecipe(@Valid Recipe recipe, BindingResult result) {
        // Add recipe if valid data was received
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        } else {
            recipe.getIngredients().forEach(ingredient -> ingredientService.save(ingredient));
            recipe.getInstructions().forEach(instruction -> instructionService.save(instruction));
            recipeService.save(recipe);
        }

        // Redirect browser to home page
        return "redirect:/";
    }

    // Update an existing recipe
    @RequestMapping(value = "recipes/{recipeId}/edit", method = RequestMethod.POST)
    public String updateRecipe(@Valid Recipe recipe) {
        // Update recipe if valid data was received
        Category category = recipe.getCategory();
        if (category != null) {
            recipe.setCategory(category);
        }
        recipe.getIngredients().forEach(ingredient -> ingredientService.save(ingredient));
        recipe.getInstructions().forEach(instruction -> instructionService.save(instruction));
        recipeService.save(recipe);

        // Redirect browser to recipe detail page
        return String.format("redirect:/recipes/%s", recipe.getId());
    }

    // Delete an existing recipe
    @RequestMapping(value = "recipes/{recipeId}/delete", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable Long recipeId) {
        // Delete recipe whose id is recipeId
        Recipe recipe = recipeService.findById(recipeId);
        recipeService.delete(recipe);

        // Redirect browser to home page
        return "redirect:/";
    }

    // Mark/unmark an existing recipe as a favorite
    @RequestMapping(value = "recipes/{recipeId}/favorite", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long recipeId, HttpServletRequest request) {
        Recipe recipe = recipeService.findById(recipeId);
        recipeService.toggleFavorite(recipe);

        return String.format("redirect:%s", request.getHeader("referer"));
    }
}
