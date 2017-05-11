package com.teamtreehouse.dao;

import com.teamtreehouse.domain.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface RecipeDao extends PagingAndSortingRepository<Recipe, Long> {
    @RestResource(rel = "recipe-category", path = "categoryName")
    List<Recipe> findByCategoryName(@Param("categoryName") String name);

    @Query("select r from Recipe r where r.user.id=:#{principal.id}")
    List<Recipe> findAll();
}
