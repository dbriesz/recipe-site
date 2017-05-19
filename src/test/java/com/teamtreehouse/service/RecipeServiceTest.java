package com.teamtreehouse.service;

import com.teamtreehouse.dao.RecipeDao;
import com.teamtreehouse.domain.*;
import com.teamtreehouse.web.exceptions.CategoryNotFoundException;
import com.teamtreehouse.web.exceptions.SearchTermNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    private List<Recipe> recipes;

    @Mock
    private RecipeDao dao;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecipeService service = new RecipeServiceImpl();

    @Before
    public void setUp() throws Exception {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();
        Recipe recipe1 = new Recipe("TestName 1", "TestDesc 1", new Category("test"),
                "TestUrl 1", ingredients, instructions, "Test PrepTime 1", "Test CookTime 1", user1);
        Recipe recipe2 = new Recipe("TestName 2", "TestDesc 2", new Category("test"),
                "TestUrl 2", ingredients, instructions, "Test PrepTime 2", "Test CookTime 2", user2);
        recipes = Arrays.asList(recipe1, recipe2);
    }

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        when(dao.findAll()).thenReturn(recipes);

        assertEquals("findAll should return two recipes", 2, service.findAll().size());
        verify(dao).findAll();
    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Recipe());

        assertThat(service.findById(1L), instanceOf(Recipe.class));
        verify(dao).findOne(1L);
    }

    @Test
    public void findByCategoryName_ShouldReturnTwo() throws Exception {
        when(dao.findByCategoryName("test")).thenReturn(recipes);

        assertEquals("findByCategoryName should return two recipes", 2, service.findByCategoryName("test").size());
        verify(dao).findByCategoryName("test");
    }

    @Test
    public void findBySearchTerm_ShouldReturnTwo() throws Exception {
        when(dao.findByDescriptionContaining("TestDesc")).thenReturn(recipes);

        assertEquals("findByDescriptionContaining should return one recipe",
                2, service.findByDescriptionContaining("TestDesc").size());
        verify(dao).findByDescriptionContaining("TestDesc");
    }

    @Test
    public void findByUser_ShouldReturnOne() throws Exception {
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(dao.findByUser()).thenReturn(recipes);

        assertEquals("findByUser should return one recipe", 2, service.findByUser().size());
        verify(dao).findByUser();
    }

    @Test
    public void saveRecipeTest() throws Exception {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();
        User user1 = new User();
        Recipe recipe = new Recipe("TestName 1", "TestDesc 1", new Category("test"),
                "TestUrl 1", ingredients, instructions, "Test PrepTime 1", "Test CookTime 1", user1);
        recipe.setId(1L);
        recipe.addIngredient(new Ingredient());
        recipe.addInstruction(new Instruction());

        when(dao.findOne(1L)).thenReturn(recipe);
        Recipe result = service.findById(1L);

        assertEquals(1, result.getId().intValue());
        assertEquals("TestName 1", result.getName());
        assertEquals("TestDesc 1", result.getDescription());
        assertEquals("test", result.getCategory().getName());
        assertEquals("TestUrl 1", result.getImageUrl());
        assertEquals(1, result.getIngredients().size());
        assertEquals(1, result.getInstructions().size());
        assertEquals("Test PrepTime 1", result.getPrepTime());
        assertEquals("Test CookTime 1", result.getCookTime());
        verify(dao).findOne(1L);
    }

    @Test
    public void deleteRecipeTest() throws Exception {
        Recipe recipe = new Recipe();
        User user = new User();
        user.setId(1L);
        user.addFavorite(recipe);

        when(dao.findOne(1L)).thenReturn(recipe);
        service.delete(recipe);
        verify(dao, times(1)).delete(recipe);
    }

    @Test
    public void categoryNotFoundExceptionTest() throws Exception {
        try {
            service.findByCategoryName("test");
        } catch (CategoryNotFoundException ex) {
            assertThat(ex.getMessage(), is("Sorry, no such category exists. Please try again."));
        }
    }

    @Test
    public void searchTermNotFoundExceptionTest() throws Exception {
        try {
            service.findByDescriptionContaining("test");
        } catch (SearchTermNotFoundException ex) {
            assertThat(ex.getMessage(), is("Sorry, no recipes found. Please try again."));
        }
    }
}
