package com.teamtreehouse.service;

import com.teamtreehouse.dao.CategoryDao;
import com.teamtreehouse.domain.Category;
import com.teamtreehouse.domain.Recipe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @Mock
    private CategoryDao dao;

    @InjectMocks
    private CategoryService service = new CategoryServiceImpl();

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<Category> categories = Arrays.asList(
                new Category(),
                new Category()
        );

        when(dao.findAll()).thenReturn(categories);

        assertEquals("findAll should return two categories", 2, service.findAll().size());
        verify(dao).findAll();
    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Category());
        assertThat(service.findById(1L), instanceOf(Category.class));
        verify(dao).findOne(1L);
    }

    @Test
    public void saveCategoryTest() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        Recipe recipe = new Recipe();
        category.addRecipe(recipe);

        when(dao.findOne(1L)).thenReturn(category);
        Category result = service.findById(1L);

        assertEquals(1, result.getId().intValue());
        assertEquals("test", result.getName());
        assertEquals(1, result.getRecipes().size());
        verify(dao).findOne(1L);
    }

    @Test
    public void deleteCategoryTest() throws Exception {
        Category category = new Category();

        when(dao.findOne(1L)).thenReturn(category);
        service.delete(category);
        verify(dao, times(1)).delete(category);
    }
}
