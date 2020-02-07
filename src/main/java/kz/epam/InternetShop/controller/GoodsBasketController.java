package kz.epam.InternetShop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.epam.InternetShop.model.OrderDetails;
import kz.epam.InternetShop.model.TO.OrderDetailsTO;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.security.UserPrincipal;
import kz.epam.InternetShop.service.annotation.CurrentUser;
import kz.epam.InternetShop.service.annotation.IsUser;
import kz.epam.InternetShop.service.interfaces.GoodsBasketService;
import kz.epam.InternetShop.service.interfaces.UserService;
import kz.epam.InternetShop.util.exception.NotAvailableGoodsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static kz.epam.InternetShop.util.TOUtil.*;

@RestController
@RequestMapping(value = "/goods", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Basket management system")
public class GoodsBasketController {

    private final GoodsBasketService basketService;
    private UserService userService;

    @Autowired
    public GoodsBasketController(GoodsBasketService basketService, UserService userService) {
        this.basketService = basketService;
        this.userService = userService;
    }

    @IsUser
    @GetMapping("/basket")
    @ApiOperation(value = "Get basket by user principal", response = List.class, httpMethod = "GET")
    public List<OrderDetailsTO> getBasketGoods(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        return basketService.getAllOrderDetails(user)
                .stream()
                .map(od -> asTO(od))
                .collect(Collectors.toList());
    }

    @IsUser
    @GetMapping("/basket/clear")
    @ApiOperation(value = "Clear the basket of user principal", response = ResponseEntity.class, httpMethod = "GET")
    public ResponseEntity<String> clearBasket(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        basketService.clear(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @IsUser
    @GetMapping("/basket/order")
    @ApiOperation(value = "Form order of user", response = ResponseEntity.class, httpMethod = "GET")
    public ResponseEntity<String> placeOrder(@CurrentUser UserPrincipal userPrincipal) throws NotAvailableGoodsException {
        User user = userService.findById(userPrincipal.getId());
        basketService.setStatusToOne(user);     // throws NotAccessibleGoodsException
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @IsUser
    @PostMapping(value = "/toBasket", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a good and create order in basket", response = ResponseEntity.class, httpMethod = "POST")
    public ResponseEntity<String> createOrderDetailsInBasket(@Valid @RequestBody OrderDetailsTO orderDetailsTO,
                                                             @CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        basketService.createOrderDetailsInBasket(createFrom(orderDetailsTO), user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @IsUser
    @PutMapping(value = "/basket", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update count order details in basket", response = ResponseEntity.class, httpMethod = "PUT")
    public ResponseEntity<String> updateCountOrderDetailsInBasket(
            @Valid @RequestBody List<OrderDetailsTO> orderDetailsTOList,
            @CurrentUser UserPrincipal userPrincipal
    ) {
        User user = userService.findById(userPrincipal.getId());
        basketService.updateCountOrderDetailsInBasket(createListFrom(orderDetailsTOList), user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @IsUser
    @DeleteMapping(value = "/basket/{orderDetailsId}")
    @ApiOperation(value = "Remove order details from basket", response = ResponseEntity.class, httpMethod = "DELETE")
    public ResponseEntity<String> removeFromBasket(@PathVariable("orderDetailsId") long orderDetailsId,
                                                   @CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        OrderDetails orderDetails = OrderDetails.builder().id(orderDetailsId).build();
        basketService.removeFromBasket(orderDetails, user);
        return new ResponseEntity(HttpStatus.OK);
    }
}
