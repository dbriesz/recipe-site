package com.teamtreehouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class User extends BaseEntity {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private String username;
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @JsonIgnore
    private String[] roles;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Recipe> favorites = new ArrayList<>();

    public User() {
        super();
    }

    public User(String username, String password, boolean enabled, String[] roles) {
        this();
        this.username = username;
        setPassword(password);
        this.enabled = enabled;
        this.roles = roles;
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

    public boolean hasAdminRole() {
        List<String> roles = Arrays.asList(getRoles());
        return roles.contains("ROLE_ADMIN");
    }
}
