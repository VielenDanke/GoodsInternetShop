package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.GoodsCategory;

import java.util.Arrays;
import java.util.List;

public class GoodsCategoryDataTestUtil {

    public static final List<GoodsCategory> GOODS_CATEGORIES =  Arrays.asList(
                GoodsCategory.builder().name("testCategory2").build(),
                GoodsCategory.builder().name("testCategory3").build(),
                GoodsCategory.builder().name("testCategory4").build()
    );
}
