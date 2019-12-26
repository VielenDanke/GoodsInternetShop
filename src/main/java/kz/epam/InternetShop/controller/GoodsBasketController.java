package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.*;
import kz.epam.InternetShop.model.TO.GoodsTO;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.util.exception.NotAccessibleGoodsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static kz.epam.InternetShop.util.TOUtil.asTO;

@RestController
@RequestMapping(value = "/goods", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoodsBasketController {

    private final GoodsBasketService basketService;

    @Autowired
    public GoodsBasketController(GoodsBasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/basket/{userId}")
    public List<GoodsTO> getBasketGoods(@PathVariable("userId") long userId) {
        User user = User.builder().id(userId).build();
        Long orderId = basketService.getBasket(user).getId();
        return basketService.getAllOrderDetails(user)
                .stream()
                .map(od -> asTO(od.getGoods(), orderId))
                .collect(Collectors.toList());
    }

    @GetMapping("/basket/{userId}/clear")
    public ResponseEntity<String> clearBasket(@PathVariable("userId") long userId) {
        User user = User.builder().id(userId).build();
        basketService.clear(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/basket/{userId}/order")
    public ResponseEntity<String> placeOrder(@PathVariable("userId") long userId) throws NotAccessibleGoodsException {
        User user = User.builder().id(userId).build();
        basketService.setStatusToOne(user);     // throws NotAccessibleGoodsException
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/toBasket", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrderDetailsInBasket(@Valid @RequestBody OrderDetails orderDetails,
                                                                @PathVariable("userId") long userId) {
        User user = User.builder().id(userId).build();
        basketService.createOrderDetailsInBasket(orderDetails, user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/basket/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveOrderDetailsInBasket(@Valid @RequestBody List<OrderDetails> orderDetailsList,
                                                            @PathVariable("userId") long userId) {
        User user = User.builder().id(userId).build();
        basketService.saveOrderDetailsInBasket(orderDetailsList, user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/basket/{userId}/{orderDetailsId}")
    public ResponseEntity<String> removeFromBasket( @PathVariable("orderDetailsId") long orderDetailsId,
                                                        @PathVariable("userId") long userId) {
        User user = User.builder().id(userId).build();
        OrderDetails orderDetails = OrderDetails.builder().id(orderDetailsId).build();
        basketService.removeFromBasket(orderDetails, user);
        return new ResponseEntity(HttpStatus.OK);
    }



}
