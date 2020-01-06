package kz.epam.InternetShop.model.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class InRangeOfCostGoodsFilterImpl extends GoodsFilter {
    private Double lowCost;
    private Double highCost;

    @JsonCreator
    public InRangeOfCostGoodsFilterImpl(@JsonProperty("active") boolean active,
                                        @JsonProperty("lowCost") Double lowCost,
                                        @JsonProperty("highCost") Double highCost) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InRangeOfCostGoodsFilterImpl that = (InRangeOfCostGoodsFilterImpl) o;

        if (lowCost != null ? !lowCost.equals(that.lowCost) : that.lowCost != null) return false;
        return highCost != null ? highCost.equals(that.highCost) : that.highCost == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (lowCost != null ? lowCost.hashCode() : 0);
        result = 31 * result + (highCost != null ? highCost.hashCode() : 0);
        return result;
    }
}
