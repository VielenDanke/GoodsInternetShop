package kz.epam.InternetShop.testdata;

import kz.epam.InternetShop.model.GoodsCategory;

import java.util.Arrays;
import java.util.List;

public class GoodsCategoryTestData {

    public static List<GoodsCategory> getGoodsCategoryList() {

        return Arrays.asList(
                GoodsCategory.builder().name("testCategory2").build(),
                GoodsCategory.builder().name("testCategory3").build(),
                GoodsCategory.builder().name("testCategory4").build()
        );
    }
}
