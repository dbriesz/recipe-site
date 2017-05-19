package com.teamtreehouse.service;

import com.teamtreehouse.dao.UserDao;
import com.teamtreehouse.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao dao;

    @InjectMocks
    private UserService service = new UserServiceImpl();

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<User> users = Arrays.asList(
                new User(),
                new User()
        );

        when(dao.findAll()).thenReturn(users);

        assertEquals("findAll should return two users", 2, service.findAll().size());
        verify(dao).findAll();
    }

    @Test
    public void findByUsername_ShouldReturnOne() throws Exception {
        when(dao.findByUsername("TestUser")).thenReturn(new User());
        assertThat(service.findByUsername("TestUser"), instanceOf(User.class));
        verify(dao).findByUsername("TestUser");
    }

    @Test
    public void findById_ShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new User());
        assertThat(service.findById(1L), instanceOf(User.class));
        verify(dao).findOne(1L);
    }

    @Test
    public void saveUserTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        String[] roles = {"ROLE_USER"};
        user.setRoles(roles);

        when(dao.findOne(1L)).thenReturn(user);
        User result = service.findById(1L);

        assertEquals(1, result.getId().intValue());
        assertEquals("test", result.getUsername());
        assertEquals(1, result.getRoles().length);
    }

    @Test
    public void usernameNotFoundExceptionTest() throws Exception {
        try {
            service.findByUsername("test");
        } catch (UsernameNotFoundException ex) {
            assertThat(ex.getMessage(), is("User not found"));
        }
    }
}
