package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class GoodsUtil {
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
