package kz.epam.InternetShop.model.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class NameLikeGoodsFilterImpl extends GoodsFilter{
    private String nameLike;

    @Override
    public List<Goods> apply(List<Goods> goodsList) {
        return goodsList
                .stream()
                .filter(g -> g.getName().contains(nameLike))
                .collect(Collectors.toList());
    }

    @JsonCreator
    public NameLikeGoodsFilterImpl(@JsonProperty("active")boolean active, @JsonProperty("nameLike")String nameLike) {
        super(active);
        this.nameLike = nameLike;
    }
}
