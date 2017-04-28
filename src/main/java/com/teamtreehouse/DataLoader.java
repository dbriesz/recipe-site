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
import java.util.Collections;
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
//        int prepTime = 1;
//        int cookTime = 1;
//        int quantity = 1;
        boolean value = false;
        List<Ingredient> ingredients = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            categoryService.save(category);

            Recipe recipe = new Recipe();
            recipe.setCategory(category);
            Ingredient ingredient = new Ingredient();
            ingredient.setRecipe(recipeService.findById((long) i));
            ingredient.setMeasurement("TestMeasurement " + i);
            ingredient.setName("TestName " + i);
            ingredient.setQuantity(i);
            ingredientService.save(ingredient);
            ingredients.add(ingredient);
            recipe.setIngredients(Collections.singletonList(
                    ingredientService.findById((long) i))
            );

            Instruction instruction = new Instruction();
            instruction.setRecipe(recipeService.findById((long) i));
            instruction.setDescription("TestDescription " + i);
            instruction.setCookingInstruction("TestCookingInstruction " + i);
            instructionService.save(instruction);
            instructions.add(instruction);
            recipe.setInstructions(Collections.singletonList(
                    instructionService.findById((long) i))
            );

            recipe.setName("TestName " + i);
            recipe.setDescription("TestDesc " + i);

            recipe.setImageUrl("TestUrl" + i);

            recipe.setPrepTime(i + " minutes");
            recipe.setCookTime(i + " hours");
            recipe.setFavorite(value);
//            prepTime++;
//            cookTime++;
            value = !value;
//            quantity++;
            recipeService.save(recipe);
        }


    }
}
