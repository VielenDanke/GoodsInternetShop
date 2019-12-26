package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.TO.GoodsTO;

public class TOUtil {
    public static GoodsTO asTO(Goods goods, Long orderId) {
        return GoodsTO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .cost(goods.getCost())
                .count(goods.getCount())
                .photos(goods.getPhotos().isEmpty() ? "" : goods.getPhotos().get(0))
                .orderId(orderId).build();
    }

    public static GoodsTO asTO(Goods goods) {
        return GoodsTO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .cost(goods.getCost())
                .count(goods.getCount())
                .photos(goods.getPhotos().isEmpty() ? "" : goods.getPhotos().get(0))
                .build();
    }

}
