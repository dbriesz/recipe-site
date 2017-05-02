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
            Ingredient ingredient2 = new Ingredient();

            ingredient.setRecipe(recipeService.findById((long) i));
            ingredient2.setRecipe(recipeService.findById((long) i));

            ingredient.setMeasurement("TestMeasurement " + i);
            ingredient2.setMeasurement("TestMeasurement " + i);

            ingredient.setName("TestIngredient " + i);
            ingredient2.setName("AnotherTestIngredient " + i);

            ingredient.setQuantity(i);
            ingredient2.setQuantity(i);

            ingredientService.save(ingredient);
            ingredientService.save(ingredient2);

            ingredients.add(ingredient);
            ingredients.add(ingredient2);

            recipe.addIngredient(ingredient);
            recipe.addIngredient(ingredient2);

/*            recipe.setIngredients(Collections.singletonList(
                    ingredientService.findById((long) i))
            );*/

            Instruction instruction = new Instruction();
            Instruction instruction2 = new Instruction();

            instruction.setRecipe(recipeService.findById((long) i));
            instruction2.setRecipe(recipeService.findById((long) i));

            instruction.setDescription("TestDescription " + i);
            instruction2.setDescription("AnotherTestDescription " + i);

            instructionService.save(instruction);
            instructionService.save(instruction2);

            instructions.add(instruction);
            instructions.add(instruction2);

            recipe.addInstruction(instruction);
            recipe.addInstruction(instruction2);

/*            recipe.setInstructions(Collections.singletonList(
                    instructionService.findById((long) i))
            );*/

            recipe.setName("TestRecipe " + i);
            recipe.setDescription("TestDesc " + i);

            recipe.setImageUrl("TestUrl" + i);

            recipe.setPrepTime(i + 1 + " minutes");
            recipe.setCookTime(i + 1 + " hours");
            recipe.setFavorite(value);
            value = !value;
            recipeService.save(recipe);
        }
    }
}
