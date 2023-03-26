package ru.VYurkin.TestFromEffectiveMobile.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductsDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.HistoryPurchasesDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchaseDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingWithDoubleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.notificationDTO.NotificationsDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.ProductService;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;
import ru.VYurkin.TestFromEffectiveMobile.util.Validator.ProductDTOExistValidator;

import java.util.*;

@RestController
@RequestMapping("/purchase")
public class PurchasesController {

    private final ProductService productService;

    private final UserService userService;


    private final Converter converter;
    private final ProductDTOExistValidator productDTOExistValidator;

    public PurchasesController(ProductService productService, UserService userService, Converter converter, ProductDTOExistValidator productDTOExistValidator) {
        this.productService = productService;
        this.userService = userService;
        this.converter = converter;
        this.productDTOExistValidator = productDTOExistValidator;
    }

    @GetMapping("/productall")
    public ProductsDTO getAllProduct(){
        return new ProductsDTO(converter.convertToProductDTO(converter.prepareConvertListProductPurchase(productService.findIsActive())));
    }

    @PostMapping("/newpurchase")
    public @ResponseBody PurchaseDTO newPurchase(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult ) {
        productDTOExistValidator.validate(productDTO, bindingResult);
        converter.validate(bindingResult);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        return userService.buyProduct(user, productDTO);
    }

    @PostMapping("/newreview")
    public @ResponseBody ReviewDTO newReview(@RequestBody @Valid ReviewDTO reviewDTO, BindingResult bindingResult){
        productDTOExistValidator.validate(reviewDTO.getProduct(), bindingResult, true);
        converter.validate(bindingResult);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        return userService.setReview(user, reviewDTO);
    }

    @PostMapping("/newrating")
    public @ResponseBody RatingWithDoubleDTO newRating(@RequestBody @Valid RatingDTO ratingDTO, BindingResult bindingResult){
        productDTOExistValidator.validate(ratingDTO.getProduct(), bindingResult, true);
        converter.validate(bindingResult);
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
    public ResponseEntity<HttpStatus> returnProduct(@RequestBody @Valid PurchaseDTO purchaseDTO, BindingResult bindingResult){
        productDTOExistValidator.validate(purchaseDTO.getProduct(), bindingResult, true);
        converter.validate(bindingResult);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        userService.returnProduct(purchaseDTO, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping ("/notification")
    public @ResponseBody NotificationsDTO getNotification(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        user =  userService.findByUsername(user.getUsername());
        return new NotificationsDTO(converter.convertToNotificationDTO(user.getNotifications()));
    }

}
