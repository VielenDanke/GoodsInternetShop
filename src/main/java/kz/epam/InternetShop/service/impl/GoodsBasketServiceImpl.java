package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.OrderDetailsRepository;
import kz.epam.InternetShop.repository.OrderRepository;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.util.ValidationUtil;
import kz.epam.InternetShop.util.exception.NotAccessibleGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsBasketServiceImpl implements GoodsBasketService {
    private static final int ZERO_STATUS = 0;
    private static final int ONE_STATUS = 1;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public GoodsBasketServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public List<OrderDetails> getAllOrderDetails(User user) {
        return orderDetailsRepository.findByOrder(getBasket(user));
    }

    @Override
    public void clear(User user) {
        Order basket = getBasket(user);
        orderRepository.delete(basket);
    }

    @Override
    public OrderDetails save(OrderDetails orderDetails) {
        return orderDetailsRepository.save(orderDetails);
    }

    @Override
    public void delete(OrderDetails orderDetails) {
        orderDetailsRepository.delete(orderDetails);
    }

    @Override
    public void setStatusToOne(User user) throws NotAccessibleGoodsException {
        Order basket = getBasket(user);
        basket.setOrderDetails(getAllOrderDetails(user));
        checkAccessibility(basket);
        basket.setStatus(ONE_STATUS);
        orderRepository.save(basket);
    }

    private void checkAccessibility(Order order) {
        order.getOrderDetails().forEach(orderDetails -> {
                if (orderDetails.getCount()<1) {
                    throw new NotAccessibleGoodsException("Order contains unaccessible item.");
                }
        });
    }

    @Override
    public Order getBasket(User user) {
        Order result;
        List<Order> orders = orderRepository.findAllByUserAndStatus(user, ZERO_STATUS );
        if (orders.size()>0) {
            result = orders.get(0);
        } else {
            result = Order.builder().status(ZERO_STATUS).user(user).build();
            orderRepository.save(result);
        }
        return result;
    }

    @Override
    public void createOrderDetailsInBasket(Goods goods, User user, Integer count) {
        Order basket = getBasket(user);
        OrderDetails newOrderDetails = OrderDetails.builder()
                .order(basket)
                .goods(goods)
                .cost(goods.getCost())
                .count(count).build();
        orderDetailsRepository.save(newOrderDetails);
    }

    @Override
    public void saveOrderDetailsInBasket(OrderDetails orderDetails, User user) throws NotFoundException{
        checkNotFound(orderDetails);
        orderDetails.setOrder(getBasket(user));
        orderDetailsRepository.save(orderDetails);
    }

    @Override
    public void removeFromBasket(OrderDetails orderDetails, User user) throws NotFoundException{
        checkNotFound(orderDetails);
        orderDetails.setOrder(getBasket(user));
        orderDetailsRepository.delete(orderDetails);
    }

    private void checkNotFound(OrderDetails orderDetails) throws NotFoundException{
        Long orderDetailsId = orderDetails.getId();
        if (orderDetailsId!=null) {
            ValidationUtil.checkNotFound(orderDetailsRepository.existsById(orderDetailsId), "Item not found");
        }
    }
}
