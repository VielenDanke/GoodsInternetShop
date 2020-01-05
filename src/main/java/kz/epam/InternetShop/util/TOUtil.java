package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.TO.GoodsCategoryTO;
import kz.epam.InternetShop.model.TO.GoodsFiltersTO;
import kz.epam.InternetShop.model.TO.GoodsTO;
import kz.epam.InternetShop.model.TO.OrderDetailsTO;
import kz.epam.InternetShop.model.filter.GoodsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TOUtil {
    public static OrderDetailsTO asTO(OrderDetails orderDetails) {
        Goods goods = orderDetails.getGoods();
        return OrderDetailsTO.builder()
                .id(orderDetails.getId())
                .goodsId(goods.getId())
                .goodsName(goods.getName())
                .cost(orderDetails.getCost())
                .count(orderDetails.getCount())
                .goodsPhoto(goods.getPhotos().isEmpty() ? "" : goods.getPhotos().get(0))
                .orderId(orderDetails.getOrder().getId())
                .available(orderDetails.isAvailable())
                .build();
    }

    public static GoodsTO asTO(Goods goods) {
        return GoodsTO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .cost(goods.getCost())
                .count(goods.getCount())
                .description(goods.getDescription())
                .photos(goods.getPhotos().isEmpty() ? new ArrayList<String>() : goods.getPhotos())
                .build();
    }

    public static GoodsCategoryTO asTO(GoodsCategory goodsCategory) {
        return GoodsCategoryTO.builder()
                .id(goodsCategory.getId())
                .name(goodsCategory.getName())
                .build();
    }

    public static List<GoodsFilter> asList(GoodsFiltersTO goodsFiltersTO) {
        return Arrays.asList(goodsFiltersTO.getAccessibleGoodsFilter(),
                             goodsFiltersTO.getInRangeOfCostGoodsFilter(),
                             goodsFiltersTO.getNameLikeFilter(),
                             goodsFiltersTO.getDescriptionLikeFilter());
    }
}
