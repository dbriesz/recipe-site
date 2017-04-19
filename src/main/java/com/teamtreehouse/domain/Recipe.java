package com.teamtreehouse.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Category category;
    private String imageUrl;
    private List<Ingredient> ingredients;
    private String instructions;
    private String prepTime;
    private String cookTime;
    private boolean favorite;
}
