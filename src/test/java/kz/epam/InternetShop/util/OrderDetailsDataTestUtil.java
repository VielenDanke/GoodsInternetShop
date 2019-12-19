package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kz.epam.InternetShop.util.GoodsCategoryDataTestUtil.GOODS_CATEGORIES;
import static kz.epam.InternetShop.util.UserTestDataTestUtil.USERS;

public class OrderDetailsDataTestUtil {

    public static final Order ORDER1 = Order.builder()
            .creationDate(LocalDateTime.now())
            .status(TestFieldsUtil.STATUS_IS_ONE)
            .user(USERS.get(0)).build();

    public static final Order ORDER2 = Order.builder()
            .creationDate(LocalDateTime.now())
            .status(TestFieldsUtil.STATUS_IS_ONE)
            .user(USERS.get(0)).build();

    public static final List<Goods> ORDER1_GOODS = Arrays.asList(
            Goods.builder()
                    .name("name1")
                    .goodsCategory(GOODS_CATEGORIES.get(0))
                    .description("description1")
                    .cost(2500.00)
                    .count(500).build(),
            Goods.builder()
                    .name("name2")
                    .goodsCategory(GOODS_CATEGORIES.get(0))
                    .description("description2")
                    .cost(2500.00)
                    .count(500).build(),
            Goods.builder()
                    .name("name3")
                    .goodsCategory(GOODS_CATEGORIES.get(0))
                    .description("description3")
                    .cost(2500.00)
                    .count(500).build()
    );

    public static final List<Goods> ORDER2_GOODS = Arrays.asList(
            Goods.builder()
                    .name("name4")
                    .goodsCategory(GOODS_CATEGORIES.get(0))
                    .description("description4")
                    .cost(2500.00)
                    .count(500).build(),
            Goods.builder()
                    .name("name5")
                    .goodsCategory(GOODS_CATEGORIES.get(0))
                    .description("description5")
                    .cost(2500.00)
                    .count(500).build(),
            Goods.builder()
                    .name("name6")
                    .goodsCategory(GOODS_CATEGORIES.get(0))
                    .description("description6")
                    .cost(2500.00)
                    .count(500).build()
    );

    public static final List<OrderDetails> ORDER1_DETAILS = Arrays.asList(
            OrderDetails.builder()
                    .count(5)
                    .cost(5000d)
                    .order(ORDER1)
                    .goods(ORDER1_GOODS.get(0)).build(),
            OrderDetails.builder()
                    .count(5)
                    .cost(5000d)
                    .order(ORDER1)
                    .goods(ORDER1_GOODS.get(1)).build(),
            OrderDetails.builder()
                    .count(5)
                    .cost(5000d)
                    .order(ORDER1)
                    .goods(ORDER1_GOODS.get(2)).build()
    );

    public static final List<OrderDetails> ORDER2_DETAILS = Arrays.asList(
            OrderDetails.builder()
                    .count(5)
                    .cost(5000d)
                    .order(ORDER2)
                    .goods(ORDER2_GOODS.get(0)).build(),
            OrderDetails.builder()
                    .count(5)
                    .cost(5000d)
                    .order(ORDER2)
                    .goods(ORDER2_GOODS.get(1)).build(),
            OrderDetails.builder()
                    .count(5)
                    .cost(5000d)
                    .order(ORDER2)
                    .goods(ORDER2_GOODS.get(2)).build()
    );
}
