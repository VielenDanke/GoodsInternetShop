package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGoodsRepository {

    private final GoodsRepository goodsRepository;

    @Autowired
    public TestGoodsRepository(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Before
    public void setUp() {
        goodsRepository.deleteAll();
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
                        .categoryId((long) 100002)
                        .name("testFirst")
                        .description("testFirst")
                        .cost(100.12)
                        .count(3)
                        .photos(Arrays.asList("testFirst"))
                        .build(),
                Goods.builder()
                        .categoryId((long) 100003)
                        .name("testSecond")
                        .description("testSecond")
                        .cost(200.22)
                        .count(4)
                        .photos(Arrays.asList("testSecond"))
                        .build(),
                Goods.builder()
                        .categoryId((long) 100004)
                        .name("testThird")
                        .description("testThird")
                        .cost(300.31)
                        .count(5)
                        .photos(Arrays.asList("testThird"))
                        .build()
        );
        return goodsList;
    }
}
