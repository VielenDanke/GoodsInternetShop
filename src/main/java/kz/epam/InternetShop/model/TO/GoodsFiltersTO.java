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
}
