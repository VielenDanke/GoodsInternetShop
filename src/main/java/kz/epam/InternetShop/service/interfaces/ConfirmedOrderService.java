package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;

import java.util.List;

public interface ConfirmedOrderService {
    List<Order> getAllOrders(User user);
    List<OrderDetails> getAllOrderDetails(Order order);
}
