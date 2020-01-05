package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.GoodsRepository;
import kz.epam.InternetShop.repository.OrderDetailsRepository;
import kz.epam.InternetShop.repository.OrderRepository;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.util.exception.NotAvailableGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kz.epam.InternetShop.util.OrderDataTestUtil.getBasket;
import static kz.epam.InternetShop.util.OrderDataTestUtil.getNewOrder;
import static kz.epam.InternetShop.util.TestFieldsUtil.STATUS_IS_ONE;
import static kz.epam.InternetShop.util.TestFieldsUtil.ZERO_STATUS;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class GoodsBasketServiceTest {

    private GoodsBasketService goodsBasketService;
    @MockBean
    private OrderDetailsRepository orderDetailsRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private GoodsRepository goodsRepository;

    private Order basket;
    private User user;
    private OrderDetails orderDetails;
    private Goods goods;

    @Before
    public void setUp() {
        this.basket = getBasket();
        this.orderDetails = basket.getOrderDetails().get(0);
        this.goods = orderDetails.getGoods();
        this.user = basket.getUser();
        Mockito.when(orderRepository.findAllByUserAndStatus(user, ZERO_STATUS)).thenReturn(Arrays.asList(basket));
        Mockito.when(goodsRepository.findById(goods.getId())).thenReturn(Optional.of(goods));
    }

    @Test
    public void shouldGetAllOrderDetails() {
        Mockito.when(orderDetailsRepository.findByOrder(basket)).thenReturn(basket.getOrderDetails());
        List<OrderDetails> actualList = goodsBasketService.getAllOrderDetails(user);
        List<OrderDetails> expectedList = basket.getOrderDetails();

        Mockito.verify(orderDetailsRepository, Mockito.times(1)).findByOrder(basket);
        Assert.assertEquals(expectedList.size(), actualList.size());
        Assert.assertTrue(expectedList.containsAll(actualList));
    }

    @Test
    public void shouldClear() {
        Mockito.when(orderDetailsRepository.findByOrder(basket)).thenReturn(basket.getOrderDetails());
        goodsBasketService.clear(user);
        Mockito.verify(orderRepository, Mockito.times(1)).delete(basket);
    }

    @Test
    public void shouldSetStatusToOne() {
        goodsBasketService.setStatusToOne(user);
        Assert.assertEquals(STATUS_IS_ONE, basket.getStatus());
        Mockito.verify(orderRepository, Mockito.times(1)).save(basket);
    }

    @Test(expected = NotAvailableGoodsException.class)
    public void shouldThrowNotAccessibleGoodsExceptionWhenSetStatusToOne() {
        orderDetails.setCount(goods.getCount() + 5);
        goodsBasketService.setStatusToOne(user);
        Mockito.verify(orderRepository, Mockito.times(1)).save(basket);
    }

    @Test
    public void shouldCreateOrderDetailsInBasketWhenOrderListSize_IsNotEmpty() {
        OrderDetails savedOrderDetails = OrderDetails.builder()
                .order(basket)
                .goods(orderDetails.getGoods())
                .cost(orderDetails.getGoods().getCost())
                .count(orderDetails.getCount()).build();
        Mockito.when(orderDetailsRepository.save(savedOrderDetails)).thenReturn(savedOrderDetails);

        OrderDetails actualOrderDetails = goodsBasketService.createOrderDetailsInBasket(orderDetails, user);

        Assert.assertEquals(basket, actualOrderDetails.getOrder());
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).save(savedOrderDetails);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenSavingOrderDetailsInBasket() {
        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(false);
        goodsBasketService.updateCountOrderDetailsInBasket(Arrays.asList(orderDetails), user);
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).existsById(orderDetails.getId());
    }

    @Test
    public void shouldRunDeleteWhenSavingOrderDetailsInBasket() {
        // row of orderDetails with zero count must delete from the basket
        orderDetails.setCount(0);
        Mockito.when(orderDetailsRepository.findById(orderDetails.getId())).thenReturn(Optional.of(orderDetails));

        goodsBasketService.updateCountOrderDetailsInBasket(Arrays.asList(orderDetails), user);
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).delete(orderDetails);
        Mockito.verify(orderDetailsRepository, Mockito.times(0)).save(orderDetails);
    }

    @Test
    public void shouldUpdateOrderForOrderDetailsWhenSavingOrderDetailsInBasket() {
        // after saving newOrderDetails must be in basket (newOrderDetails.getOrder = basket)
        OrderDetails newOrderDetails = OrderDetails.builder()
                .id(orderDetails.getId())
                .count(goods.getCount())
                .cost(goods.getCost())
                .order(null)
                .goods(goods).build();
        Mockito.when(orderDetailsRepository.findById(newOrderDetails.getId())).thenReturn(Optional.of(orderDetails));

        goodsBasketService.updateCountOrderDetailsInBasket(Arrays.asList(newOrderDetails), user);
        Mockito.verify(orderDetailsRepository, Mockito.times(0)).delete(orderDetails);
        Mockito.verify(orderDetailsRepository, Mockito.times(1))
                .updateCount(orderDetails.getId(), orderDetails.getCount());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenDeletingOrderDetails() {
        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(false);
        goodsBasketService.removeFromBasket(orderDetails, user);

        Mockito.verify(orderDetailsRepository, Mockito.times(0)).delete(orderDetails);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenDeletingOrderDetailsWithNullOrder() {
        orderDetails.setOrder(null);
        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(true);

        goodsBasketService.removeFromBasket(orderDetails, user);
        Mockito.verify(orderDetailsRepository, Mockito.times(0)).delete(orderDetails);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWhenDeletingOrderDetailsWithStrangeOrder() {
        orderDetails.setOrder(getNewOrder());
        Mockito.when(orderDetailsRepository.existsById(orderDetails.getId())).thenReturn(true);

        goodsBasketService.removeFromBasket(orderDetails, user);
        Mockito.verify(orderDetailsRepository, Mockito.times(0)).delete(orderDetails);
    }

    @Test
    public void shouldDeleteOrderDetails() {
        Mockito.when(orderDetailsRepository.findById(orderDetails.getId())).thenReturn(Optional.of(orderDetails));
        goodsBasketService.removeFromBasket(orderDetails, user);
        Mockito.verify(orderDetailsRepository, Mockito.times(1)).delete(orderDetails);
    }

    @Autowired
    public void setGoodsBasketService(GoodsBasketService goodsBasketService) {
        this.goodsBasketService = goodsBasketService;
    }
}