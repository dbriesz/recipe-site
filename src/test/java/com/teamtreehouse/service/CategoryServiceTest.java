package com.teamtreehouse.service;

import com.teamtreehouse.dao.CategoryDao;
import com.teamtreehouse.domain.Category;
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
}
