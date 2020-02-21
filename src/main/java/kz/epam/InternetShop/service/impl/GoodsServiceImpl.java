package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.filter.GoodsFilter;
import kz.epam.InternetShop.repository.GoodsRepository;
import kz.epam.InternetShop.service.interfaces.GoodsService;
import kz.epam.InternetShop.util.ValidationUtil;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    private final GoodsRepository repository;

    @Autowired
    public GoodsServiceImpl(GoodsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "goods")
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Goods> findAll(List<GoodsFilter> filters) {
        return applyFilters(repository.findAll(), filters);
    }

    @Override
    @Cacheable(value = "goods")
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<Goods> findAllByGoodsCategory(GoodsCategory goodsCategory, List<GoodsFilter> filters) {
        return applyFilters(repository.findAllByGoodsCategory(goodsCategory), filters);
    }

    private List<Goods> applyFilters(List<Goods> goodsList, List<GoodsFilter> filters) {
        List<Goods> results = goodsList;
        for (GoodsFilter filter : filters) {
            if (filter.isActive()) {
                results = filter.apply(results);
            }
        }
        return results;
    }

    @Override
    @CachePut(value = "goods", key = "#goods.id", unless = "#result == null")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = NotFoundException.class)
    public Goods save(Goods goods) throws NotFoundException {
        if (goods.getId()!=null) {
            checkNotFound(goods);
        }
        return repository.save(goods);
    }

    @Override
    @CacheEvict(value = "goods")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = NotFoundException.class)
    public void delete(Goods goods) throws NotFoundException {
        checkNotFound(goods);
        repository.delete(goods);
    }

    @Override
    @Cacheable(value = "goods")
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public Goods get(long goodsId) {
        Goods goods =  repository.findById(goodsId).orElse(null);
        ValidationUtil.checkNotFound(goods!=null, "Item not found");
        return goods;
    }

    private void checkNotFound(Goods goods) throws NotFoundException {
        Long goodsId = goods.getId();
        if (goodsId!=null) {
            ValidationUtil.checkNotFound(repository.existsById(goodsId), "Item not found");
        }
    }
}
