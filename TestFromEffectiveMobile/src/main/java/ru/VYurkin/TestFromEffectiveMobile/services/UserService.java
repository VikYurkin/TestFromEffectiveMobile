package ru.VYurkin.TestFromEffectiveMobile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchaseDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Rating;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;
import ru.VYurkin.TestFromEffectiveMobile.models.user.DatesPurchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Role;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.repositories.*;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final PurchasesRepository purchasesRepository;

    private final DatesPurchasesRepository datesPurchasesRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;

    private User userExist;

    private Product productExist;
    private static float COMMISSION = 0.05f;
@Autowired
    public UserService(ProductRepository productRepository, UserRepository userRepository, PurchasesRepository purchasesRepository, DatesPurchasesRepository datesPurchasesRepository, ReviewRepository reviewRepository, RatingRepository ratingRepository, PasswordEncoder passwordEncoder, ProductService productService) {
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

    @Transactional
    public Date buyProduct(Long userId, Long productId){
        Date date = null;
        if(userAndProductOrNot(userId, productId))
            return null;

        float balance = userExist.getBalance();
        float coast = productExist.getCoast();
        int countOfShop = productExist.getCount();
        int countOfUser;

        if((balance<productExist.getCoast())|(countOfShop<0))
            return null;
        Purchases purchases =purchasesRepository.findByUserAndProduct(userExist, productExist);
        date=new Date();
        if(purchases==null){
            Purchases newPurchases = new Purchases(productExist, userExist, 1);
                List<DatesPurchases> listDate = new  ArrayList<>();
                listDate.add(new DatesPurchases(newPurchases, date));
                newPurchases.setDates(listDate);
                purchasesRepository.save(newPurchases);
            }else{
            datesPurchasesRepository.save(new DatesPurchases(purchases, date));
                purchases.setCount(purchases.getCount()+1);
            }
            productExist.setCount(countOfShop-1);
            userExist.setBalance(balance-coast);
            return date;
    }

    public String setReview(Long userId, Long productId, String text) {

        if(userAndProductOrNot(userId, productId))
            return null;

        Purchases purchases = purchasesRepository.findByUserAndProduct(userExist, productExist);

        if(purchases==null)
            return null;

        reviewRepository.save(new Review(productExist, userId, text));
            return text;

    }
    @Transactional
    public Double setRating(Long userId, Long productId, int rating){
        if(userAndProductOrNot(userId, productId) |rating<0|rating>5)
            return null;

        Purchases purchases = purchasesRepository.findByUserAndProduct(userExist, productExist);

        if (purchases == null)
            return null;

        Optional<Rating> newRating=productExist.getRating().stream().filter(ratings -> ratings.getUserId()==userId).findAny();
            if(newRating.isPresent())
                newRating.get().setRating(rating);
            else
                ratingRepository.save(new Rating(productExist, rating, userId));

            Double doubleRating;
            doubleRating=productExist.getRating().stream()
                    .mapToInt(Rating::getRating).average().getAsDouble();
            return doubleRating;
        }
        @Transactional
        public boolean returnProduct(PurchaseDTO purchaseDTO, User user){
            Product product = productService.findProductIsActive(purchaseDTO.getProduct());
            if(userAndProductOrNot(user.getUserId(), product.getProductId()))
                return false;

            Purchases purchases =purchasesRepository.findByUserAndProduct(userExist, productExist);

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
    private boolean userAndProductOrNot(Long userId, Long productId){
        Optional<User> userOrNot=userRepository.findById(userId);
        Optional<Product> productOrNot=productRepository.findById(productId);
        if(userOrNot.isPresent()&productOrNot.isPresent()){
            userExist =userOrNot.get();
            productExist=productOrNot.get();
        }
        return (!(userOrNot.isPresent() & productOrNot.isPresent()));
    }
}
