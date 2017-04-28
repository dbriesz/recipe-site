package com.teamtreehouse;

import com.teamtreehouse.domain.Category;
import com.teamtreehouse.domain.Ingredient;
import com.teamtreehouse.domain.Instruction;
import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private InstructionService instructionService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    private List<Recipe> recipes = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int prepTime = 1;
        int cookTime = 1;
        int quantity = 1;
        boolean value = false;

        for (int i = 1; i <= 5; i++) {
            Category category = new Category();
            category.setName("Category " + 1);
            categoryService.save(category);

            Recipe recipe = new Recipe();
            Ingredient ingredient = new Ingredient();
            ingredient.setRecipe(recipe);
            ingredient.setMeasurement("TestMeasurement " + 1);
            ingredient.setName("TestName " + 1);
            ingredient.setQuantity(quantity);
            ingredientService.save(ingredient);

            Instruction instruction = new Instruction();
            instruction.setRecipe(recipe);
            instruction.setDescription("TestDescription " + 1);
            instruction.setCookingInstruction("TestCookingInstruction " + 1);
            instructionService.save(instruction);

            recipe.setName("TestName " + 1);
            recipe.setDescription("TestDesc " + 1);
            recipe.setCategory(category);
            recipe.setImageUrl("TestUrl" + 1);
            recipe.addIngredient(ingredient);
            recipe.addInstruction(instruction);
            recipe.setPrepTime(prepTime + " minutes");
            recipe.setCookTime(cookTime + " hours");
            recipe.setFavorite(value);
            prepTime++;
            cookTime++;
            value = !value;
            quantity++;
            recipeService.save(recipe);
        }


    }
}
