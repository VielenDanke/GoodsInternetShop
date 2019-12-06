package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.OrderDetails;

import java.util.Arrays;
import java.util.List;

import static kz.epam.InternetShop.util.GoodsCategoryDataTestUtil.GOODS_CATEGORIES;

public class GoodsDataTestUtil {

    public static final List<Goods> GOODS_LIST = Arrays.asList(
            Goods.builder()
                    .id(null)
                    .goodsCategory(GOODS_CATEGORIES.get(0))
                    .name("testFirst")
                    .description("testFirst")
                    .cost(100.12)
                    .count(3)
                    .photos(Arrays.asList("testFirst"))
                    .orderDetails(Arrays.asList(new OrderDetails()))
                    .build(),
            Goods.builder()
                    .id(null)
                    .goodsCategory(GOODS_CATEGORIES.get(1))
                    .name("testSecond")
                    .description("testSecond")
                    .cost(200.22)
                    .count(4)
                    .photos(Arrays.asList("testSecond"))
                    .orderDetails(Arrays.asList(new OrderDetails()))
                    .build(),
            Goods.builder()
                    .id(null)
                    .goodsCategory(GOODS_CATEGORIES.get(2))
                    .name("testThird")
                    .description("testThird")
                    .cost(300.31)
                    .count(5)
                    .photos(Arrays.asList("testThird"))
                    .orderDetails(Arrays.asList(new OrderDetails()))
                    .build()
    );
}
