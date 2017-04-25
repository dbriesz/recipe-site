package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Category;
import com.teamtreehouse.domain.Ingredient;
import com.teamtreehouse.domain.Instruction;
import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
public class RecipeControllerTest {

    final String BASE_URL = "http://localhost:8080";

    @Mock
    private MockHttpSession session;

    @Mock
    private RecipeService recipeService;

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
        Recipe recipe = recipeBuilder();
        Recipe recipe2 = recipeBuilder();

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        recipes.add(recipe2);

        when(recipeService.findAll()).thenReturn(recipes);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    public void getRecipeDetail() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("detail"));
    }

    @Test
    public void addNewRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/add"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/"));
    }

    @Test
    public void editRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("edit"));
    }

    @Test
    public void updateRecipeTest() throws Exception {
        Recipe recipe = recipeBuilder();

        when(recipeService.findById(1L)).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes/1"));
    }

    private Recipe recipeBuilder() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("TestName");
        recipe.setDescription("TestDesc");
        recipe.setCategory(new Category("TestCategory"));
        recipe.setImageUrl("TestUrl");
        recipe.addIngredient(new Ingredient());
        recipe.addInstruction(new Instruction());
        recipe.setPrepTime("10 minutes");
        recipe.setCookTime("1 hour");
        recipe.setFavorite(true);
        return recipe;
    }

}