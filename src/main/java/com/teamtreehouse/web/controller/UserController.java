package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.domain.User;
import com.teamtreehouse.service.RecipeService;
import com.teamtreehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "login";
    }

    @RequestMapping("/access_denied")
    public String accessDenied() {
        return "access_denied";
    }

    private void addCurrentLoggedInUserToModel(Model model) {
        User user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            user = (User) principal;
        }

        if (user != null) {
            user = userService.findByUsername(user.getUsername());
            String name = user.getUsername(); //get logged in username
            model.addAttribute("username", name);
            model.addAttribute("currentUser", user);
        }
    }

    // User signup page
    @RequestMapping("/signup")
    public String formNewUser(Model model) {
        // Add model attributes needed for new form
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        return "signup";
    }

    // User profile page
    @RequestMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        // Get the user given by username
        addCurrentLoggedInUserToModel(model);
//        model.addAttribute("username", principal.getName());
        List<Recipe> recipes = recipeService.findByUser();
        model.addAttribute("recipes", recipes);

        return "profile";
    }

    // Add a new user
    @RequestMapping(value = "users/add", method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result) {
        // Add user if valid data was received
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
        } else {
            userService.save(user);
        }

        // Redirect browser to home page
        return "redirect:/";
    }
}
