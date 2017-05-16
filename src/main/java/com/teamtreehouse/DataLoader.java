package com.teamtreehouse;

import com.teamtreehouse.domain.*;
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

        List<Ingredient> ingredients = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();
        int count = 1;
        String[] roles1 = {"ROLE_USER", "ROLE_ADMIN"};
        String[] roles2 = {"ROLE_USER"};
        User user1 = new User("user1", "password", true, roles1);
        User user2 = new User("user2", "password", true, roles2);
        user1.setId(1L);
        user2.setId(2L);

        userService.save(user1);
        userService.save(user2);

        for (int i = 1; i <= 5; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            categoryService.save(category);

            Recipe recipe = new Recipe();
            recipe.setCategory(category);
            Ingredient ingredient = new Ingredient();
            Ingredient ingredient2 = new Ingredient();

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

            Instruction instruction = new Instruction();
            Instruction instruction2 = new Instruction();

            instruction.setDescription("TestDescription " + i);
            instruction2.setDescription("AnotherTestDescription " + i);

            instructionService.save(instruction);
            instructionService.save(instruction2);

            instructions.add(instruction);
            instructions.add(instruction2);

            recipe.addInstruction(instruction);
            recipe.addInstruction(instruction2);

            recipe.setName("TestRecipe " + i);
            recipe.setDescription("TestDesc " + i);

            recipe.setImageUrl("TestUrl" + i);

            recipe.setPrepTime(i + 1 + " minutes");
            recipe.setCookTime(i + 1 + " hours");

            recipe.setUser(user1);
            if (count %2 == 0) {
                user1.addFavorite(recipe);
            }
            count++;
            recipeService.save(recipe);
            userService.save(user1);
        }

        int num = 1;
        for (int i = 6; i <= 10; i++) {
            Category category = new Category();
            category.setName("Category " + i);
            categoryService.save(category);

            Recipe recipe2 = new Recipe();
            recipe2.setCategory(category);
            Ingredient ingredient = new Ingredient();
            Ingredient ingredient2 = new Ingredient();

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

            recipe2.addIngredient(ingredient);
            recipe2.addIngredient(ingredient2);

            Instruction instruction = new Instruction();
            Instruction instruction2 = new Instruction();

            instruction.setDescription("TestDescription " + i);
            instruction2.setDescription("AnotherTestDescription " + i);

            instructionService.save(instruction);
            instructionService.save(instruction2);

            instructions.add(instruction);
            instructions.add(instruction2);

            recipe2.addInstruction(instruction);
            recipe2.addInstruction(instruction2);

            recipe2.setName("TestRecipe " + i);
            recipe2.setDescription("TestDesc " + i);

            recipe2.setImageUrl("TestUrl" + i);

            recipe2.setPrepTime(i + 1 + " minutes");
            recipe2.setCookTime(i + 1 + " hours");

            recipe2.setUser(user2);
            if (num %2 == 0) {
                user2.addFavorite(recipe2);
            }
            num++;
            recipeService.save(recipe2);
            userService.save(user2);
        }
    }
}
