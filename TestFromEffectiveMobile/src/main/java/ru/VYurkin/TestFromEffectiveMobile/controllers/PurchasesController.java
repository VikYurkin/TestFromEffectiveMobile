package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductsDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.HistoryPurchasesDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchaseDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchasesDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingWithDoubleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewWithoutProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.notificationDTO.NotificationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.notificationDTO.NotificationsDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.product.InfoSale;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Rating;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Notification;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.services.NotificationService;
import ru.VYurkin.TestFromEffectiveMobile.services.OrganisationService;
import ru.VYurkin.TestFromEffectiveMobile.services.ProductService;
import ru.VYurkin.TestFromEffectiveMobile.services.UserService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/purchase")
public class PurchasesController extends UserController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private NotificationService notificationService;



    @GetMapping("/productall")
    public ProductsDTO getAllProduct(){
        List<Product> list =productService.findIsActive();
        return new ProductsDTO(convertToProductDTO(prepareConvertListProductPurchase(list)));
    }

    @PostMapping("/newpurchase")
    public @ResponseBody PurchaseDTO newPurchase(@RequestBody ProductDTO productDTO ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        Product product = productService.findProductIsActive(productDTO);
        Date date =userService.buyProduct(user.getUserId(), product.getProductId());
        PurchaseDTO purchaseDTO=null;
        if(date!=null){
            purchaseDTO = new PurchaseDTO(productDTO, date);
            userService.getMoney(product);
        }
        return purchaseDTO;
    }

    @PostMapping("/newreview")
    public @ResponseBody ReviewDTO newReview(@RequestBody ReviewDTO reviewDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        Product product = productService.findProductIsActive(reviewDTO.getProduct());
        String review = userService.setReview(user.getUserId(), product.getProductId(), reviewDTO.getReview());
        if(review != null)
            return new ReviewDTO(reviewDTO.getProduct(), review);
        return null;
    }

    @PostMapping("/newrating")
    public @ResponseBody RatingWithDoubleDTO newRating(@RequestBody RatingDTO ratingDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        Product product = productService.findProductIsActive(ratingDTO.getProduct());
        Double rating = userService.setRating(user.getUserId(), product.getProductId(), ratingDTO.getRating());
        if(rating != null)
            return new RatingWithDoubleDTO(ratingDTO.getProduct(), rating);
        return null;
    }

    @GetMapping ("/historypurchase")
    public @ResponseBody HistoryPurchasesDTO getHistoryPurchase(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        user =  userService.findByUsername(user.getUsername());
        return convertToPurchasesDTO(user);
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
        return new NotificationsDTO(convertToNotificationDTO(user.getNotifications()));
    }

    protected void notificationForAdmin(String headerPost, String post){
        Notification notification = new Notification(headerPost ,new Date(), post);
        List<User>admins=userService.findAdmin();
        for(User admin:admins){
            notification.setUser(admin);
            notificationService.save(notification);
        }
    }

    protected Organisation convertToOrganisation(OrganisationDTO organisationDTO, User user) {
        Organisation organisation=modelMapper.map(organisationDTO, Organisation.class);
        organisation.setUser(user);
        organisation.setIsActive(false);
        organisation.setIsDelete(false);
        organisation=organisationService.save(organisation);
        return organisation;
    }


    private List<ProductDTO> convertToProductDTO(List<Product> products){
        return products.stream().map(this::convertToProductDTO).collect(Collectors.toList());
    }

    private ProductDTO convertToProductDTO(Product product){
        return modelMapper.map(product, ProductDTO.class);
    }

    protected OrganisationDTO convertToOrganisationDTO(Organisation organisation){return modelMapper.map(organisation, OrganisationDTO.class);}

    private InfoSaleDTO convertToInfoSaleDTOWithoutProduct(InfoSale infoSale){return modelMapper.map(infoSale, InfoSaleDTO.class);}
    protected List<ReviewWithoutProductDTO> convertToReviewWithoutProductDTO(List<Review> reviews){
            List<ReviewWithoutProductDTO> list = new ArrayList<>();
            reviews.forEach(review ->list.add(modelMapper.map(review, ReviewWithoutProductDTO.class)));
            return list;
    }
    private List<Product> prepareConvertListProductPurchase(List<Product> list){

        for(Product product:list){
            product=prepareConvertListProductPurchase(product);
        }

        return list;
    }
    private Product prepareConvertListProductPurchase(Product product){
        product.setRatingForUser(product.getRating().stream()
                .mapToInt(Rating::getRating).average().getAsDouble());
        product.setOrganisationDTO(convertToOrganisationDTO(product.getOrganisation()));
        product.setReviewWithoutProductDTO(convertToReviewWithoutProductDTO(product.getReviews()));
        product.setInfoSaleDTO(convertToInfoSaleDTOWithoutProduct(product.getInfoSale()));
        List <String> productNameList = new ArrayList<>();
        product.getInfoSale().getProductIdWithSale().forEach(name->productNameList.add(name.getProductName()));
        product.getInfoSaleDTO().setProductName(productNameList);
        return product;
    }

    private HistoryPurchasesDTO convertToPurchasesDTO(User user){
        List<PurchasesDTO> historyPurchases = new ArrayList<>();
        for (Purchases purchases:user.getHistoryPurchase()){
            ProductDTO productDTO=convertToProductDTO(prepareConvertListProductPurchase(purchases.getProduct()));
            int count = purchases.getCount();
            List<Date> dates = new ArrayList<>();
            purchases.getDates().forEach(date-> dates.add(date.getDate()));
            historyPurchases.add(new PurchasesDTO(productDTO, dates,count));
        }
        return new HistoryPurchasesDTO(historyPurchases);
    }
    private NotificationDTO convertToNotificationDTO(Notification notification){
        return modelMapper.map(notification, NotificationDTO.class);
    }
    private List<NotificationDTO> convertToNotificationDTO(List<Notification> notifications){
        return notifications.stream().map(this::convertToNotificationDTO).collect(Collectors.toList());
    }

}
