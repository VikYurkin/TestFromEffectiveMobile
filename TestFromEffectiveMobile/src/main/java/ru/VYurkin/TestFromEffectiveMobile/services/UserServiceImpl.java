package ru.VYurkin.TestFromEffectiveMobile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchaseDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingWithDoubleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Rating;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;
import ru.VYurkin.TestFromEffectiveMobile.models.user.DatesPurchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Role;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.repositories.*;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.ProductService;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final PurchasesRepository purchasesRepository;

    private final DatesPurchasesRepository datesPurchasesRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private static float COMMISSION = 0.05f;
@Autowired
    public UserServiceImpl(ProductRepository productRepository, UserRepository userRepository, PurchasesRepository purchasesRepository, DatesPurchasesRepository datesPurchasesRepository, ReviewRepository reviewRepository, RatingRepository ratingRepository, PasswordEncoder passwordEncoder, ProductService productService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.purchasesRepository = purchasesRepository;
        this.datesPurchasesRepository = datesPurchasesRepository;
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
        this.passwordEncoder = passwordEncoder;
        this.productService = productService;
}

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }
    public User registration(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(0);
        user.setRole(Collections.singleton(Role.USER));
        user.setIsActive(true);
        user.setIsDelete(false);
        userRepository.save(user);
        return user;
    }

    public PurchaseDTO buyProduct(User user, ProductDTO productDTO){
        Product product = productService.findProductIsActive(productDTO);
        PurchaseDTO purchaseDTO = null;
        Date date = null;
        if(userAndProductOrNot(user, product))
            return null;

        float balance = user.getBalance();
        float coast = product.getCoast();
        int countOfShop = product.getCount();

        if((balance<product.getCoast())|(countOfShop<0))
            return null;
        Purchases purchases =purchasesRepository.findByUserAndProduct(user, product);
        date=new Date();
        if(purchases==null){
            Purchases newPurchases = new Purchases(product, user, 1);
            List<DatesPurchases> listDate = new  ArrayList<>();
            listDate.add(new DatesPurchases(newPurchases, date));
            newPurchases.setDates(listDate);
            purchasesRepository.save(newPurchases);
        }else{
            datesPurchasesRepository.save(new DatesPurchases(purchases, date));
            purchases.setCount(purchases.getCount()+1);
        }
        product.setCount(countOfShop-1);
        user.setBalance(balance-coast);

        if(date!=null){
            purchaseDTO = new PurchaseDTO(productDTO, date);
            getMoney(product);
        }
       return purchaseDTO;
    }


    public ReviewDTO setReview(User user, ReviewDTO reviewDTO) {
        Product product = productService.findProductIsActive(reviewDTO.getProduct());
        String text = reviewDTO.getReview();
        if(userAndProductOrNot(user, product))
            return null;

        Purchases purchases = purchasesRepository.findByUserAndProduct(user, product);

        if(purchases==null)
            return null;

        reviewRepository.save(new Review(product, user.getUserId(), text));
        return new ReviewDTO(reviewDTO.getProduct(), text);
    }
    public RatingWithDoubleDTO setRating(User user, RatingDTO ratingDTO){
        Product product = productService.findProductIsActive(ratingDTO.getProduct());
        int rating = ratingDTO.getRating();
        if(userAndProductOrNot(user, product) |rating<0|rating>5)
            return null;
        Purchases purchases = purchasesRepository.findByUserAndProduct(user, product);

        if (purchases == null)
            return null;

        Optional<Rating> newRating=product.getRating().stream().filter
                (ratings -> ratings.getUserId()==user.getUserId()).findAny();
            if(newRating.isPresent())
                newRating.get().setRating(rating);
            else
                ratingRepository.save(new Rating(product, rating, user.getUserId()));

            Double doubleRating;
            doubleRating=product.getRating().stream()
                    .mapToInt(Rating::getRating).average().getAsDouble();
        if(doubleRating != null)
            return new RatingWithDoubleDTO(ratingDTO.getProduct(), doubleRating);
        return null;
        }

        public boolean returnProduct(PurchaseDTO purchaseDTO, User user){
            Product product = productService.findProductIsActive(purchaseDTO.getProduct());
            if(userAndProductOrNot(user, product))
                return false;

            Purchases purchases =purchasesRepository.findByUserAndProduct(user, product);

            if(purchases==null)
                return false;

            long date=purchaseDTO.getDate().getTime();
            for(DatesPurchases dateOnDB:purchases.getDates()){
                if(dateOnDB.getDate().getTime()==date){
                   if(((new Date().getTime() - date) / 1000 / 60 / 60 / 24) < 3){
                       datesPurchasesRepository.delete(dateOnDB);
                       int countOfUser =purchases.getCount();
                       if(countOfUser==1)
                           purchasesRepository.delete(purchases);

                       purchases.setCount(purchases.getCount()-1);
                       product.setCount(product.getCount()+1);
                       user.setBalance((user.getBalance()+product.getCoast()));
                       User salesman = product.getOrganisation().getUser();
                       salesman.setBalance(salesman.getBalance()-(1-COMMISSION)*product.getCoast());
                       userRepository.save(user);
                       return true;
                   }
                }
            }
            return false;
        }


   public List<User> findAdmin(){
       return userRepository.findByRole(Role.ADMIN.toString());
    }


    public User save(User user){
        return userRepository.save(user);
    }

    public void getMoney(Product product){
        User salesman =product.getOrganisation().getUser();
        salesman.setBalance(salesman.getBalance()+product.getCoast()*(1-COMMISSION));
        save(salesman);
    }
    private boolean userAndProductOrNot(User user, Product product){
        Optional<User> userOrNot=userRepository.findById(user.getUserId());
        Optional<Product> productOrNot=productRepository.findById(product.getProductId());
        return (!(userOrNot.isPresent() & productOrNot.isPresent()));
    }
}
