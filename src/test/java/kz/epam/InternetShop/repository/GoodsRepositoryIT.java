package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static kz.epam.InternetShop.testdata.GoodsCategoryTestData.getGoodsCategoryList;
import static kz.epam.InternetShop.testdata.GoodsTestData.getGoodsList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryIT {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsCategoryRepository goodsCategoryRepository;

    @Before
    public void setUp() {
        goodsRepository.deleteAll();
        goodsCategoryRepository.deleteAll();

        Iterable<Goods> goodsIterable = getGoodsList();

        getGoodsCategoryList().forEach(goodsCategory -> {
            goodsCategory.setGoods(Arrays.asList(goodsIterable.iterator().next()));
            goodsCategoryRepository.saveAndFlush(goodsCategory);
        });
    }

    @Test
    @Rollback
    @Transactional
    public void goodsRepositoryShouldNotBeEmpty() {
        assertThat(goodsRepository).isNotNull();
        assertThat(goodsCategoryRepository).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    public void findAllGoods() {
        List<Goods> goodsRepositoryAll = goodsRepository.findAll();

        Assert.assertEquals(goodsRepositoryAll, getGoodsList());
    }
}
