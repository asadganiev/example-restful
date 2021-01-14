package com.example.rest.controllers.v1;

import com.example.rest.forms.UserRegistrationForm;
import com.example.rest.services.PasswordEncoderImpl;
import com.example.rest.services.UserDetailsServiceImpl;
import com.example.rest.services.Users;
import com.example.rest.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(classes = {BCryptPasswordEncoder.class, PasswordEncoderImpl.class})
@WebMvcTest(controllers = UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Users users;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    static String asJsonString(final Object obj) {

        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @AllArgsConstructor
    class TestFixture {

        private String shortData;
        private String longData;
    }

    @Test
    void signUpWithBadRequest() throws Exception {

        UserRegistrationForm user = new UserRegistrationForm();

        Map<String, String> fieldErrors = new HashMap<>();

        fieldErrors.put("username", "username length should be 2-50");
        fieldErrors.put("password", "password length should be 8-50");
        fieldErrors.put("firstName", "firstname length should be 2-50");
        fieldErrors.put("lastName", "lastname length should be 2-50");
        fieldErrors.put("street", "street length should be 2-100");
        fieldErrors.put("region", "region length should be 2-100");
        fieldErrors.put("city", "city length should be 2-100");

        for (Field field : user.getClass().getDeclaredFields()) {

            String fieldName = field.getName();

            if (!fieldName.equals("id")
                    && !fieldName.equals("zipCode")) {

                initUser(user);

                field.setAccessible(true);

                if (!fieldName.equals("birthday")) {

                    // Blank input
                    field.set(user, "");

                    mockMvc.perform(post("/v1/users/signup")
                            .content(asJsonString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest())
                            .andExpect(content().string(containsString(
                                    fieldName.toLowerCase() + " should not be blank")));

                    // Short input
                    field.set(user, "1");

                    mockMvc.perform(post("/v1/users/signup")
                            .content(asJsonString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest())
                            .andExpect(
                                    content().string(containsString(fieldErrors.get(fieldName))));

                    // Long input
                    field.set(user, "veryLongInputValueThatContainsMoreThanFiftyCharacters" +
                            "veryLongInputValueThatContainsMoreThanFiftyCharacters");

                    mockMvc.perform(post("/v1/users/signup")
                            .content(asJsonString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest())
                            .andExpect(
                                    content().string(containsString(fieldErrors.get(fieldName))));
                } else {

                    field.set(user, "");

                    mockMvc.perform(post("/v1/users/signup")
                            .content(asJsonString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest())
                            .andExpect(
                                    content().string(containsString(
                                            "birthday should not be empty")));

                    field.set(user, null);

                    mockMvc.perform(post("/v1/users/signup")
                            .content(asJsonString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isBadRequest())
                            .andExpect(
                                    content().string(containsString(
                                            "birthday should not be empty")));
                }
            }
        }
    }

    private void initUser(UserRegistrationForm user) {

        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRegion("Samarkand");
        user.setCity("Samarkand");
        user.setStreet("Baker St 221B");
        user.setBirthday("01-01-2000");
        user.setZipCode(140100);
    }

    @Test
    void signUpWhenUserDoesNotExist() throws Exception {

        UserRegistrationForm user = new UserRegistrationForm();

        initUser(user);

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);

        when(users.findByUsername(any())).thenReturn(null);
        when(users.save(any())).thenReturn(mockUser);

        mockMvc.perform(post("/v1/users/signup")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/v1/users/1"))
                .andExpect(content().string(containsString("User Created")));
    }

    @Test
    void signUpWhenUserExists() throws Exception {

        UserRegistrationForm user = new UserRegistrationForm();

        initUser(user);

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);

        when(users.findByUsername(any())).thenReturn(mockUser);

        mockMvc.perform(post("/v1/users/signup")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(
                        content().string(containsString("User with such username already exists")));
    }
}