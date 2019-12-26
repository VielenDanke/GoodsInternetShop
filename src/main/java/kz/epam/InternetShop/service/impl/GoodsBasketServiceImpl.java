package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.GoodsRepository;
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
    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsBasketServiceImpl(OrderRepository orderRepository,
                                  OrderDetailsRepository orderDetailsRepository, GoodsRepository goodsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.goodsRepository = goodsRepository;
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
    public void setStatusToOne(User user) throws NotAccessibleGoodsException {
        Order basket = getBasket(user);
        checkAccessibility(basket);
        basket.setStatus(ONE_STATUS);
        orderRepository.save(basket);
    }

    private void checkAccessibility(Order order) {
        List<OrderDetails> orderDetailsList = order.getOrderDetails();
        if (orderDetailsList!=null && orderDetailsList.size()>0 )
        order.getOrderDetails().forEach(orderDetails -> {
                Long goodsId = orderDetails.getGoods().getId();
                Integer availableCount = goodsRepository.findById(goodsId).get().getCount();
                if (orderDetails.getCount()>availableCount) {
                    throw new NotAccessibleGoodsException("Order contains unaccessible item.");
                }
        });
    }

    @Override
    public Order getBasket(User user) {
        Order result;
        List<Order> orders = orderRepository.findAllByUserAndStatus(user, ZERO_STATUS);
        if (orders.size()>0) {
            result = orders.get(0);
        } else {
            result = Order.builder().status(ZERO_STATUS).user(user).build();
            orderRepository.save(result);
        }
        return result;
    }

    @Override
    public OrderDetails createOrderDetailsInBasket(OrderDetails orderDetails, User user) {
        Order basket = getBasket(user);
        OrderDetails newOrderDetails = OrderDetails.builder()
                .order(basket)
                .goods(orderDetails.getGoods())
                .cost(orderDetails.getGoods().getCost())
                .count(orderDetails.getCount()).build();
        return orderDetailsRepository.save(newOrderDetails);
    }

    @Override
    public void saveOrderDetailsInBasket(List<OrderDetails> orderDetailsList, User user) throws NotFoundException{
        Order basket = getBasket(user);
        orderDetailsList.forEach(od -> {
            if (od.getCount() == 0) {
                removeFromBasket(od, user);
            } else {
                checkNotFound(od);
                od.setOrder(basket);
                orderDetailsRepository.save(od);
            }
        });
        checkAccessibility(basket);
    }

    @Override
    public void removeFromBasket(OrderDetails orderDetails, User user) throws NotFoundException{
        checkNotFound(orderDetails);
        Long basketId = getBasket(user).getId();
        ValidationUtil.checkNotFound(orderDetails.getOrder()!=null&&orderDetails.getOrder().getId().equals(basketId),
                                     "Item not found");
        orderDetailsRepository.delete(orderDetails);
    }

    private void checkNotFound(OrderDetails orderDetails) throws NotFoundException{
        Long orderDetailsId = orderDetails.getId();
        if (orderDetailsId!=null) {
            ValidationUtil.checkNotFound(orderDetailsRepository.existsById(orderDetailsId), "Item not found");
        }

    }
}
