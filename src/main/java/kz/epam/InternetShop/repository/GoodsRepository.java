package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    @Query("SELECT g FROM Goods g")
    List<Goods> findAll();

    List<Goods> findAllByCategoryId(Long categoryId);

    List<Goods> findAllByName(String name);
}
