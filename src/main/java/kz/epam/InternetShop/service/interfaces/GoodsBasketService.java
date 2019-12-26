package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.util.exception.NotAccessibleGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;

import java.util.List;

public interface GoodsBasketService {
    void clear(User user);
    List<OrderDetails> getAllOrderDetails(User user);
    void setStatusToOne(User user) throws NotAccessibleGoodsException;
    Order getBasket(User user);
    OrderDetails createOrderDetailsInBasket(OrderDetails orderDetails, User user);
    void saveOrderDetailsInBasket(List<OrderDetails> orderDetailsList, User user)
                                                        throws NotFoundException, NotAccessibleGoodsException;
    void removeFromBasket(OrderDetails orderDetails, User user) throws NotFoundException;

}
