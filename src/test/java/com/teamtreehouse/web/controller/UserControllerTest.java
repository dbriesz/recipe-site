package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.User;
import com.teamtreehouse.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    final String BASE_URL = "http://localhost:8080";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setViewResolvers(viewResolver).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserProfileTest() throws Exception {
        User user = new User("user1", "password", true, new String[]{"ROLE_USER"});
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, "user1");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        when(userService.findByUsername("user1")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    public void getAddUserPageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    public void getUserLoginPageTest() throws Exception {
        User user = userBuilder();

        when(userService.findByUsername("Test User")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void addUserTest() throws Exception {
        User user = userBuilder();

        when(userService.findByUsername("Test User")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    private User userBuilder() {
        User user = new User("Test User", "password", true, new String[]{"ROLE_USER", "ROLE_ADMIN"});
        user.setId(1L);
        return user;
    }
}