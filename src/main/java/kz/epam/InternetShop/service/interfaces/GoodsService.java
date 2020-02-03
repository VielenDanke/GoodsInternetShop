package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.filter.GoodsFilter;
import kz.epam.InternetShop.service.CRUDInterface;

import java.util.List;

public interface GoodsService extends CRUDInterface<Goods> {
    List<Goods> findAll(List<GoodsFilter> filters);

    List<Goods> findAllByGoodsCategory(GoodsCategory goodsCategory, List<GoodsFilter> filters);

    Goods get(long goodsId);

}
