package com.teamtreehouse.service;

import com.teamtreehouse.dao.RecipeDao;
import com.teamtreehouse.domain.Category;
import com.teamtreehouse.domain.Ingredient;
import com.teamtreehouse.domain.Instruction;
import com.teamtreehouse.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    private List<Recipe> recipes;
    private Recipe recipe1;
    private Recipe recipe2;

    @Mock
    private RecipeDao dao;

    @InjectMocks
    private RecipeService service = new RecipeServiceImpl();

    @Before
    public void setUp() throws Exception {
        List<Ingredient> ingredients = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();
        recipe1 = new Recipe("TestName 1", "TestDesc 1", new Category("test"),
                "TestUrl 1", ingredients, instructions, "Test PrepTime 1", "Test CookTime 1", false);
        recipe2 = new Recipe("TestName 2", "TestDesc 2", new Category("test"),
                "TestUrl 2", ingredients, instructions, "Test PrepTime 2", "Test CookTime 2", true);
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
}
