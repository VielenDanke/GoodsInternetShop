package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class GoodsUtil {

    /**
     * This method take in the Users's list of goods by price range and filtered it
     * by price range
     * @param goods list of goods by user price range
     * @param lowCost min price range
     * @param highCost max price range
     * @return list of goods filtered by price between minValue and maxValue
     */
    public List<Goods> getGoodsInRangeOfCost (List<Goods> goods, Double lowCost, Double highCost) {
        return goods.stream().filter(g -> isBetween(g.getCost(), lowCost, highCost)).collect(Collectors.toList());
    }

    private boolean isBetween(Double cost, Double lowCost, Double highCost) {
        return cost>lowCost && cost<highCost;
    }

    public List<Goods> getAccessibleGoods(List<Goods> goods) {
        return goods.stream().filter(g -> g.getCount()>0).collect(Collectors.toList());
    }

}
