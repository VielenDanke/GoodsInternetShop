package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    List<OrderDetails> findByOrder(Order order);
    OrderDetails findByOrderAndGoodsAndCost(Order order, Goods goods, Double cost);

    @Modifying
    @Transactional
    @Query("UPDATE OrderDetails od SET od.count=:count WHERE od.id=:orderDetailsId")
    void updateCount(@Param("orderDetailsId") Long orderDetailsId, @Param("count") Integer count);
}
