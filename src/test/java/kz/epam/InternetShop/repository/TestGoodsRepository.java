package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGoodsRepository {

    private final GoodsRepository goodsRepository;

    @Autowired
    public TestGoodsRepository(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    private Goods goods;

    @Before
    public void setup() {
        goods = Goods.builder()
                .categoryId((long) 100002)
                .name("test")
                .description("test")
                .cost(200.22)
                .count(5)
                .photos(Collections.singletonList("test"))
                .build();

        goodsRepository.save(goods);
    }

    @After
    public void destroy() {
        goodsRepository.delete(goods);
    }
}
