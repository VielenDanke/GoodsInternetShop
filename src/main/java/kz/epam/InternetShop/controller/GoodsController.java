package kz.epam.InternetShop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.epam.InternetShop.model.Goods;
import kz.epam.InternetShop.model.GoodsCategory;
import kz.epam.InternetShop.model.TO.GoodsCategoryTO;
import kz.epam.InternetShop.model.TO.GoodsFiltersTO;
import kz.epam.InternetShop.model.TO.GoodsTO;
import kz.epam.InternetShop.model.filter.GoodsFilter;
import kz.epam.InternetShop.service.annotation.IsAdmin;
import kz.epam.InternetShop.service.interfaces.GoodsCategoryService;
import kz.epam.InternetShop.service.interfaces.GoodsService;
import kz.epam.InternetShop.util.TOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kz.epam.InternetShop.util.TOUtil.asList;
import static kz.epam.InternetShop.util.TOUtil.asTO;

@RestController
@RequestMapping(value = "/goods", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Goods management system")
public class GoodsController {

    private final GoodsCategoryService categoryService;

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsCategoryService categoryService, GoodsService goodsService) {
        this.categoryService = categoryService;
        this.goodsService = goodsService;
    }

    @GetMapping("/categories")
    @ApiOperation(value = "Return goods categories", response = List.class, httpMethod = "GET")
    List<GoodsCategoryTO> getGoodsCategories() {
        return categoryService.getAll()
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return all goods", response = List.class, httpMethod = "GET")
    List<GoodsTO> findAll(@Valid @RequestBody GoodsFiltersTO goodsFiltersTO) {
        return goodsService.findAll(asList(goodsFiltersTO))
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/categories/{categoryId}")
    @ApiOperation(value = "Find all goods by goods category", response = List.class, httpMethod = "GET")
    List<GoodsTO> findAllByGoodsCategory(@PathVariable Long categoryId) {
        List<GoodsFilter> filters = Collections.emptyList();
        GoodsCategory goodsCategory = GoodsCategory.builder().id(categoryId).build();
        return goodsService.findAllByGoodsCategory(goodsCategory, filters)
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }


    @PostMapping(value = "/categories/{categoryId}/filter", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find all goods by goods category with filters", response = List.class, httpMethod = "POST")
    List<GoodsTO> findAllByGoodsCategory(@PathVariable Long categoryId,
                                         @Valid @RequestBody GoodsFiltersTO goodsFiltersTO) {
        GoodsCategory goodsCategory = GoodsCategory.builder().id(categoryId).build();
        return goodsService.findAllByGoodsCategory(goodsCategory, asList(goodsFiltersTO))
                .stream()
                .map(TOUtil::asTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{goodsId}")
    @ApiOperation(value = "Find goods by ID", response = GoodsTO.class, httpMethod = "GET")
    public GoodsTO get(@PathVariable("goodsId") long goodsId) {
        goodsService.get(goodsId);
        return asTO(goodsService.get(goodsId));
    }

    @IsAdmin
    @DeleteMapping(value = "/{goodsId}")
    @ApiOperation(value = "Delete goods by ID", response = ResponseEntity.class, httpMethod = "DELETE")
    public ResponseEntity delete(@PathVariable("goodsId") long goodsId) {
        goodsService.delete(Goods.builder().id(goodsId).build());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @IsAdmin
    @PutMapping(value = "/{goodsId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update goods by ID", response = ResponseEntity.class, httpMethod = "PUT")
    public ResponseEntity update(@Valid @RequestBody Goods goods, @PathVariable("goodsId") long goodsId) {
        goods.setId(goodsId);
        goodsService.save(goods);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @IsAdmin
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create new goods", response = Goods.class, httpMethod = "POST")
    public Goods create(@Valid @RequestBody Goods goods) {
        goods.setId(null);
        return goodsService.save(goods);
    }
}
