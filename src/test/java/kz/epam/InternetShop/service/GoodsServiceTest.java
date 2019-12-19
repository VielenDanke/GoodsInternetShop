package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.filter.GoodsFilter;
import kz.epam.InternetShop.model.filter.InRangeOfCostGoodsFilterImpl;
import kz.epam.InternetShop.repository.GoodsRepository;
import kz.epam.InternetShop.service.interfaces.GoodsService;
import kz.epam.InternetShop.util.TestFieldsUtil;
import kz.epam.InternetShop.util.exception.NotFoundException;
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

        goodsService.findAll(new ArrayList<>());

        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnAllListOfGoodsWhenFilterList_IsNotEmpty() {
        List<GoodsFilter> goodsFilterList = Arrays.asList(new InRangeOfCostGoodsFilterImpl(true, 200.22, 2000.22));
        Mockito.when(goodsRepository.findAll()).thenReturn(GOODS_LIST);

        goodsService.findAll(goodsFilterList);

        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnListOfGoodsByCategoryWhenFilterList_IsEmpty() {
        goodsCategory = new GoodsCategory();
        Mockito.when(goodsRepository.findAllByGoodsCategory(goodsCategory)).thenReturn(GOODS_LIST);

        goodsService.findAllByGoodsCategory(goodsCategory, new ArrayList<>());

        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByGoodsCategory(goodsCategory);
    }

    @Test
    public void shouldReturnListOfGoodsByCategoryWhenFilterList_IsNotEmpty() {
        goodsCategory = new GoodsCategory();
        List<GoodsFilter> goodsFilterList = Arrays.asList(new InRangeOfCostGoodsFilterImpl(true, 200.22, 2000.22));
        Mockito.when(goodsRepository.findAllByGoodsCategory(goodsCategory)).thenReturn(GOODS_LIST);

        goodsService.findAllByGoodsCategory(goodsCategory, goodsFilterList);

        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByGoodsCategory(goodsCategory);
    }

    @Test
    public void shouldReturnListOfGoodsByNameLikeWhenFilterList_IsEmpty() {
        Mockito.when(goodsRepository.findAllByNameLike(TestFieldsUtil.EXAMPLE_STRING)).thenReturn(GOODS_LIST);

        goodsService.findAllByNameLike(TestFieldsUtil.EXAMPLE_STRING, new ArrayList<>());

        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByNameLike(TestFieldsUtil.EXAMPLE_STRING);
    }

    @Test
    public void shouldReturnListOfGoodsByNameLikeWhenFilterList_IsNotEmpty() {
        List<GoodsFilter> goodsFilterList = Arrays.asList(new InRangeOfCostGoodsFilterImpl(true, 200.22, 2000.22));
        Mockito.when(goodsRepository.findAllByNameLike(TestFieldsUtil.EXAMPLE_STRING)).thenReturn(GOODS_LIST);

        goodsService.findAllByNameLike(TestFieldsUtil.EXAMPLE_STRING, goodsFilterList);

        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByNameLike(TestFieldsUtil.EXAMPLE_STRING);
    }

    @Test
    public void shouldReturnListOfGoodsByDescriptionLikeWhenFilterList_IsEmpty() {
        Mockito.when(goodsRepository.findAllByNameLike(TestFieldsUtil.EXAMPLE_STRING)).thenReturn(GOODS_LIST);

        goodsService.findAllByDescriptionLike(TestFieldsUtil.EXAMPLE_STRING, new ArrayList<>());

        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByDescriptionLike(TestFieldsUtil.EXAMPLE_STRING);
    }

    @Test
    public void shouldReturnListOfGoodsByDescriptionLikeWhenFilterList_IsNotEmpty() {
        List<GoodsFilter> goodsFilterList = Arrays.asList(new InRangeOfCostGoodsFilterImpl(true, 200.22, 2000.22));
        Mockito.when(goodsRepository.findAllByDescriptionLike(TestFieldsUtil.EXAMPLE_STRING)).thenReturn(GOODS_LIST);

        goodsService.findAllByDescriptionLike(TestFieldsUtil.EXAMPLE_STRING, goodsFilterList);

        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByDescriptionLike(TestFieldsUtil.EXAMPLE_STRING);
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
