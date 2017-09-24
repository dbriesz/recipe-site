package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.*;
import com.teamtreehouse.domain.User;
import com.teamtreehouse.service.*;
import com.teamtreehouse.web.FlashMessage;
import com.teamtreehouse.web.exceptions.CategoryNotFoundException;
import com.teamtreehouse.web.exceptions.SearchTermNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
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
    @Autowired
    private UserService userService;

    // Home page - index of all recipes
    @SuppressWarnings("unchecked")
    @RequestMapping("/")
    public String listRecipes(Model model, Principal principal) {
        List<Recipe> recipes = recipeService.findAll();
        List<Category> categories = categoryService.findAll();

        model.addAttribute("recipes", recipes);
        model.addAttribute("action", "/recipes/add");
        model.addAttribute("categories", categories);
        addCurrentLoggedInUserToModel(model);

        return "index";
    }

    private void addCurrentLoggedInUserToModel(Model model) {
        org.springframework.security.core.userdetails.User principal = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken)) {
            Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (o != null) {
                principal = (org.springframework.security.core.userdetails.User) o;
            }

            if (principal != null) {
                User foundUser = userService.findByUsername(principal.getUsername());
                model.addAttribute("currentUser", foundUser);
                String name = foundUser.getUsername(); //get logged in username
                model.addAttribute("username", name);
            }
        } else {
            throw new AccessDeniedException("Please log in or create an account");
        }
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
        addCurrentLoggedInUserToModel(model);

        return "index";
    }

    // List of recipes based on search term
    @RequestMapping("/search")
    public String search(@RequestParam String searchTerm, Model model) {
        List<Category> categories = categoryService.findAll();
        List<Recipe> recipes;
        if (searchTerm.equals("")) {
            recipes = recipeService.findAll();
        } else {
            recipes = recipeService.findByDescriptionContaining(searchTerm);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("recipes", recipes);
        model.addAttribute("action", "/search");
        model.addAttribute("searchTerm", searchTerm);
        addCurrentLoggedInUserToModel(model);

        return "index";
    }

    // Single recipe page
    @RequestMapping("recipes/{recipeId}")
    public String recipe(@PathVariable Long recipeId, Model model) {
        // Get the recipe given by recipeId
        Recipe recipe = recipeService.findById(recipeId);

        model.addAttribute("recipe", recipe);
        addCurrentLoggedInUserToModel(model);

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
        model.addAttribute("cancelRedirect", String.format("%s", request.getHeader("referer")));
        addCurrentLoggedInUserToModel(model);

        return "edit";
    }

    // Edit an existing recipe
    @RequestMapping("recipes/{recipeId}/edit")
    public String editRecipe(@PathVariable Long recipeId, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        // Add model attributes needed for edit form
        Recipe recipe = recipeService.findById(recipeId);
        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", recipe);
        }
        if (getCurrentLoggedInUser().getUsername().equals(recipe.getUser().getUsername())) {
            model.addAttribute("action", String.format("/recipes/%s/edit", recipeId));
            model.addAttribute("heading", "Recipe Editor");
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("instructions", instructionService.findAll());
            model.addAttribute("cancelRedirect", String.format("%s", request.getHeader("referer")));
            addCurrentLoggedInUserToModel(model);

            return "edit";
        } else {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Sorry, you can only edit recipes that you created", FlashMessage.Status.FAILURE));
            return String.format("redirect:%s", request.getHeader("referer"));
        }
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
            Category category = recipe.getCategory();
            if (category != null) {
                recipe.setCategory(category);
            }
            User user = getCurrentLoggedInUser();
            recipe.getIngredients().forEach(ingredient -> ingredientService.save(ingredient));
            recipe.getInstructions().forEach(instruction -> instructionService.save(instruction));
            recipe.setUser(user);
            recipeService.save(recipe);
            user.addCreatedRecipe(recipe);
            userService.save(user);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("New recipe added!", SUCCESS));
        }

        // Redirect browser to home page
        return "redirect:/";
    }

    private User getCurrentLoggedInUser() {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) o;
        return userService.findByUsername(principal.getUsername());
    }

    // Update an existing recipe
    @RequestMapping(value = "recipes/{recipeId}/edit", method = RequestMethod.POST)
    public String updateRecipe(@Valid Recipe recipe, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        // Update recipe if valid data was received
        Category category = recipe.getCategory();
        if (category != null) {
            recipe.setCategory(category);
        }
/*        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Invalid input. Please try again.", FlashMessage.Status.FAILURE));
            return String.format("redirect:%s", request.getHeader("referer"));
        } else {*/
            User currentUser = getCurrentLoggedInUser();
            recipe.getIngredients().forEach(ingredient -> ingredientService.save(ingredient));
            recipe.getInstructions().forEach(instruction -> instructionService.save(instruction));
            recipe.setUser(currentUser);
            recipeService.save(recipe);
            userService.save(currentUser);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe updated successfully!", SUCCESS));

            // Redirect browser to recipe detail page
            return String.format("redirect:/recipes/%s", recipe.getId());
//        }
    }

    // Delete an existing recipe
    @RequestMapping(value = "recipes/{recipeId}/delete", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable Long recipeId, RedirectAttributes redirectAttributes) {
        // Delete recipe whose id is recipeId
        Recipe recipe = recipeService.findById(recipeId);
        if (getCurrentLoggedInUser().getUsername().equals(recipe.getUser().getUsername())) {
            recipeService.delete(recipe);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe deleted successfully!", SUCCESS));
        } else {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Sorry, you can only delete recipes that you created", FAILURE));
        }

        // Redirect browser to home page
        return "redirect:/";
    }

    // Mark/unmark an existing recipe as a favorite
    @RequestMapping(value = "recipes/{recipeId}/favorite", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long recipeId, HttpServletRequest request,
                                 RedirectAttributes redirectAttributes, Model model) {
        Recipe recipe = recipeService.findById(recipeId);
        User user = getCurrentLoggedInUser();
        addCurrentLoggedInUserToModel(model);
        if (recipe.isFavorited(user)) {
            user.removeFavorite(recipe);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe removed from favorites!", SUCCESS));
        } else {
            user.addFavorite(recipe);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe added to favorites!", SUCCESS));
        }
        userService.save(user);

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
    public String categoryNotFound(Model model, Exception ex) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(SearchTermNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String searchTermNotFound(Model model, Exception ex) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public String adeHandler(HttpServletRequest request, AccessDeniedException ex) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put("flash", new FlashMessage(ex.getMessage(), FAILURE));
        return "redirect:/login";
    }
}
