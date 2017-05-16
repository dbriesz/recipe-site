package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Recipe;
import com.teamtreehouse.domain.User;
import com.teamtreehouse.service.RecipeService;
import com.teamtreehouse.service.UserService;
import com.teamtreehouse.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static com.teamtreehouse.web.FlashMessage.Status.FAILURE;

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
        org.springframework.security.core.userdetails.User principal = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken)) {
            Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (o != null) {
                principal = (org.springframework.security.core.userdetails.User) o;
            }

            if (principal != null) {
                User foundUser = userService.findByUsername(principal.getUsername());
                model.addAttribute("currentUser", foundUser);
                String name = foundUser.getUsername(); //get logged in username
                model.addAttribute("username", name);
            }
        } else {
            throw new AccessDeniedException("Not logged in");
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
        List<Recipe> recipes = recipeService.findByUser();
        model.addAttribute("recipes", recipes);

        return "profile";
    }

    // Add a new user
    @RequestMapping(value = "users/add", method = RequestMethod.POST)
    public String addUser(User user, BindingResult result,
                          RedirectAttributes redirectAttributes) {
        // Add user if valid data was received
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Problem creating account. Please try again.", FlashMessage.Status.FAILURE));
            return "redirect:/signup";
        }
        if (userService.findByUsername(user.getUsername()) == null) {
            user.setRoles(new String[]{"ROLE_USER"});
            userService.save(user);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(
                    "Account successfully created! Please enter your username and password to log in.", FlashMessage.Status.SUCCESS));
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("An account with that username already exists. Please try again.", FlashMessage.Status.FAILURE));
            return "redirect:/signup";
        }
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public String adeHandler(HttpServletRequest request, AccessDeniedException ex) {
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put("flash", new FlashMessage(ex.getMessage(), FAILURE));
        return "redirect:/login";
    }
}
