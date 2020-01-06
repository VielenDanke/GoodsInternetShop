package kz.epam.InternetShop.model.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class AccessibleGoodsFilterImpl extends GoodsFilter{

    @Override
    public List<Goods> apply(List<Goods> goodsList) {
        return goodsList.stream().filter(g -> g.getCount()>0).collect(Collectors.toList());
    }

    @JsonCreator
    public AccessibleGoodsFilterImpl(@JsonProperty("active") boolean active) {
        super(active);
    }


}
