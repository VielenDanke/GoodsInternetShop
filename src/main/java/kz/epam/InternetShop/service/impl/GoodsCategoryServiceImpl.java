package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.repository.GoodsCategoryRepository;
import kz.epam.InternetShop.service.interfaces.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {
    private final GoodsCategoryRepository repository;

    @Autowired
    public GoodsCategoryServiceImpl(GoodsCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GoodsCategory> getAll() {
        return repository.findAll();
    }
}
