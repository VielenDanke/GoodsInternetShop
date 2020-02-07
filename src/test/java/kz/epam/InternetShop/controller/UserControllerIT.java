package kz.epam.InternetShop.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class UserControllerIT {

    private static final String ADMIN_USERNAME = "Admin@mail.ru";
    private static final String CLIENT_USERNAME = "User@mail.ru";

    private MockMvc mockMvc;
    private UserController userController;

    @Test
    public void userControllerShouldNotBeNull() {
        Assert.assertNotNull(userController);
    }

    @Test
    public void shouldBeForbiddenForUnauthorizedPerson_WhenGettingAllUsers() throws Exception {
        String url = "/user/all";

        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldBeForbiddenForUnauthorizedPerson_WhenSearchingUserById() throws Exception {
        String url = "/user/100001";

        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldBeForbiddenForUnauthorizedPerson_WhenDeletingUserById() throws Exception {
        String url = "/user/100001";

        this.mockMvc.perform(delete(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldBeForbiddenFroUnauthorizedPerson_WhenUpdatingPerson() throws Exception {
        String url = "/user/update";

        this.mockMvc.perform(put(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldBeForbiddenFroUnauthorizedPerson_WhenGettingUserFromUserPrincipal() throws Exception {
        String url = "/user/me";

        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(ADMIN_USERNAME)
    public void shouldGetAllUsersFromDatabase() throws Exception {
        String url = "/user/all";

        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(ADMIN_USERNAME)
    public void shouldGetUserById_WhenSearchingUserById() throws Exception {
        String url = "/user/100001";

        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(ADMIN_USERNAME)
    public void shouldDeleteUserById() throws Exception {
        String url = "/user/100001";
        String successfulMessage = "User deleted successfully";

        this.mockMvc.perform(delete(url))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(successfulMessage));
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    public void shouldUpdateUserByUserPrincipal() throws Exception {
        String url = "/user/update";
        String jsonContent = "{\"fullName\":\"fullName\", \"address\":\"address\"}";

        this.mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                    .andDo(print())
                    .andExpect(authenticated())
                    .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    public void shouldReturnUserByUserPrincipal() throws Exception {
        String url = "/user/me";

        this.mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
