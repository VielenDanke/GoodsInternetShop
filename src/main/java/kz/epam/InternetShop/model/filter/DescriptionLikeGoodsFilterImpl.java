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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DescriptionLikeGoodsFilterImpl that = (DescriptionLikeGoodsFilterImpl) o;

        return descriptionLike != null ? descriptionLike.equals(that.descriptionLike) : that.descriptionLike == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (descriptionLike != null ? descriptionLike.hashCode() : 0);
        return result;
    }
}
