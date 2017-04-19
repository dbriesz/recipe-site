package com.teamtreehouse.domain;

import javax.persistence.*;
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Instruction> instructions;
    private String prepTime;
    private String cookTime;
    private boolean favorite;
}
