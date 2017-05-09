package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.User;
import com.teamtreehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

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




    // User profile page
    @SuppressWarnings("unchecked")
    @RequestMapping("users/{userId}")
    public String userProfile(@PathVariable String username, Model model) {
        // Get the user given by username
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);

        return "profile";
    }

    // User signup page
    @RequestMapping("users/add")
    public String formNewUser(Model model) {
        // Add model attributes needed for new form
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        model.addAttribute("action", "users/add");

        return "signup";
    }

    // User login page
    @RequestMapping("users/{userId}/login")
    public String login(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", user);
        }
        model.addAttribute("action", String.format("/users/%s/login", username));

        return "login";
    }

/*    // Add a new user
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
    }*/

}
