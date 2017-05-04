package com.teamtreehouse.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private Category category;
    private String imageUrl;
    @OneToMany
    private List<Ingredient> ingredients = new ArrayList<>();
    @OneToMany
    private List<Instruction> instructions = new ArrayList<>();
    private String prepTime;
    private String cookTime;
    private boolean favorite;
    @ManyToOne
    private User user;

    public Recipe() {
    }

    public Recipe(String name, String description, Category category,
                  String imageUrl, List<Ingredient> ingredients, List<Instruction> instructions,
                  String prepTime, String cookTime, boolean favorite) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.favorite = favorite;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        ingredients.add(ingredient);
    }

    public void addInstruction(Instruction instruction) {
        instruction.setRecipe(this);
        instructions.add(instruction);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

/*    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (favorite != recipe.favorite) return false;
        if (id != null ? !id.equals(recipe.id) : recipe.id != null) return false;
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
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (ingredients != null ? ingredients.hashCode() : 0);
        result = 31 * result + (instructions != null ? instructions.hashCode() : 0);
        result = 31 * result + (prepTime != null ? prepTime.hashCode() : 0);
        result = 31 * result + (cookTime != null ? cookTime.hashCode() : 0);
        result = 31 * result + (favorite ? 1 : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
