package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGoodsRepository {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsCategoryRepository goodsCategoryRepository;

    @Before
    public void setUp() {
        goodsRepository.deleteAll();
        goodsCategoryRepository.deleteAll();
        getGoodsCategoryList().forEach(goodsCategoryRepository::save);
        getGoodsList().forEach(goodsRepository::save);
    }

    @Test
    @Rollback
    @Transactional
    public void goodsRepositoryShouldNotBeEmpty() {
        assertThat(goodsRepository).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    public void findAllGoods() {
        List<Goods> goodsRepositoryAll = goodsRepository.findAll();

        Assert.assertEquals(goodsRepositoryAll, getGoodsList());
    }

    private List<Goods> getGoodsList() {

        List<Goods> goodsList = Arrays.asList(
                Goods.builder()
                        .goodsCategory(GoodsCategory.builder().id((long) 100002).build())
                        .name("testFirst")
                        .description("testFirst")
                        .cost(100.12)
                        .count(3)
                        .photos(Arrays.asList("testFirst"))
                        .build(),
                Goods.builder()
                        .goodsCategory(GoodsCategory.builder().id((long) 100003).build())
                        .name("testSecond")
                        .description("testSecond")
                        .cost(200.22)
                        .count(4)
                        .photos(Arrays.asList("testSecond"))
                        .build(),
                Goods.builder()
                        .goodsCategory(GoodsCategory.builder().id((long) 100004).build())
                        .name("testThird")
                        .description("testThird")
                        .cost(300.31)
                        .count(5)
                        .photos(Arrays.asList("testThird"))
                        .build()
        );
        return goodsList;
    }

    private List<GoodsCategory> getGoodsCategoryList() {

        List<GoodsCategory> goodsCategoryList = Arrays.asList(
                GoodsCategory.builder().id((long) 100002).name("testCategory2").build(),
                GoodsCategory.builder().id((long) 100003).name("testCategory3").build(),
                GoodsCategory.builder().id((long) 100004).name("testCategory4").build()
        );
        return goodsCategoryList;
    }
}
