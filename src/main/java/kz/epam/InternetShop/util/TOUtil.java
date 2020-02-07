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
import java.util.stream.Collectors;

public class TOUtil {
    public static OrderDetailsTO asTO(OrderDetails orderDetails) {
        Goods goods = orderDetails.getGoods();
        Order order = orderDetails.getOrder();
        List<String> photos = goods.getPhotos();
        return OrderDetailsTO.builder()
                .id(orderDetails.getId())
                .goodsId(goods==null ? null : goods.getId())
                .goodsName(goods==null ? null : goods.getName())
                .cost(orderDetails.getCost())
                .count(orderDetails.getCount())
                .goodsPhoto(photos==null || photos.isEmpty() ? "" : goods.getPhotos().get(0))
                .orderId(order==null ? null : order.getId())
                .available(orderDetails.isAvailable())
                .build();
    }
    public static OrderDetails createFrom(OrderDetailsTO orderDetailsTO) {
        Long goodsId = orderDetailsTO.getGoodsId();
        Goods goods = (goodsId==null)
                ? null
                : Goods.builder().id(goodsId).build();
        Long orderId = orderDetailsTO.getOrderId();
        Order order = (orderId==null)
                ? null
                : Order.builder().id(orderId).build();
        return OrderDetails.builder()
                .id(orderDetailsTO.getId())
                .goods(goods)
                .cost(orderDetailsTO.getCost())
                .count(orderDetailsTO.getCount())
                .order(order)
                .available(orderDetailsTO.isAvailable())
                .build();
    }

    public static List<OrderDetails> createListFrom(List<OrderDetailsTO> orderDetailsTO) {
        return orderDetailsTO.stream().map(odTO->createFrom(odTO)).collect(Collectors.toList());
    }

    public static GoodsTO asTO(Goods goods) {
        return GoodsTO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .cost(goods.getCost())
                .count(goods.getCount())
                .description(goods.getDescription())
                .photos(goods.getPhotos()==null || goods.getPhotos().isEmpty() ? new ArrayList<String>() : goods.getPhotos())
                .build();
    }

    public static Goods createFrom(GoodsTO goodsTO) {
        GoodsCategory goodsCategory = GoodsCategory.builder().id(goodsTO.getCategoryId()).build();
        Goods goods = Goods.builder()
                .id(goodsTO.getId())
                .name(goodsTO.getName())
                .cost(goodsTO.getCost())
                .count(goodsTO.getCount())
                .description(goodsTO.getDescription())
                .goodsCategory(goodsCategory)                 // photo
                .build();
        return goods;
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
