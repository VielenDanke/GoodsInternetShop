package kz.epam.InternetShop.model.TO;

import kz.epam.InternetShop.model.filter.AccessibleGoodsFilterImpl;
import kz.epam.InternetShop.model.filter.DescriptionLikeGoodsFilterImpl;
import kz.epam.InternetShop.model.filter.InRangeOfCostGoodsFilterImpl;
import kz.epam.InternetShop.model.filter.NameLikeGoodsFilterImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class GoodsFiltersTO {
    private AccessibleGoodsFilterImpl accessibleGoodsFilter;
    private InRangeOfCostGoodsFilterImpl inRangeOfCostGoodsFilter;
    private NameLikeGoodsFilterImpl nameLikeFilter;
    private DescriptionLikeGoodsFilterImpl descriptionLikeFilter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsFiltersTO that = (GoodsFiltersTO) o;

        if (accessibleGoodsFilter != null ? !accessibleGoodsFilter.equals(that.accessibleGoodsFilter) : that.accessibleGoodsFilter != null)
            return false;
        if (inRangeOfCostGoodsFilter != null ? !inRangeOfCostGoodsFilter.equals(that.inRangeOfCostGoodsFilter) : that.inRangeOfCostGoodsFilter != null)
            return false;
        if (nameLikeFilter != null ? !nameLikeFilter.equals(that.nameLikeFilter) : that.nameLikeFilter != null)
            return false;
        return descriptionLikeFilter != null ? descriptionLikeFilter.equals(that.descriptionLikeFilter) : that.descriptionLikeFilter == null;
    }

    @Override
    public int hashCode() {
        int result = accessibleGoodsFilter != null ? accessibleGoodsFilter.hashCode() : 0;
        result = 31 * result + (inRangeOfCostGoodsFilter != null ? inRangeOfCostGoodsFilter.hashCode() : 0);
        result = 31 * result + (nameLikeFilter != null ? nameLikeFilter.hashCode() : 0);
        result = 31 * result + (descriptionLikeFilter != null ? descriptionLikeFilter.hashCode() : 0);
        return result;
    }
}
