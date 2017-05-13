package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.*;
import com.teamtreehouse.service.*;
import com.teamtreehouse.web.exceptions.CategoryNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.contains;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .setViewResolvers(viewResolver).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getIndex() throws Exception {
        List<Category> categories = new ArrayList<>();
        Recipe recipe = recipeBuilder();
        Recipe recipe2 = recipeBuilder();

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        recipes.add(recipe2);
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(userService.findByUsername("user1")).thenReturn(user);
        when(recipeService.findAll()).thenReturn(recipes);
        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("detail"));
    }

    @Test
    public void formNewRecipeTest() throws Exception {
        List<Category> categories = new ArrayList<>();
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(userService.findByUsername("user1")).thenReturn(user);
        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }

    @Test
    public void addNewRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/add"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
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

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("edit"));
    }

    @Test
    public void updateRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);
        recipe.setName("TestName2");

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/edit"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void deleteRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test(expected = CategoryNotFoundException.class)
    public void categoryNotFoundExceptionTest() throws Exception {
        when(recipeService.findByCategoryName("test")).thenThrow(new CategoryNotFoundException());

        recipeService.findByCategoryName("test");

        mockMvc.perform(MockMvcRequestBuilders.get("/category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("error"));
    }

    private Recipe recipeBuilder() {
        Recipe recipe = new Recipe();
        User user = new User();
        recipe.setId(1L);
        recipe.setName("TestName");
        recipe.setDescription("TestDesc");
        recipe.setCategory(new Category("TestCategory"));
        recipe.setImageUrl("TestUrl");
        recipe.addIngredient(new Ingredient());
        recipe.addInstruction(new Instruction());
        recipe.setPrepTime("10 minutes");
        recipe.setCookTime("1 hour");
        recipe.isFavorited(user);
        return recipe;
    }
}