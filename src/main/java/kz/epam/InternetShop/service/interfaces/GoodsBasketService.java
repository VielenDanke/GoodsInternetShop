package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.util.exception.NotAvailableGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;

import java.util.List;

public interface GoodsBasketService {
    void clear(User user);
    List<OrderDetails> getAllOrderDetails(User user);
    void setStatusToOne(User user) throws NotAvailableGoodsException;
    Order getBasket(User user);
    OrderDetails createOrderDetailsInBasket(OrderDetails orderDetails, User user);
    void updateCountOrderDetailsInBasket(List<OrderDetails> orderDetailsList, User user)
                                                        throws NotFoundException, NotAvailableGoodsException;
    void removeFromBasket(OrderDetails orderDetails, User user) throws NotFoundException;

}
