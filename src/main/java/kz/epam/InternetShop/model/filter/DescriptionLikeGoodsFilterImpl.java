package kz.epam.InternetShop.model.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class DescriptionLikeGoodsFilterImpl extends GoodsFilter{
    private String descriptionLike;

    @Override
    public List<Goods> apply(List<Goods> goodsList) {
        return goodsList
                .stream()
                .filter(g -> g.getDescription().contains(descriptionLike))
                .collect(Collectors.toList());
    }

    public DescriptionLikeGoodsFilterImpl(@JsonProperty("active") boolean active,
                                          @JsonProperty("descriptionLike") String descriptionLike) {
        super(active);
        this.descriptionLike = descriptionLike;
    }
}
