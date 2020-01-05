package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.Order;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.TO.OrderDetailsTO;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.util.exception.NotAvailableGoodsException;
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
    public List<OrderDetailsTO> getBasketGoods(@PathVariable("userId") long userId) {
        User user = User.builder().id(userId).build();
        Order basket = basketService.getBasket(user);
        List<OrderDetailsTO> list = basketService.getAllOrderDetails(user)
                .stream()
                .map(od -> asTO(od))
                .collect(Collectors.toList());

        return basketService.getAllOrderDetails(user)
                .stream()
                .map(od -> asTO(od))
                .collect(Collectors.toList());
    }

    @GetMapping("/basket/{userId}/clear")
    public ResponseEntity<String> clearBasket(@PathVariable("userId") long userId) {
        User user = User.builder().id(userId).build();
        basketService.clear(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/basket/{userId}/order")
    public ResponseEntity<String> placeOrder(@PathVariable("userId") long userId) throws NotAvailableGoodsException {
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
    public ResponseEntity<String> updateCountOrderDetailsInBasket(
            @Valid @RequestBody List<OrderDetails> orderDetailsList,
            @PathVariable("userId") long userId
    ) {
        User user = User.builder().id(userId).build();
        basketService.updateCountOrderDetailsInBasket(orderDetailsList, user);
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
