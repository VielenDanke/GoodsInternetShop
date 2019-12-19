package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.repository.GoodsRepository;
import kz.epam.InternetShop.util.ValidationUtil;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static kz.epam.InternetShop.util.GoodsDataTestUtil.GOODS_LIST;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class GoodsServiceTest {

    public final static String EXAMPLE_STRING = "example";

    private Goods goods;

    @MockBean
    private GoodsRepository goodsRepository;

    @Test
    public void saveGoods() {
        goods = new Goods();
        Mockito.doReturn(goods).when(goodsRepository).save(goods);
        Goods goodsAfterSave = goodsRepository.save(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).save(goods);
        Assert.assertEquals(goods, goodsAfterSave);
    }

    @Test
    public void deleteGoods() {
        goods = new Goods();
        goodsRepository.delete(goods);
        Mockito.verify(goodsRepository, Mockito.times(1)).delete(goods);
    }

    @Test(expected = NotFoundException.class)
    public void saveGoodsWhenGoodsDoesntExists() {
        goods = new Goods();
        goods.setId((long) 1);
        Mockito.doReturn(false).when(goodsRepository).existsById((long) 1);
        boolean found = goodsRepository.existsById((long) 1);
        ValidationUtil.checkNotFound(found, EXAMPLE_STRING);

        goodsRepository.save(goods);
        goodsRepository.delete(goods);

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
        Mockito.doReturn(GOODS_LIST).when(goodsRepository).findAllByNameLike(EXAMPLE_STRING);
        List<Goods> allGoodsByNameLike = goodsRepository.findAllByNameLike(EXAMPLE_STRING);
        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByNameLike(EXAMPLE_STRING);
        Assert.assertNotNull(allGoodsByNameLike);
    }

    @Test
    public void shouldReturnGoodsByDescriptionLike() {
        Mockito.doReturn(GOODS_LIST).when(goodsRepository).findAllByDescriptionLike(EXAMPLE_STRING);
        List<Goods> allGoodsByDescriptionLike = goodsRepository.findAllByDescriptionLike(EXAMPLE_STRING);
        Mockito.verify(goodsRepository, Mockito.times(1)).findAllByDescriptionLike(EXAMPLE_STRING);
        Assert.assertNotNull(allGoodsByDescriptionLike);
    }
}
