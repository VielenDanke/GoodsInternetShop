package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.filter.*;
import kz.epam.InternetShop.repository.GoodsRepository;
import kz.epam.InternetShop.service.interfaces.GoodsService;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kz.epam.InternetShop.util.TestFieldsUtil.RANDOM_NUMBER_RANGE_999;
import static kz.epam.InternetShop.util.GoodsDataTestUtil.GOODS_LIST;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class GoodsServiceTest {

    private GoodsService goodsService;
    @MockBean
    private GoodsRepository goodsRepository;

    private Goods goods;
    private GoodsCategory goodsCategory;

    @Test
    public void shouldReturnAllListOfGoodsWhenFilterList_IsEmpty() {
        Mockito.when(goodsRepository.findAll()).thenReturn(GOODS_LIST);

        List<Goods> actualList = goodsService.findAll(new ArrayList<>());

        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
        Assert.assertEquals(GOODS_LIST.size(),actualList.size());
        Assert.assertTrue(GOODS_LIST.containsAll(actualList));
    }

    @Test
    public void shouldReturnAllListOfGoodsWithAccessibleGoodsFilter() {
        Mockito.when(goodsRepository.findAll()).thenReturn(GOODS_LIST);
        int oldCount = GOODS_LIST.get(0).getCount();
        GOODS_LIST.get(0).setCount(0);
        List<Goods> expectedList = GOODS_LIST.stream().filter(g->g.getCount()>0).collect(Collectors.toList());

        List<Goods> actualList = goodsService.findAll(Arrays.asList(new AccessibleGoodsFilterImpl(true)));

        Assert.assertEquals(expectedList.size(),actualList.size());
        Assert.assertTrue(expectedList.containsAll(actualList));
        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
        GOODS_LIST.get(0).setCount(oldCount);
    }

    @Test
    public void shouldReturnAllListOfGoodsWithInRangeOfCostGoodsFilter() {
        double lowCost = 0d;
        double highCost = 250d;
        Mockito.when(goodsRepository.findAll()).thenReturn(GOODS_LIST);
        List<Goods> expectedList = GOODS_LIST.stream()
                .filter(g->g.getCost()>lowCost && g.getCost()<highCost)
                .collect(Collectors.toList());

        List<Goods> actualList =
                goodsService.findAll(Arrays.asList(new InRangeOfCostGoodsFilterImpl(true, lowCost, highCost)));

        Assert.assertEquals(expectedList.size(),actualList.size());
        Assert.assertTrue(expectedList.containsAll(actualList));
        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnAllListOfGoodsWithNameLikeGoodsFilter() {
        String nameLike = "ir";
        Mockito.when(goodsRepository.findAll()).thenReturn(GOODS_LIST);
        List<Goods> expectedList = GOODS_LIST.stream()
                .filter(g->g.getName().contains(nameLike))
                .collect(Collectors.toList());

        List<Goods> actualList =
                goodsService.findAll(Arrays.asList(new NameLikeGoodsFilterImpl(true, nameLike)));

        Assert.assertEquals(expectedList.size(),actualList.size());
        Assert.assertTrue(expectedList.containsAll(actualList));
        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnAllListOfGoodsWithDescriptionLikeGoodsFilter() {
        String descriptionLike = "ir";
        Mockito.when(goodsRepository.findAll()).thenReturn(GOODS_LIST);
        List<Goods> expectedList = GOODS_LIST.stream()
                .filter(g->g.getDescription().contains(descriptionLike))
                .collect(Collectors.toList());

        List<Goods> actualList =
                goodsService.findAll(Arrays.asList(new DescriptionLikeGoodsFilterImpl(true, descriptionLike)));

        Assert.assertEquals(expectedList.size(),actualList.size());
        Assert.assertTrue(expectedList.containsAll(actualList));
        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnListOfGoodsByCategoryWhenFilterList_IsEmpty() {
        goodsCategory = new GoodsCategory();
        Mockito.when(goodsRepository.findAllByGoodsCategory(goodsCategory)).thenReturn(GOODS_LIST);

        List<Goods> actualList = goodsService.findAllByGoodsCategory(goodsCategory, new ArrayList<>());

        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByGoodsCategory(goodsCategory);
        Assert.assertEquals(GOODS_LIST.size(),actualList.size());
        Assert.assertTrue(GOODS_LIST.containsAll(actualList));

    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenSavingGoods() {
        goods = new Goods();
        goodsCategory = new GoodsCategory();

        goods.setId(RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(goodsRepository.existsById(goods.getId())).thenReturn(false);

        goodsService.save(goods);

        Mockito.verify(goodsRepository, Mockito.times(0)).save(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).existsById(goods.getId());
    }

    @Test
    public void shouldSaveGoodsToDatabase() {
        goods = new Goods();
        goodsCategory = new GoodsCategory();

        goods.setId(RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(goodsRepository.existsById(goods.getId())).thenReturn(true);

        goodsService.save(goods);

        Mockito.verify(goodsRepository, Mockito.times(1)).save(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).existsById(goods.getId());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenDeletingGoods() {
        goods = new Goods();
        goodsCategory = new GoodsCategory();

        goods.setId(RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(goodsRepository.existsById(goods.getId())).thenReturn(false);

        goodsService.delete(goods);

        Mockito.verify(goodsRepository, Mockito.times(0)).delete(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).existsById(goods.getId());
    }

    @Test
    public void shouldDeleteGoodsToDatabase() {
        goods = new Goods();
        goodsCategory = new GoodsCategory();

        goods.setId(RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(goodsRepository.existsById(goods.getId())).thenReturn(true);

        goodsService.delete(goods);

        Mockito.verify(goodsRepository, Mockito.times(1)).delete(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).existsById(goods.getId());
    }

    @Autowired
    public void setGoodsService(GoodsService goodsService) {
        this.goodsService = goodsService;
    }
}
