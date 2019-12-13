package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static kz.epam.InternetShop.util.GoodsDataTestUtil.GOODS_LIST;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {

    private final static String GOODS_EXAMPLE_STRING = "toys";

    private GoodsService goodsService;
    private Goods goods;

    @MockBean
    private GoodsRepository goodsRepository;

    @Test
    public void saveGoods() {
        goods = new Goods();
        Mockito.doReturn(goods).when(goodsRepository).save(goods);
        Goods goodsAfterSave = goodsService.save(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).save(goods);
        Assert.assertEquals(goods, goodsAfterSave);
    }

    @Test
    public void deleteGoods() {
        goods = new Goods();
        goodsService.delete(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).delete(goods);
    }

    @Test(expected = NotFoundException.class)
    public void saveGoodsWhenGoodsDoesntExists() {
        goods = new Goods();
        goods.setId((long) 1);
        Mockito.doReturn(false).when(goodsRepository).existsById((long) 1);
        goodsService.save(goods);
        goodsService.delete(goods);
        Mockito.verify(goodsRepository, Mockito.times(0)).save(goods);
        Mockito.verify(goodsRepository, Mockito.times(0)).delete(goods);
    }

    @Test
    public void shouldReturnAllGoods() {
        Mockito.doReturn(GOODS_LIST).when(goodsRepository).findAll();
        List<Goods> allGoods = goodsRepository.findAll();
        Mockito.verify(goodsRepository, Mockito.times(1)).findAll();
        Assert.assertNotNull(allGoods);
    }

    @Test
    public void shouldReturnGoodsByGoodsCategory() {
        GoodsCategory goodsCategory = new GoodsCategory();
        Mockito.doReturn(GOODS_LIST).when(goodsRepository).findAllByGoodsCategory(goodsCategory);
        List<Goods> goodsList = goodsRepository.findAllByGoodsCategory(goodsCategory);
        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByGoodsCategory(goodsCategory);
        Assert.assertNotNull(goodsList);
    }

    @Test
    public void shouldReturnGoodsByGoodsNameLike() {
        Mockito.doReturn(GOODS_LIST).when(goodsRepository).findAllByNameLike(GOODS_EXAMPLE_STRING);
        List<Goods> allGoodsByNameLike = goodsRepository.findAllByNameLike(GOODS_EXAMPLE_STRING);
        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByNameLike(GOODS_EXAMPLE_STRING);
        Assert.assertNotNull(allGoodsByNameLike);
    }

    @Test
    public void shouldReturnGoodsByDescriptionLike() {
        Mockito.doReturn(GOODS_LIST).when(goodsRepository).findAllByDescriptionLike(GOODS_EXAMPLE_STRING);
        List<Goods> allGoodsByDescriptionLike = goodsRepository.findAllByDescriptionLike(GOODS_EXAMPLE_STRING);
        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByDescriptionLike(GOODS_EXAMPLE_STRING);
        Assert.assertNotNull(allGoodsByDescriptionLike);
    }

    @Autowired
    public void setGoodsService(GoodsService goodsService) {
        this.goodsService = goodsService;
    }
}
