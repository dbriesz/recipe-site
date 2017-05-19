package com.teamtreehouse.service;

import com.teamtreehouse.dao.IngredientDao;
import com.teamtreehouse.domain.Ingredient;
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
public class IngredientServiceTest {

    @Mock
    private IngredientDao dao;

    @InjectMocks
    private IngredientService service = new IngredientServiceImpl();

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient(),
                new Ingredient()
        );

        when(dao.findAll()).thenReturn(ingredients);

        assertEquals("findAll should return two ingredients", 2, service.findAll().size());
        verify(dao).findAll();
    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Ingredient());
        assertThat(service.findById(1L), instanceOf(Ingredient.class));
        verify(dao).findOne(1L);
    }

    @Test
    public void saveIngredientTest() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("test");
        ingredient.setMeasurement("1 cup");
        ingredient.setQuantity(1);

        when(dao.findOne(1L)).thenReturn(ingredient);
        Ingredient result = service.findById(1L);

        assertEquals(1, result.getId().intValue());
        assertEquals("test", result.getName());
        assertEquals("1 cup", result.getMeasurement());
        assertEquals(1, result.getQuantity());
    }

    @Test
    public void deleteIngredientTest() throws Exception {
        Ingredient ingredient = new Ingredient();

        when(dao.findOne(1L)).thenReturn(ingredient);
        service.delete(ingredient);
        verify(dao, times(1)).delete(ingredient);
    }
}
