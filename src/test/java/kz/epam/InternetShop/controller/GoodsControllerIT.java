package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.TO.GoodsFiltersTO;
import kz.epam.InternetShop.model.filter.AccessibleGoodsFilterImpl;
import kz.epam.InternetShop.model.filter.NameLikeGoodsFilterImpl;
import kz.epam.InternetShop.service.interfaces.GoodsCategoryService;
import kz.epam.InternetShop.service.interfaces.GoodsService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import java.io.Serializable;
import java.util.*;

import static kz.epam.InternetShop.util.GoodsCategoryDataTestUtil.GOODS_CATEGORIES;
import static kz.epam.InternetShop.util.GoodsDataTestUtil.GOODS_LIST;
import static kz.epam.InternetShop.util.TOUtil.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
class GoodsControllerIT {
    private static final String ADMIN_USERNAME = "Admin@mail.ru";
    private static final String CLIENT_USERNAME = "User@mail.ru";

    private MockMvc mockMvc;

    @MockBean
    private GoodsCategoryService categoryService;

    @MockBean
    private GoodsService goodsService;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void getGoodsCategories() throws Exception {
        String url = "/goods/categories/";
        Mockito.when(categoryService.getAll()).thenReturn(GOODS_CATEGORIES);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected =  "[{\"id\":null,\"name\":\"testCategory2\"}," +
                            "{\"id\":null,\"name\":\"testCategory3\"}," +
                            "{\"id\":null,\"name\":\"testCategory4\"}]";
        Assert.assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void findAll() throws Exception {
        String url = "/goods/";
        GoodsFiltersTO goodsFiltersTO = GoodsFiltersTO.builder()
                .accessibleGoodsFilter(new AccessibleGoodsFilterImpl(true))
                .inRangeOfCostGoodsFilter(null)
                .nameLikeFilter(null)
                .descriptionLikeFilter(null)
                .build();
        String mockApplicationJson ="{\"accessibleGoodsFilter\":{\"active\": true}}";
        Mockito.when(goodsService.findAll(asList(goodsFiltersTO))).thenReturn(GOODS_LIST);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(mockApplicationJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected =  "[{\"id\":null,\"name\":\"testFirst\",\"cost\":100.12,\"count\":3," +
                                         "\"description\":\"testFirst\",\"photos\":[\"testFirst\"]}," +
                            "{\"id\":null,\"name\":\"testSecond\",\"cost\":200.22,\"count\":4," +
                                         "\"description\":\"testSecond\",\"photos\":[\"testSecond\"]}," +
                            "{\"id\":null,\"name\":\"testThird\",\"cost\":300.31,\"count\":5," +
                                         "\"description\":\"testThird\",\"photos\":[\"testThird\"]}]";
        Assert.assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void findAllByGoodsCategory() throws Exception {
        Long goodsCategoryId = 100099L;
        String url = "/goods/categories/" + goodsCategoryId.toString();
        GoodsCategory goodsCategory = GoodsCategory.builder().id(goodsCategoryId).build();
        GoodsFiltersTO goodsFiltersTO = GoodsFiltersTO.builder()
                .accessibleGoodsFilter(new AccessibleGoodsFilterImpl(true))
                .inRangeOfCostGoodsFilter(null)
                .nameLikeFilter(null)
                .descriptionLikeFilter(null)
                .build();
        String mockApplicationJson ="{\"accessibleGoodsFilter\":{\"active\": true}}";
        Mockito.when(goodsService.findAllByGoodsCategory(goodsCategory, asList(goodsFiltersTO))).thenReturn(GOODS_LIST);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(mockApplicationJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected =  "[{\"id\":null,\"name\":\"testFirst\",\"cost\":100.12,\"count\":3," +
                "\"description\":\"testFirst\",\"photos\":[\"testFirst\"]}," +
                "{\"id\":null,\"name\":\"testSecond\",\"cost\":200.22,\"count\":4," +
                "\"description\":\"testSecond\",\"photos\":[\"testSecond\"]}," +
                "{\"id\":null,\"name\":\"testThird\",\"cost\":300.31,\"count\":5," +
                "\"description\":\"testThird\",\"photos\":[\"testThird\"]}]";
        Assert.assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void delete() throws Exception {
        Long goodsId = 100099L;
        String url = "/goods/" + goodsId.toString();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(url)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        //Assert that the return status is 204 No Content
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void update() throws Exception {
        Long goodsId = 100099L;
        String url = "/goods/" + goodsId.toString();

        String mockApplicationJson = "{\"id\":null,\"name\":\"testFirst\",\"cost\":100.12,\"count\":3," +
                                                       "\"description\":\"testFirst\",\"photos\":[\"testFirst\"]}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(mockApplicationJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        //Assert that the return status is 204 No Content
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithUserDetails(CLIENT_USERNAME)
    void create() throws Exception {
        Long goodsId = 100099L;
        String url = "/goods/";
        String mockApplicationJson = "{\"id\":null,\"name\":\"testFirst\"," +
                "\"description\":\"testFirst\",\"cost\":100.12,\"count\":3," +
                "\"photos\":[\"testFirst\"],\"goodsCategory\":null}";
        Goods goods = Goods.builder()
                .id(null)
                .name("testFirst")
                .cost(100.12d)
                .count(3)
                .description("testFirst")
                .photos(Arrays.asList("testFirst"))
                .build();
        Mockito.when(goodsService.save(goods)).thenReturn(goods);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .content(mockApplicationJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = mockApplicationJson;
        Assert.assertEquals(expected, result.getResponse().getContentAsString());
    }
}