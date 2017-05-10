package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.*;
import com.teamtreehouse.service.*;
import com.teamtreehouse.web.FlashMessage;
import com.teamtreehouse.web.exceptions.CategoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.teamtreehouse.web.FlashMessage.Status.FAILURE;
import static com.teamtreehouse.web.FlashMessage.Status.SUCCESS;

@Controller
public class RecipeController {
    final Logger logger = LoggerFactory.getLogger(RecipeController.class);

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
    public String formNewRecipe(Model model, HttpServletRequest request) {
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
        model.addAttribute("cancel-redirect", String.format("redirect:%s", request.getHeader("referer")));

        return "edit";
    }

    // Edit an existing recipe
    @RequestMapping("recipes/{recipeId}/edit")
    public String editRecipe(@PathVariable Long recipeId, Model model, HttpServletRequest request) {
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
        model.addAttribute("cancel-redirect", String.format("redirect:%s", request.getHeader("referer")));

        return "edit";
    }

    // Add a recipe
    @RequestMapping(value = "recipes/add", method = RequestMethod.POST)
    public String addRecipe(@Valid Recipe recipe, BindingResult result, RedirectAttributes redirectAttributes) {
        // Add recipe if valid data was received
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Invalid input. Ingredient quantity must be a number. Please try again.", FlashMessage.Status.FAILURE));
            return "redirect:/recipes/add";
        } else {
            recipe.getIngredients().forEach(ingredient -> ingredientService.save(ingredient));
            recipe.getInstructions().forEach(instruction -> instructionService.save(instruction));
            recipeService.save(recipe);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("New recipe added!", FlashMessage.Status.SUCCESS));
        }

        // Redirect browser to home page
        return "redirect:/";
    }

    // Update an existing recipe
    @RequestMapping(value = "recipes/{recipeId}/edit", method = RequestMethod.POST)
    public String updateRecipe(@Valid Recipe recipe, BindingResult result, RedirectAttributes redirectAttributes) {
        // Update recipe if valid data was received
        Category category = recipe.getCategory();
        if (category != null) {
            recipe.setCategory(category);
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Invalid input. Ingredient quantity must be a number. Please try again.", FlashMessage.Status.FAILURE));
            return String.format("redirect:/recipes/%s/edit", recipe.getId());
        } else {
            recipe.getIngredients().forEach(ingredient -> ingredientService.save(ingredient));
            recipe.getInstructions().forEach(instruction -> instructionService.save(instruction));
            recipeService.save(recipe);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe updated!", FlashMessage.Status.SUCCESS));

            // Redirect browser to recipe detail page
            return String.format("redirect:/recipes/%s", recipe.getId());
        }
    }

    // Delete an existing recipe
    @RequestMapping(value = "recipes/{recipeId}/delete", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable Long recipeId, RedirectAttributes redirectAttributes) {
        // Delete recipe whose id is recipeId
        Recipe recipe = recipeService.findById(recipeId);

        recipeService.delete(recipe);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe deleted!", FlashMessage.Status.SUCCESS));

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

    @ExceptionHandler(value = NumberFormatException.class)
    public String nfeHandler(HttpServletRequest request, NumberFormatException ex) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put("flash", new FlashMessage(ex.getMessage(), FAILURE));
        return "redirect:" + request.getHeader("referer");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(Model model, Exception ex) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
