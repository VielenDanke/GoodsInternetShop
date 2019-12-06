package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.OrderDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static kz.epam.InternetShop.util.GoodsCategoryDataTestUtil.GOODS_CATEGORIES;
import static kz.epam.InternetShop.util.OrderDetailsDataTestUtil.*;
import static kz.epam.InternetShop.util.UserTestDataTestUtil.USERS;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailsRepositoryIT {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsCategoryRepository goodsCategoryRepository;


    @Before
    public void setUp() {

        goodsCategoryRepository.deleteAll();
        GOODS_CATEGORIES.forEach(category -> {
                    category.setId(null);
                    goodsCategoryRepository.save(category);
        });

        userRepository.deleteAll();
        USERS.forEach(user -> {
                    user.setId(null);
                    userRepository.save(user);
                }
        );

        goodsRepository.deleteAll();
        ORDER1_GOODS.forEach(g -> {
                    g.setId(null);
                    goodsRepository.saveAndFlush(g);
                }
        );
        ORDER2_GOODS.forEach(g -> {
                    g.setId(null);
                    goodsRepository.saveAndFlush(g);
                }
        );

        orderRepository.deleteAll();
        ORDER1.setId(null);
        orderRepository.save(ORDER1);
        ORDER2.setId(null);
        orderRepository.save(ORDER2);

        orderDetailsRepository.deleteAll();
        ORDER1_DETAILS.forEach(orderDetails -> {
                    orderDetails.setId(null);
                    orderDetailsRepository.save(orderDetails);
                }
        );
        ORDER2_DETAILS.forEach(orderDetails -> {
                    orderDetails.setId(null);
                    orderDetailsRepository.save(orderDetails);
                }
        );
    }

    @Test
    @Transactional
    @Rollback
    public void findByOrder() {
        List<OrderDetails> actualList = orderDetailsRepository.findByOrder(ORDER1);
        Assert.assertEquals(ORDER1_DETAILS.size(), actualList.size());
        Assert.assertEquals(true, actualList.containsAll(ORDER1_DETAILS));
    }
}