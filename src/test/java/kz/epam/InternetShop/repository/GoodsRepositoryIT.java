package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static kz.epam.InternetShop.util.GoodsCategoryDataTestUtil.GOODS_CATEGORIES;
import static kz.epam.InternetShop.util.GoodsDataTestUtil.GOODS_LIST;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryIT {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsCategoryRepository goodsCategoryRepository;

    @Before
    public void setUp() {
        goodsCategoryRepository.deleteAll();
        goodsRepository.deleteAll();

        GOODS_CATEGORIES.forEach(goodsCategory -> {
            goodsCategory.setId(null);
            goodsCategoryRepository.saveAndFlush(goodsCategory);
        });

        GOODS_LIST.forEach(goods -> {
            goods.setId(null);
            goodsRepository.saveAndFlush(goods);
        });
    }

    @Test
    @Rollback
    @Transactional
    public void shouldFindAllGoods() {
        List<Goods> goodsRepositoryAll = goodsRepository.findAll();
        Assert.assertEquals(goodsRepositoryAll, GOODS_LIST);
    }

    @Test
    @Rollback
    @Transactional
    public void shouldFindAllGoodsByCategory() {
        List<Goods> goodsByCategoryList = goodsRepository.findAllByGoodsCategory(GOODS_CATEGORIES.get(0));
        Assert.assertEquals(goodsByCategoryList.get(0), GOODS_LIST.get(0));
    }

    @Test
    @Rollback
    @Transactional
    public void shouldFindAllGoodsByNameLike() {
        List<Goods> goodsByNameLikeList = goodsRepository.findAllByNameLike(GOODS_LIST.get(0).getName());
        Assert.assertEquals(goodsByNameLikeList.get(0), GOODS_LIST.get(0));
    }
}
