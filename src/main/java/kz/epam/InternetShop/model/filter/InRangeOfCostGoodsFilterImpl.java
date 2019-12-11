package kz.epam.InternetShop.model.filter;

import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class InRangeOfCostGoodsFilterImpl extends GoodsFilter {
    private Double lowCost;
    private Double highCost;

    public InRangeOfCostGoodsFilterImpl(boolean active, Double lowCost, Double highCost) {
        super(active);
        this.lowCost = lowCost;
        this.highCost = highCost;
    }

    /**
     * get list of goods filtered by price between this.lowCost and this.highCost
     * @param goodsList list of goods by user price range
     * @return list of goods filtered by price between this.lowCost and this.highCost
     */
    @Override
    public List<Goods> apply(List<Goods> goodsList) {
        return goodsList.stream()
                .filter(g -> isBetween(g.getCost(), lowCost, highCost)).collect(Collectors.toList());
    }

    private boolean isBetween(Double cost, Double lowCost, Double highCost) {
        return cost>lowCost && cost<highCost;
    }
}