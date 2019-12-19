package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static kz.epam.InternetShop.util.OrderDataTestUtil.ORDER_LIST;
import static kz.epam.InternetShop.util.UserTestDataTestUtil.USERS;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class OrderRepositoryIT {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        USERS.forEach(user -> {
                    user.setId(null);
                    userRepository.save(user);
                }
        );

        ORDER_LIST.forEach(order -> {
            order.setId(null);
            orderRepository.saveAndFlush(order);
        });
    }

    @Test
    @Rollback
    @Transactional
    public void shouldReturnListOfOrder_whenFirstElementShouldBeEqualsWithFirstElementOfCurrentList() {
        List<Order> orders = orderRepository.findAllByUserAndStatus(USERS.get(0), 0);
        Assert.assertEquals(ORDER_LIST.get(0), orders.get(0));
    }
}