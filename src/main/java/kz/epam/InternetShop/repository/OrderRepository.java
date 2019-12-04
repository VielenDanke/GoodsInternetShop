package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserIdAndStatus(long userId, int status);

    @Override
    Order save(Order order);

    @Override
    void delete(Order order);
}
