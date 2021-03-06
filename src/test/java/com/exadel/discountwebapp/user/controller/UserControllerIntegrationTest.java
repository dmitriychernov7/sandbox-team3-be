package com.exadel.discountwebapp.user.controller;

import com.exadel.discountwebapp.user.repository.UserRepository;
import com.exadel.discountwebapp.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/user-init.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:sql/clean-up.sql")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetAllUsersWithRoleUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].firstName").value("Ivan"))
                .andExpect(jsonPath("$.content[0].lastName").value("Ivanov"))
                .andExpect(jsonPath("$.content[0].email").value("ivan_ivanov@gmail.com"))
                .andExpect(jsonPath("$.content[0].imageUrl").value("user1.jsp"))

                .andExpect(jsonPath("$.content[1].firstName").value("Petro"))
                .andExpect(jsonPath("$.content[1].lastName").value("Petrenko"))
                .andExpect(jsonPath("$.content[1].email").value("petro_petrenko@gmail.com"))
                .andExpect(jsonPath("$.content[1].imageUrl").value("user2.jsp"))

                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetUserByIdWithRoleUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/2"))
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Petro"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Petrenko"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("petro_petrenko@gmail.com"))
                .andExpect(jsonPath("$.imageUrl").value("user2.jsp"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsersWithRoleAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.content[0].firstName").value("Ivan"))
                .andExpect(jsonPath("$.content[0].lastName").value("Ivanov"))
                .andExpect(jsonPath("$.content[0].email").value("ivan_ivanov@gmail.com"))
                .andExpect(jsonPath("$.content[0].imageUrl").value("user1.jsp"))

                .andExpect(jsonPath("$.content[1].firstName").value("Petro"))
                .andExpect(jsonPath("$.content[1].lastName").value("Petrenko"))
                .andExpect(jsonPath("$.content[1].email").value("petro_petrenko@gmail.com"))
                .andExpect(jsonPath("$.content[1].imageUrl").value("user2.jsp"))

                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetUserByIdWithRoleAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Ivan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Ivanov"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ivan_ivanov@gmail.com"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturn403ErrorWhenNotAuthorizedUserTryGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().reason("Access Denied"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void shouldReturn403ErrorWhenNotAuthorizedUserTryGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().reason("Access Denied"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn404ErrorIfAuthorizedUserEnteredWrongUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sdfdf/dsfdfd"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn404ErrorIfAuthorizedUserTryGetUserByWrongId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
