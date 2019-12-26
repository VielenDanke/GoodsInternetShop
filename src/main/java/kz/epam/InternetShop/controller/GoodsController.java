package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.TO.GoodsTO;
import kz.epam.InternetShop.model.filter.GoodsFilter;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.service.interfaces.GoodsCategoryService;
import kz.epam.InternetShop.service.interfaces.GoodsService;
import kz.epam.InternetShop.util.TOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static kz.epam.InternetShop.util.TOUtil.asTO;

@RestController
@RequestMapping(value = "/goods", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoodsController {

    private final GoodsCategoryService categoryService;

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsCategoryService categoryService, GoodsService goodsService) {
        this.categoryService = categoryService;
        this.goodsService = goodsService;
    }

    @GetMapping("/categories")
    List<GoodsCategory> getGoodsCategories() {
        return categoryService.getAll();
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    List<GoodsTO> findAll(@Valid @RequestBody List<GoodsFilter> filters) {
        return goodsService.findAll(filters)
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/categories/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<GoodsTO> findAllByGoodsCategory(@PathVariable Long categoryId, @Valid @RequestBody List<GoodsFilter> filters) {
        GoodsCategory goodsCategory = GoodsCategory.builder().id(categoryId).build();
        return goodsService.findAllByGoodsCategory(goodsCategory, filters)
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/nameLike", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<GoodsTO> findAllByNameLike(@Valid @RequestBody String nameLike, @Valid @RequestBody List<GoodsFilter> filters) {
        return goodsService.findAllByNameLike(nameLike, filters)
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/descriptionLike", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<GoodsTO> findAllByDescriptionLike(@Valid @RequestBody String descriptionLike,
                                           @Valid @RequestBody List<GoodsFilter> filters) {
        return goodsService.findAllByDescriptionLike(descriptionLike, filters)
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "/{goodsId}")
    void delete(@PathVariable("goodsId") long goodsId) {
        goodsService.delete(Goods.builder().id(goodsId).build());
    }

    //TO DO update create for Goods Service

    @PutMapping(value = "/{goodsId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody Goods goods, @PathVariable("goodsId") long goodsId) {
        goodsService.save(goods);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody Goods goods) {
        goodsService.save(goods);
    }

}
