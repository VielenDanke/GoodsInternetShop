package kz.epam.InternetShop.model.filter;

import kz.epam.InternetShop.model.Goods;

import java.util.List;

public abstract class GoodsFilter {
    private boolean active;

    public GoodsFilter(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    };

    public abstract List<Goods> apply(List<Goods> goodsList);
}
