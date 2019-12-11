package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.service.CRUDInterface;
import kz.epam.InternetShop.util.exception.NotAccessibleGoodsException;
import kz.epam.InternetShop.util.exception.NotFoundException;

import java.util.List;

public interface GoodsBasketService extends CRUDInterface<OrderDetails> {
    void clear(User user);
    List<OrderDetails> getAllOrderDetails(User user);
    void setStatusToOne(User user) throws NotAccessibleGoodsException;
    Order getBasket(User user);
    void createOrderDetailsInBasket(Goods goods, User user, Integer count);
    void saveOrderDetailsInBasket(OrderDetails orderDetails, User user) throws NotFoundException;
    void removeFromBasket(OrderDetails orderDetails, User user) throws NotFoundException;
}
