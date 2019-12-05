package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    List<OrderDetails> findByCount(Integer count);

    List<OrderDetails> findByCost(Double cost);

    List<OrderDetails> findByOrderId(Long orderId);

    List<OrderDetails> findByGoodsId(Long goodsId);

    List<OrderDetails> findByCountAndCost(Integer count, Double cost);
}
