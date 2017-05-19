package com.teamtreehouse.service;

import com.teamtreehouse.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DetailsServiceTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private DetailsService service;

    @Test
    public void usernameNotFoundExceptionTest() throws Exception {
        try {
            service.loadUserByUsername("test");
        } catch (UsernameNotFoundException ex) {
            assertThat(ex.getMessage(), is("User not found"));
        }
    }

    @Test
    public void createUserAccountTest() throws Exception {
        User user = userBuilder();
        when(userService.findByUsername("Test User")).thenReturn(user);

        assertThat(service.loadUserByUsername("Test User").getUsername(), is("Test User"));
    }

    private User userBuilder() {
        User user = new User("Test User", "password", true, new String[]{"ROLE_USER", "ROLE_ADMIN"});
        user.setId(1L);
        return user;
    }
}
