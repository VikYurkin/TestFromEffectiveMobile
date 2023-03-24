package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductsDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.HistoryPurchasesDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchaseDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingWithDoubleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.notificationDTO.NotificationsDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.ProductService;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;

import java.util.*;

@RestController
@RequestMapping("/purchase")
public class PurchasesController {

    private final ProductService productService;

    private final UserService userService;


    private final Converter converter;

    public PurchasesController(ProductService productService, UserService userService,  Converter converter) {
        this.productService = productService;
        this.userService = userService;
        this.converter = converter;
    }

    @GetMapping("/productall")
    public ProductsDTO getAllProduct(){
        return new ProductsDTO(converter.convertToProductDTO(converter.prepareConvertListProductPurchase(productService.findIsActive())));
    }

    @PostMapping("/newpurchase")
    public @ResponseBody PurchaseDTO newPurchase(@RequestBody ProductDTO productDTO ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        return userService.buyProduct(user, productDTO);
    }

    @PostMapping("/newreview")
    public @ResponseBody ReviewDTO newReview(@RequestBody ReviewDTO reviewDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        return userService.setReview(user, reviewDTO);
    }

    @PostMapping("/newrating")
    public @ResponseBody RatingWithDoubleDTO newRating(@RequestBody RatingDTO ratingDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        return userService.setRating(user, ratingDTO);
    }

    @GetMapping ("/historypurchase")
    public @ResponseBody HistoryPurchasesDTO getHistoryPurchase(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        user =  userService.findByUsername(user.getUsername());
        return converter.convertToPurchasesDTO(user);
    }

    @PostMapping ("/returnproduct")
    public ResponseEntity<HttpStatus> returnProduct(@RequestBody PurchaseDTO purchaseDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        boolean result =userService.returnProduct(purchaseDTO, user);
        if(result){
            return ResponseEntity.ok(HttpStatus.OK);
       }
       return ResponseEntity.of(Optional.of(HttpStatus.BAD_REQUEST));
    }

    @GetMapping ("/notification")
    public @ResponseBody NotificationsDTO getNotification(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        user =  userService.findByUsername(user.getUsername());
        return new NotificationsDTO(converter.convertToNotificationDTO(user.getNotifications()));
    }

}
