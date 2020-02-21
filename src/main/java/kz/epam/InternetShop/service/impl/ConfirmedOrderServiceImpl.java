package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.OrderDetailsRepository;
import kz.epam.InternetShop.repository.OrderRepository;
import kz.epam.InternetShop.service.interfaces.ConfirmedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfirmedOrderServiceImpl implements ConfirmedOrderService {
    private static final int ONE_STATUS = 1;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public ConfirmedOrderServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Order> getAllOrders(User user) {
        return orderRepository.findAllByUserAndStatus(user, ONE_STATUS);
    }

    @Override
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<OrderDetails> getAllOrderDetails(Order order) {
        return orderDetailsRepository.findByOrder(order);
    }
}
