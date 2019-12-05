package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.GoodsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory, Long> {
}
