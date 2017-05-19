package com.teamtreehouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;
    @OneToMany
    private List<Recipe> recipes;

    public Category() {
        super();
        recipes = new ArrayList<>();
    }

    public Category(String name) {
        this();
        this.name = name;
    }

    public void addRecipe(Recipe recipe) {
        recipe.setCategory(this);
        recipes.add(recipe);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
