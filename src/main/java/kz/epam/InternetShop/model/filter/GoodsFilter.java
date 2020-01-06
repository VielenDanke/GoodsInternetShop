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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsFilter that = (GoodsFilter) o;

        return active == that.active;
    }

    @Override
    public int hashCode() {
        return (active ? 1 : 0);
    }
}
