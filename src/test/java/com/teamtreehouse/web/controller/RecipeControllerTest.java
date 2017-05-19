package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.*;
import com.teamtreehouse.service.*;
import com.teamtreehouse.web.exceptions.CategoryNotFoundException;
import com.teamtreehouse.web.exceptions.SearchTermNotFoundException;
import org.hamcrest.Matchers;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.teamtreehouse.web.FlashMessage.Status.SUCCESS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class RecipeControllerTest {

    final String BASE_URL = "http://localhost:8080";

    @Mock
    private RecipeService recipeService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private InstructionService instructionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecipeController recipeController;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setViewResolvers(viewResolver)
                .addFilter(new SecurityContextPersistenceFilter())
                .build();
    }

    @Test
    public void getIndex() throws Exception {
        List<Category> categories = new ArrayList<>();
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        Recipe recipe = recipeBuilder();
        Recipe recipe2 = recipeBuilder();

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        recipes.add(recipe2);

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findAll()).thenReturn(recipes);
        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/").with(user("user1")))
            .andExpect(model().attribute("recipes", recipes))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    public void getRecipeDetail() throws Exception {
        Recipe recipe = recipeBuilder();
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1").with(user("user1")))
            .andExpect(model().attribute("recipe", recipe))
            .andExpect(status().isOk())
            .andExpect(view().name("detail"));
    }

    @Test
    public void formNewRecipeTest() throws Exception {
        List<Category> categories = new ArrayList<>();

        when(userService.findByUsername("user1")).thenReturn(new User());
        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/add").with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }

    @Test
    public void addNewRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/add").with(user("user1")))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void getEditRecipePageTest() throws Exception {
        List<Category> categories = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();
        Recipe recipe = recipeBuilder();

        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findById(1L)).thenReturn(recipe);
        when(categoryService.findAll()).thenReturn(categories);
        when(ingredientService.findAll()).thenReturn(ingredients);
        when(instructionService.findAll()).thenReturn(instructions);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/edit").with(user("user1")))
            .andExpect(model().attribute("recipe", recipe))
            .andExpect(model().attribute("categories", categories))
            .andExpect(model().attribute("ingredients", ingredients))
            .andExpect(model().attribute("instructions", instructions))
            .andExpect(status().isOk())
            .andExpect(view().name("edit"));
    }

    @Test
    public void updateRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);
        recipe.addIngredient(new Ingredient("TestIngredient 2", "TestMeasurement 2", 1));
        recipe.addInstruction(new Instruction("TestDesc 2"));
        recipe.setName("TestName2");
        recipe.setCategory(new Category("TestCategory2"));
        recipe.setDescription("TestDesc2");
        recipe.setCookTime("30 minutes");
        recipe.setPrepTime("20 minutes");
        recipe.setImageUrl("TestUrl2");

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/edit").with(user("user1")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void deleteRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/delete").with(user("user1")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("flash", HasPropertyWithValue.hasProperty("status", Matchers.equalTo(SUCCESS))));
    }

    @Test
    public void deletingRecipeRemovesFavorite() throws Exception {
        Recipe recipe = recipeBuilder();
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        List<Recipe> favorites = user.getFavorites();

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/delete").with(user("user1")));

        assertThat(favorites, IsEmptyCollection.empty());
    }

    @Test
    public void categoryFoundTest() throws Exception {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = recipeBuilder();
        recipes.add(recipe);
        User user = new User();
        user.setUsername("user1");

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findByCategoryName("TestCategory")).thenReturn(recipes);

        recipeService.findByCategoryName("TestCategory");

        mockMvc.perform(MockMvcRequestBuilders.get("/category").param("category", "TestCategory").with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void searchTermFoundTest() throws Exception {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = recipeBuilder();
        recipes.add(recipe);
        User user = new User();
        user.setUsername("user1");

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findByDescriptionContaining("TestDesc")).thenReturn(recipes);

        recipeService.findByDescriptionContaining("TestDesc");

        mockMvc.perform(MockMvcRequestBuilders.get("/search").param("searchTerm", "TestDesc").with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void markFavoriteTest() throws Exception {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipes.add(recipe);
        User user = new User();
        user.setUsername("user1");

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/favorite").with(user("user1")))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("flash", hasProperty("message", equalTo("Recipe added to favorites!"))));
    }

    @Test
    public void unMarkFavoriteTest() throws Exception {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipes.add(recipe);
        User user = new User();
        user.setUsername("user1");
        user.addFavorite(recipe);

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/favorite").with(user("user1")))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("flash", hasProperty("message", equalTo("Recipe removed from favorites!"))));
    }

    @Test(expected = CategoryNotFoundException.class)
    public void categoryNotFoundExceptionTest() throws Exception {
        when(recipeService.findByCategoryName("test")).thenThrow(new CategoryNotFoundException());

        recipeService.findByCategoryName("test");

        mockMvc.perform(MockMvcRequestBuilders.get("/category").with(user("user1")))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("ex", Matchers.instanceOf(CategoryNotFoundException.class)))
                .andExpect(view().name("error"));
    }

    @Test(expected = SearchTermNotFoundException.class)
    public void searchTermNotFoundExceptionTest() throws Exception {
        when(recipeService.findByDescriptionContaining("test")).thenThrow(new SearchTermNotFoundException());

        recipeService.findByDescriptionContaining("test");

        mockMvc.perform(MockMvcRequestBuilders.get("/search").with(user("user1")))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("ex", Matchers.instanceOf(SearchTermNotFoundException.class)))
                .andExpect(view().name("error"));
    }

    @Test
    public void ingredientsSavedWhenRecipeSaved() throws Exception {
        Recipe recipe = recipeBuilder();
        List<Ingredient> ingredients = recipe.getIngredients();

        when(ingredientService.findAll()).thenReturn(ingredients);

        recipe.addIngredient(new Ingredient());
        recipe.addIngredient(new Ingredient());
        recipe.addIngredient(new Ingredient());

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/add").with(user("user1")));

        assertThat(ingredients, hasSize(4));
    }

    @Test
    public void instructionsSavedWhenRecipeSaved() throws Exception {
        Recipe recipe = recipeBuilder();
        List<Instruction> instructions = recipe.getInstructions();

        when(instructionService.findAll()).thenReturn(instructions);

        recipe.addInstruction(new Instruction());
        recipe.addInstruction(new Instruction());
        recipe.addInstruction(new Instruction());

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/add").with(user("user1")));

        assertThat(instructions, hasSize(4));
    }

    private Recipe recipeBuilder() {
        Recipe recipe = new Recipe();
        User user = new User();
        recipe.setId(1L);
        recipe.setName("TestName");
        recipe.setDescription("TestDesc");
        recipe.setCategory(new Category("TestCategory"));
        recipe.setImageUrl("TestUrl");
        recipe.addIngredient(new Ingredient("TestIngredient", "TestMeasurement", 1));
        recipe.addInstruction(new Instruction("TestDesc"));
        recipe.setPrepTime("10 minutes");
        recipe.setCookTime("1 hour");
        recipe.isFavorited(user);
        return recipe;
    }
}