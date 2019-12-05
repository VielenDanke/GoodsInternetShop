package kz.epam.InternetShop.testdata;

import kz.epam.InternetShop.model.Goods;

import java.util.Arrays;
import java.util.List;

public class GoodsTestData {

    public static List<Goods> getGoodsList() {

        return Arrays.asList(
                Goods.builder()
                        .name("testFirst")
                        .description("testFirst")
                        .cost(100.12)
                        .count(3)
                        .photos(Arrays.asList("testFirst"))
                        .build(),
                Goods.builder()
                        .name("testSecond")
                        .description("testSecond")
                        .cost(200.22)
                        .count(4)
                        .photos(Arrays.asList("testSecond"))
                        .build(),
                Goods.builder()
                        .name("testThird")
                        .description("testThird")
                        .cost(300.31)
                        .count(5)
                        .photos(Arrays.asList("testThird"))
                        .build()
        );
    }
}
