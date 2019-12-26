package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static kz.epam.InternetShop.util.GoodsCategoryDataTestUtil.GOODS_CATEGORIES;
import static kz.epam.InternetShop.util.OrderDetailsDataTestUtil.ORDER1_DETAILS;
import static kz.epam.InternetShop.util.UserTestDataTestUtil.USERS;

public class OrderDataTestUtil {

    public static final List<Order> ORDER_LIST = Arrays.asList(
            Order.builder()
                    .creationDate(LocalDateTime.now())
                    .status(0)
                    .user(USERS.get(0))
                    .orderDetails(Arrays.asList(ORDER1_DETAILS.get(0)))
                    .build(),
            Order.builder()
                    .creationDate(LocalDateTime.now())
                    .status(0)
                    .user(USERS.get(1))
                    .orderDetails(Arrays.asList(ORDER1_DETAILS.get(1)))
                    .build(),
            Order.builder()
                    .creationDate(LocalDateTime.now())
                    .status(0)
                    .user(USERS.get(2))
                    .orderDetails(Arrays.asList(ORDER1_DETAILS.get(2)))
                    .build()
    );

    private static AtomicLong idCounter = new AtomicLong(1);

    public static Order getBasket() {
        Order basket = Order.builder()
                .id(idCounter.getAndIncrement())
                .creationDate(LocalDateTime.now())
                .status(TestFieldsUtil.ZERO_STATUS)
                .user(USERS.get(0)).build();
        Goods goods = Goods.builder()
                        .id(idCounter.getAndIncrement())
                        .name("name1")
                        .goodsCategory(GOODS_CATEGORIES.get(0))
                        .description("description1")
                        .cost(2500.00)
                        .count(500).build();
        List<OrderDetails> orderDetails = Arrays.asList(
                OrderDetails.builder()
                        .id(idCounter.getAndIncrement())
                        .count(goods.getCount())
                        .cost(goods.getCost())
                        .order(Order.builder().id(basket.getId()).build())
                        .goods(goods).build()
        );
        basket.setOrderDetails(orderDetails);
        return basket;
    }

    public static Order getNewOrder() {
        return Order.builder().id(idCounter.getAndIncrement()).build();
    }
}
