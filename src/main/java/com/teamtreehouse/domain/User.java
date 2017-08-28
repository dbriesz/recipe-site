package com.teamtreehouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends BaseEntity {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private String username;
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @JsonIgnore
    private String[] roles;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Recipe> createdRecipes;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Recipe> favorites;

    public User() {
        super();
        createdRecipes = new ArrayList<>();
        favorites = new ArrayList<>();
    }

    public User(String username, String password, boolean enabled, String[] roles) {
        this();
        this.username = username;
        setPassword(password);
        this.enabled = enabled;
        this.roles = roles;
    }

    public void addCreatedRecipe (Recipe recipe) {
        createdRecipes.add(recipe);
    }

    public void removeCreatedRecipe (Recipe recipe) {
        createdRecipes.remove(recipe);
    }

    public List<Recipe> getCreatedRecipes () {
        return createdRecipes;
    }

    public void setCreatedRecipes(List<Recipe> createdRecipes) {
        this.createdRecipes = createdRecipes;
    }

    public void addFavorite(Recipe recipe) {
        favorites.add(recipe);
    }

    public void removeFavorite(Recipe recipe) {
        favorites.remove(recipe);
    }

    public List<Recipe> getFavorites() {
        return favorites;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username != null ? username.equals(user.username) : user.username == null;
    }

    /*    public boolean hasAdminRole() {
        List<String> roles = Arrays.asList(getRoles());
        return roles.contains("ROLE_ADMIN");
    }*/
}
