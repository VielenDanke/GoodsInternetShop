package kz.epam.InternetShop.model.filter;

import kz.epam.InternetShop.model.Goods;

import java.util.List;
import java.util.stream.Collectors;

public class AccessibleGoodsFilterImpl extends GoodsFilter{

    public AccessibleGoodsFilterImpl(boolean active) {
        super(active);
    }

    @Override
    public List<Goods> apply(List<Goods> goodsList) {
        return goodsList.stream().filter(g -> g.getCount()>0).collect(Collectors.toList());
    }
}
