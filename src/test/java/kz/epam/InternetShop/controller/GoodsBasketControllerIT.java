package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.service.interfaces.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static kz.epam.InternetShop.util.OrderDetailsDataTestUtil.ORDER1_DETAILS;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
class GoodsBasketControllerIT {
    private static final String CLIENT_USERNAME = "User@mail.ru";
    private static final  Long CLIENT_USERNAME_ID = 100001L;
    private static final User USER = User.builder().id(CLIENT_USERNAME_ID).build();

    private MockMvc mockMvc;

    @MockBean
    private GoodsBasketService basketService;

    @MockBean
    private UserService userService;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void getBasketGoods() throws Exception {
        String url = "/goods/basket/";
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        Mockito.when(basketService.getAllOrderDetails(USER)).thenReturn(ORDER1_DETAILS);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"id\":null,\"goodsId\":null,\"goodsName\":\"name1\",\"cost\":5000.0,\"count\":5," +
                "\"goodsPhoto\":\"\",\"orderId\":null,\"available\":false}," +
                "{\"id\":null,\"goodsId\":null,\"goodsName\":\"name2\",\"cost\":5000.0,\"count\":5," +
                "\"goodsPhoto\":\"\",\"orderId\":null,\"available\":false}," +
                "{\"id\":null,\"goodsId\":null,\"goodsName\":\"name3\",\"cost\":5000.0,\"count\":5," +
                "\"goodsPhoto\":\"\",\"orderId\":null,\"available\":false}]";

        Assert.assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void clearBasket() throws Exception {
        String url = "/goods/basket/clear";
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
        Mockito.verify(basketService, Mockito.times(1)).clear(USER);
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void placeOrder() throws Exception {
        String url = "/goods/basket/order";
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
        Mockito.verify(basketService, Mockito.times(1)).setStatusToOne(USER);
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void createOrderDetailsInBasket() throws Exception {
        Long orderDetailsId = 100099L;
        String mockApplicationJson = "{\"id\":" + orderDetailsId.toString() +
                ",\"goodsId\":null,\"goodsName\":\"name1\",\"cost\":5000.0,\"count\":5," +
                "\"goodsPhoto\":\"\",\"orderId\":null,\"available\":false}";
        OrderDetails orderDetails = OrderDetails.builder().id(orderDetailsId).build();

        String url = "/goods/toBasket";
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(mockApplicationJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        Mockito.verify(basketService, Mockito.times(1))
                .createOrderDetailsInBasket(orderDetails, USER);
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void updateCountOrderDetailsInBasket() throws Exception {
        Long orderDetailsId = 100099L;
        String mockApplicationJson = "[{\"id\":" + orderDetailsId.toString() +
                ",\"goodsId\":null,\"goodsName\":\"name1\",\"cost\":5000.0,\"count\":5," +
                "\"goodsPhoto\":\"\",\"orderId\":null,\"available\":false}]";
        OrderDetails orderDetails = OrderDetails.builder().id(orderDetailsId).build();

        String url = "/goods/basket";
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(mockApplicationJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        Mockito.verify(basketService, Mockito.times(1))
                .updateCountOrderDetailsInBasket(Arrays.asList(orderDetails), USER);
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void removeFromBasket() throws Exception {
        Long orderDetailsId = 100099L;
        OrderDetails orderDetails = OrderDetails.builder().id(orderDetailsId).build();
        String url = "/goods/basket/" + orderDetailsId.toString();
        Mockito.when(userService.findById(USER.getId())).thenReturn(USER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(url)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        Mockito.verify(basketService, Mockito.times(1))
                .removeFromBasket(orderDetails, USER);
    }
}