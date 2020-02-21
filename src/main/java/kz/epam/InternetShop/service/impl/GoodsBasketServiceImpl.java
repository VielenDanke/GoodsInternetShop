package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.GoodsRepository;
import kz.epam.InternetShop.repository.OrderDetailsRepository;
import kz.epam.InternetShop.repository.OrderRepository;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.util.ValidationUtil;
import kz.epam.InternetShop.util.exception.NotAvailableGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<OrderDetails> getAllOrderDetails(User user) {
        return checkAvailability(orderDetailsRepository.findByOrder(getBasket(user)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void clear(User user) {
        Order basket = getBasket(user);
        orderRepository.delete(basket);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = NotAvailableGoodsException.class)
    public void setStatusToOne(User user) throws NotAvailableGoodsException {
        Order basket = getBasket(user);
        checkAvailability(basket);
        basket.setStatus(ONE_STATUS);
        orderRepository.save(basket);
    }

    private void checkAvailability(Order order) {
        List<OrderDetails> orderDetailsList = order.getOrderDetails();
        if (orderDetailsList!=null && orderDetailsList.size()>0)
            checkAvailability(orderDetailsList).forEach(orderDetails -> {
                if (!orderDetails.isAvailable()) {
                    throw new NotAvailableGoodsException("Order contains unaccessible item.");
                }
        });
    }

    private List<OrderDetails> checkAvailability(List<OrderDetails> orderDetailsList) {
            orderDetailsList.forEach(orderDetails -> {
                Long goodsId = orderDetails.getGoods().getId();
                Integer availableCount = goodsRepository.findById(goodsId).get().getCount();
                if (orderDetails.getCount()>availableCount) {
                    orderDetails.setAvailable(false);
                } else {
                    orderDetails.setAvailable(true);
                }
            });
     return orderDetailsList;
    }
    
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderDetails createOrderDetailsInBasket(OrderDetails orderDetails, User user) {
        OrderDetails targetOrderDetails;
        Order basket = getBasket(user);
        OrderDetails existingOrderDetails = orderDetailsRepository
                .findByOrderAndGoodsAndCost(basket, orderDetails.getGoods(), orderDetails.getCost());
        if (existingOrderDetails!=null) {
            existingOrderDetails.setCount(existingOrderDetails.getCount() + orderDetails.getCount());
            targetOrderDetails = existingOrderDetails;
        } else {
            targetOrderDetails = OrderDetails.builder()
                    .order(basket)
                    .goods(orderDetails.getGoods())
                    .cost(orderDetails.getCost())
                    .count(orderDetails.getCount()).build();
        }
        return orderDetailsRepository.save(targetOrderDetails);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = NotFoundException.class)
    public void updateCountOrderDetailsInBasket(List<OrderDetails> orderDetailsList, User user) throws NotFoundException{
        orderDetailsList.forEach(od -> {
            if (od.getCount() == 0) {
                OrderDetails orderDetails = orderDetailsRepository.findById(od.getId()).orElse(null);
                if (orderDetails!=null) {
                    orderDetailsRepository.delete(orderDetails);
                }
            } else {
                Long basketId = getBasket(user).getId();
                checkNotFound(od, basketId);
                orderDetailsRepository.updateCount(od.getId(), od.getCount());
            }
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = NotFoundException.class)
    public void removeFromBasket(OrderDetails orderDetails, User user) throws NotFoundException{
        Long basketId = getBasket(user).getId();
        checkNotFound(orderDetails, basketId);
        orderDetailsRepository.delete(orderDetails);
    }

    private void checkNotFound(OrderDetails orderDetails, Long basketId) throws NotFoundException{
        OrderDetails targetOrderDetails = orderDetailsRepository.findById(orderDetails.getId()).orElse(null);
        ValidationUtil.checkNotFound(targetOrderDetails!=null && targetOrderDetails.getOrder().getId().equals(basketId),
                "Item not found");
    }
}
