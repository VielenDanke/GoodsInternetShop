package kz.epam.InternetShop.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class AuthControllerIT {

    private MockMvc mockMvc;
    private AuthController authController;

    @Test
    public void autowiredContentShouldNotBeNull() {
        Assert.assertNotNull(mockMvc);
        Assert.assertNotNull(authController);
    }

    @Test
    public void shouldSaveUserToDatabase() throws Exception {
        String url = "/auth/signup";
        String jsonContent = "{\"email\":\"newuser@mail.ru\", \"password\":\"test\", \"name\":\"username\", \"address\":\"address\"}";

        this.mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andExpect(status().isCreated());
    }

    @Test
    public void shouldLoginUserWithCorrectCredentials() throws Exception {
        String url = "/auth/login";
        String jsonContent = "{\"email\":\"newuser@mail.ru\",\"password\":\"test\"}";

        this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonContent)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }
}
