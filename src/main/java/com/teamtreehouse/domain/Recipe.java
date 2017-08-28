package com.teamtreehouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Recipe extends BaseEntity {
    @NotNull
    @Size(max = 40)
    private String name;
    private String description;
    @ManyToOne
    private Category category;
    private String imageUrl;
    @OneToMany
    private List<Ingredient> ingredients;
    @OneToMany
    private List<Instruction> instructions;
    private String prepTime;
    private String cookTime;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Recipe() {
        super();
        ingredients = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    public Recipe(String name, String description, Category category,
                  String imageUrl, List<Ingredient> ingredients, List<Instruction> instructions,
                  String prepTime, String cookTime, User user) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.user = user;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public boolean isFavorited(User user) {
        return user.getFavorites().contains(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user != null) {
            user.addCreatedRecipe(this);
        }
        this.user = user;
    }

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if (description != null ? !description.equals(recipe.description) : recipe.description != null) return false;
        if (category != null ? !category.equals(recipe.category) : recipe.category != null) return false;
        if (imageUrl != null ? !imageUrl.equals(recipe.imageUrl) : recipe.imageUrl != null) return false;
        if (ingredients != null ? !ingredients.equals(recipe.ingredients) : recipe.ingredients != null) return false;
        if (instructions != null ? !instructions.equals(recipe.instructions) : recipe.instructions != null)
            return false;
        if (prepTime != null ? !prepTime.equals(recipe.prepTime) : recipe.prepTime != null) return false;
        if (cookTime != null ? !cookTime.equals(recipe.cookTime) : recipe.cookTime != null) return false;
        return user != null ? user.equals(recipe.user) : recipe.user == null;
    }*/
}
