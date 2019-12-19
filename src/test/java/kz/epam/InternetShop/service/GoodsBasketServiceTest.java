package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.OrderDetailsRepository;
import kz.epam.InternetShop.repository.OrderRepository;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.util.TestFieldsUtil;
import kz.epam.InternetShop.util.exception.NotAccessibleGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kz.epam.InternetShop.util.OrderDataTestUtil.ORDER_LIST;
import static kz.epam.InternetShop.util.OrderDetailsDataTestUtil.ORDER1_DETAILS;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class GoodsBasketServiceTest {

    private GoodsBasketService goodsBasketService;
    @MockBean
    private OrderDetailsRepository orderDetailsRepository;
    @MockBean
    private OrderRepository orderRepository;

    private User user;
    private OrderDetails orderDetails;
    private Goods goods;

    @Test
    public void shouldReturnListOfGoodsByOrderWhenOrderListSize_IsEmpty() {
        user = new User();
        Mockito.doReturn(new ArrayList<Order>()).when(orderRepository).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        List<OrderDetails> orderDetailsList = goodsBasketService.getAllOrderDetails(user);

        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).findByOrder(ArgumentMatchers.any(Order.class));
        Assert.assertNotNull(orderDetailsList);
    }

    @Test
    public void shouldReturnListOfGoodsByOrderWhenOrderListSize_IsNotEmpty() {
        user = new User();
        Mockito.doReturn(ORDER_LIST).when(orderRepository).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        List<OrderDetails> orderDetailsList = goodsBasketService.getAllOrderDetails(user);

        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(0)).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).findByOrder(ArgumentMatchers.any(Order.class));
        Assert.assertNotNull(orderDetailsList);
    }

    @Test
    public void shouldClearTheBasketWhenOrderList_IsNotEmpty() {
        user = new User();
        Mockito.doReturn(new ArrayList<Order>()).when(orderRepository).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        goodsBasketService.clear(user);

        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(orderRepository, Mockito.times(1)).delete(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void shouldClearTheBasketWhenOrderList_IsEmpty() {
        user = new User();
        Mockito.doReturn(ORDER_LIST).when(orderRepository).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        goodsBasketService.clear(user);

        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(0)).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(orderRepository, Mockito.times(1)).delete(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void shouldSaveOrderDetails() {
        orderDetails = new OrderDetails();
        Mockito.doReturn(orderDetails).when(orderDetailsRepository).save(orderDetails);
        OrderDetails afterSaving = goodsBasketService.save(orderDetails);

        Assert.assertEquals(afterSaving, orderDetails);
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).save(orderDetails);
    }

    @Test
    public void shouldDeleteOrderDetails() {
        orderDetails = new OrderDetails();
        goodsBasketService.delete(orderDetails);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).delete(orderDetails);
    }

    @Test
    public void shouldSetStatusToOneWhetOrderListSize_IsNotEmpty_And_checkAccessibility_IsNotThrowException() {
        user = new User();
        Mockito.doReturn(ORDER_LIST).when(orderRepository).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.doReturn(ORDER1_DETAILS).when(orderDetailsRepository).findByOrder(ORDER_LIST.get(0));

        goodsBasketService.setStatusToOne(user);

        Assert.assertEquals(ORDER_LIST.get(0).getStatus(), Integer.valueOf(1));
        Mockito.verify(orderRepository, Mockito.times(2)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).findByOrder(ArgumentMatchers.any(Order.class));
        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any(Order.class));
    }

    @Test(expected = NotAccessibleGoodsException.class)
    public void shouldThrowNotAccessibleGoodsException_OrderListSizeIsNotEmpty() {
        user = new User();
        orderDetails = new OrderDetails();
        orderDetails.setCount(0);
        Mockito.when(orderRepository.findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS)).thenReturn(new ArrayList<>());
        Mockito.when(orderDetailsRepository.findByOrder(ArgumentMatchers.any(Order.class))).thenReturn(Arrays.asList(orderDetails));

        goodsBasketService.setStatusToOne(user);

        Mockito.verify(orderRepository, Mockito.times(2)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(2)).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void shouldCreateOrderDetailsInBasketWhenOrderListSize_IsNotEmpty() {
        user = new User();
        goods = new Goods();
        Integer count = TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextInt();

        goods.setCost(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextDouble());

        Mockito.when(orderRepository.findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS)).thenReturn(ORDER_LIST);

        goodsBasketService.createOrderDetailsInBasket(goods, user, count);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).save(ArgumentMatchers.any(OrderDetails.class));
        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
    }

    @Test
    public void shouldCreateOrderDetailsInBasketWhenOrderListSize_IsEmpty() {
        user = new User();
        goods = new Goods();
        Integer count = TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextInt();

        goods.setCost(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextDouble());

        Mockito.when(orderRepository.findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS)).thenReturn(new ArrayList<>());

        goodsBasketService.createOrderDetailsInBasket(goods, user, count);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).save(ArgumentMatchers.any(OrderDetails.class));
        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any(Order.class));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenSavingOrderDetailsInBasket() {
        user = new User();
        orderDetails = new OrderDetails();
        orderDetails.setId(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(false);

        goodsBasketService.saveOrderDetailsInBasket(orderDetails, user);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).existsById(orderDetails.getId());
    }

    @Test
    public void shouldSaveOrderDetailsInBasketWhenOrderListSize_IsNotEmpty() {
        user = new User();
        orderDetails = new OrderDetails();
        orderDetails.setId(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(true);
        Mockito.when(orderRepository.findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS)).thenReturn(ORDER_LIST);

        goodsBasketService.saveOrderDetailsInBasket(orderDetails, user);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).existsById(orderDetails.getId());
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).save(orderDetails);
        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(0)).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void shouldSaveOrderDetailsInBasketWhenOrderListSize_IsEmpty() {
        user = new User();
        orderDetails = new OrderDetails();
        orderDetails.setId(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(true);
        Mockito.when(orderRepository.findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS)).thenReturn(new ArrayList<>());

        goodsBasketService.saveOrderDetailsInBasket(orderDetails, user);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).existsById(orderDetails.getId());
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).save(orderDetails);
        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any(Order.class));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenDeletingOrderDetails() {
        user = new User();
        orderDetails = new OrderDetails();
        orderDetails.setId(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(false);

        goodsBasketService.removeFromBasket(orderDetails, user);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).existsById(orderDetails.getId());
    }

    @Test
    public void shouldDeleteOrderDetailsWhenOrderList_IsNotEmpty() {
        user = new User();
        orderDetails = new OrderDetails();
        orderDetails.setId(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(true);
        Mockito.when(orderRepository.findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS)).thenReturn(ORDER_LIST);

        goodsBasketService.removeFromBasket(orderDetails, user);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).existsById(orderDetails.getId());
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).delete(orderDetails);
        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(0)).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void shouldDeleteOrderDetailsWhenOrderList_IsEmpty() {
        user = new User();
        orderDetails = new OrderDetails();
        orderDetails.setId(TestFieldsUtil.RANDOM_NUMBER_RANGE_999.nextLong());

        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(true);
        Mockito.when(orderRepository.findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS)).thenReturn(new ArrayList<>());

        goodsBasketService.removeFromBasket(orderDetails, user);

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).existsById(orderDetails.getId());
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).delete(orderDetails);
        Mockito.verify(orderRepository, Mockito.times(1)).findAllByUserAndStatus(user, TestFieldsUtil.ZERO_STATUS);
        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any(Order.class));
    }

    @Autowired
    public void setGoodsBasketService(GoodsBasketService goodsBasketService) {
        this.goodsBasketService = goodsBasketService;
    }
}