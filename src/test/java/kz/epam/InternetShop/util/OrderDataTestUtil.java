package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Order;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
}
