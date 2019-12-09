package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    @Query("SELECT g FROM Goods g")
    List<Goods> findAll();

    List<Goods> findAllByGoodsCategory(GoodsCategory goodsCategory);

    List<Goods> findAllByNameLike(String nameLike);

    List<Goods> findAllByDescriptionLike(String descriptionLike);
}
