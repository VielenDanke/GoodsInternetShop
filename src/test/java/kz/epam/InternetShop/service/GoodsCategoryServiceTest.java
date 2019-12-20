package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.repository.GoodsCategoryRepository;
import kz.epam.InternetShop.service.interfaces.GoodsCategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static kz.epam.InternetShop.util.GoodsCategoryDataTestUtil.GOODS_CATEGORIES;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class GoodsCategoryServiceTest {

    private GoodsCategoryService goodsCategoryService;

    @MockBean
    private GoodsCategoryRepository goodsCategoryRepository;

    @Test
    public void shouldReturnAllGoodsCategory() {
        Mockito.when(goodsCategoryRepository.findAll()).thenReturn(GOODS_CATEGORIES);

        List<GoodsCategory> goodsCategoryList = goodsCategoryService.getAll();

        Mockito.verify(goodsCategoryRepository, Mockito.times(1)).findAll();
        Assert.assertEquals(GOODS_CATEGORIES, goodsCategoryList);
    }

    @Autowired
    public void setGoodsCategoryService(GoodsCategoryService goodsCategoryService) {
        this.goodsCategoryService = goodsCategoryService;
    }
}
