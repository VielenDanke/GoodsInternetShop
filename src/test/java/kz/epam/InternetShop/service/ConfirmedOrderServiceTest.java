package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.OrderDetailsRepository;
import kz.epam.InternetShop.repository.OrderRepository;
import kz.epam.InternetShop.service.interfaces.ConfirmedOrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static kz.epam.InternetShop.util.OrderDataTestUtil.ORDER_LIST;
import static kz.epam.InternetShop.util.OrderDetailsDataTestUtil.ORDER1_DETAILS;
import static kz.epam.InternetShop.util.TestFieldsUtil.STATUS_IS_ONE;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class ConfirmedOrderServiceTest {

    private ConfirmedOrderService confirmedOrderService;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderDetailsRepository orderDetailsRepository;

    @Test
    public void shouldReturnAllOrdersByUserAndStatus() {
        User user = new User();

        Mockito.when(orderRepository.findAllByUserAndStatus(user, STATUS_IS_ONE)).thenReturn(ORDER_LIST);

        List<Order> orderList = confirmedOrderService.getAllOrders(user);

        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, STATUS_IS_ONE);
        Assert.assertEquals(orderList, ORDER_LIST);
    }

    @Test
    public void shouldReturnAllOrderDetailsByOrder() {
        Order order = new Order();

        Mockito.when(orderDetailsRepository.findByOrder(order)).thenReturn(ORDER1_DETAILS);

        List<OrderDetails> orderDetailsList = confirmedOrderService.getAllOrderDetails(order);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).findByOrder(order);
        Assert.assertEquals(orderDetailsList, ORDER1_DETAILS);
    }

    @Autowired
    public void setConfirmedOrderService(ConfirmedOrderService confirmedOrderService) {
        this.confirmedOrderService = confirmedOrderService;
    }
}
