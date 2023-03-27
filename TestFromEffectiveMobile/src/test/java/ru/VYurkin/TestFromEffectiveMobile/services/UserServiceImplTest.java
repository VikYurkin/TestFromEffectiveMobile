package ru.VYurkin.TestFromEffectiveMobile.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchaseDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingWithDoubleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Rating;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;
import ru.VYurkin.TestFromEffectiveMobile.models.user.DatesPurchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Role;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.repositories.*;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.ProductService;

import java.util.*;

@DisplayName("Unit-level testing for UserService")
public class UserServiceImplTest {

    private UserRepository userRepository;

    private PurchasesRepository purchasesRepository;

    private DatesPurchasesRepository datesPurchasesRepository;
    private ReviewRepository reviewRepository;

    private PasswordEncoder passwordEncoder;
    private ProductService productService;
    private static float COMMISSION = 0.05f;
    private UserServiceImpl userService;


    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        productService = Mockito.mock(ProductService.class);
        purchasesRepository = Mockito.mock(PurchasesRepository.class);
        reviewRepository = Mockito.mock(ReviewRepository.class);
        RatingRepository ratingRepository = Mockito.mock(RatingRepository.class);
        datesPurchasesRepository = Mockito.mock(DatesPurchasesRepository.class);
        userService = new UserServiceImpl(userRepository, purchasesRepository, datesPurchasesRepository, reviewRepository, ratingRepository, passwordEncoder, productService);
    }

    @Test
    public void shouldFindByUsername() {

        User user = new User();
        user.setUsername("user1");
        Mockito.when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        //when
        User newUser = userService.findByUsername("user1");

        //then
        Assertions.assertNotNull(newUser);
        Assertions.assertEquals(user, newUser);

    }

    @Test
    public void shouldUserRegistration() {
        User user = new User();
        user.setUsername("user1");
        String oldPassword ="111111111";
        String newPassword = "2222222222";
        user.setPassword(oldPassword);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(newPassword);

        //when
        User newUser = userService.registration(user);
        Mockito.verify(passwordEncoder).encode(oldPassword);
        //then
        Assertions.assertNotNull(newUser);
        Assertions.assertEquals(newPassword, newUser.getPassword());
        Assertions.assertEquals(0, newUser.getBalance());
        Assertions.assertEquals(Collections.singleton(Role.USER), newUser.getRole());
        Assertions.assertTrue(newUser.getIsActive());
        Assertions.assertFalse(newUser.getIsDelete());
    }

    @Test
    public void shouldBuyProduct() {
        User bayer = new User();
        bayer.setUsername("bayer");
        bayer.setBalance(50);

        User salesman= new User();
        salesman.setBalance(0);
        salesman.setUsername("salesman");

        Organisation organisation = new Organisation();
        organisation.setUser(salesman);

        ProductDTO productDTO = new ProductDTO();

        Product product = new Product();
        product.setCount(1);
        product.setCoast(50);
        product.setOrganisation(organisation);

        Purchases purchases = new Purchases(product, bayer, 1);

        Mockito.when(productService.findProductIsActive(productDTO)).thenReturn(product);
        Mockito.when(purchasesRepository.findByUserAndProduct(bayer, product)).thenReturn(null);
        Mockito.when(purchasesRepository.save(purchases)).thenReturn(purchases);
        Mockito.when(userRepository.save(salesman)).thenReturn(salesman);

        //when
        PurchaseDTO purchaseDTO = userService.buyProduct(bayer, productDTO);
        Mockito.verify(productService).findProductIsActive(productDTO);
        Mockito.verify(purchasesRepository).findByUserAndProduct(bayer, product);
        Mockito.verify(userRepository).save(salesman);

        //then
        Assertions.assertNotNull(purchaseDTO);
        Assertions.assertEquals(new Date().getTime(), purchaseDTO.getDate().getTime(), 1000 );
        Assertions.assertEquals(0, bayer.getBalance());
        Assertions.assertEquals(0, product.getCount());
        Assertions.assertEquals(50*(1-COMMISSION), salesman.getBalance());
    }

    @Test
    public void shouldSetReview() {
        User user= new User();

        ReviewDTO reviewDTO = new ReviewDTO();
        String text ="text";
        ProductDTO productDTO = new ProductDTO();
        reviewDTO.setReview(text);
        reviewDTO.setProduct(productDTO);

        Product product = new Product();

        Purchases purchases = new Purchases(product, user, 1);

        Review review =new Review(product, user.getUserId(), text );
        Mockito.when(productService.findProductIsActive(productDTO)).thenReturn(product);
        Mockito.when(purchasesRepository.findByUserAndProduct(user, product)).thenReturn(purchases);
        Mockito.when(reviewRepository.save(review)).thenReturn(review);

        //when
        ReviewDTO newReviewDTO = userService.setReview(user, reviewDTO);
        Mockito.verify(productService).findProductIsActive(productDTO);
        Mockito.verify(purchasesRepository).findByUserAndProduct(user, product);
        Mockito.verify(reviewRepository).save(review);

        //then
        Assertions.assertNotNull(newReviewDTO);
        Assertions.assertEquals(newReviewDTO, reviewDTO );
    }

    @Test
    public void shouldSetRating() {
        User user= new User();

        RatingDTO ratingDTO = new RatingDTO();
        int rating =5;
        ProductDTO productDTO = new ProductDTO();
        ratingDTO.setRating(rating);
        ratingDTO.setProduct(productDTO);

        Product product = new Product();
        product.setRating(new ArrayList<>(Collections.singletonList(new Rating(product,0, user.getUserId()))));

        Purchases purchases = new Purchases(product, user, 1);

        Mockito.when(productService.findProductIsActive(productDTO)).thenReturn(product);
        Mockito.when(purchasesRepository.findByUserAndProduct(user, product)).thenReturn(purchases);

        //when
        RatingWithDoubleDTO newRatingDTO = userService.setRating(user, ratingDTO);
        Mockito.verify(productService).findProductIsActive(productDTO);
        Mockito.verify(purchasesRepository).findByUserAndProduct(user, product);

        //then
        Assertions.assertNotNull(newRatingDTO);
        Assertions.assertEquals(5, newRatingDTO.getRating() );
    }

    @Test
    public void shouldReturnProduct() {
        User bayer = new User();
        bayer.setUsername("bayer");
        bayer.setBalance(0);

        User salesman= new User();
        salesman.setBalance(50*(1-COMMISSION));
        salesman.setUsername("salesman");

        Organisation organisation = new Organisation();
        organisation.setUser(salesman);

        Product product = new Product();
        product.setCount(0);
        product.setCoast(50);
        product.setOrganisation(organisation);


        PurchaseDTO purchaseDTO = new PurchaseDTO();

        Date date =new Date();

        ProductDTO productDTO = new ProductDTO();
        purchaseDTO.setProduct(productDTO);
        purchaseDTO.setDate(date);


        Purchases purchases = new Purchases(product, bayer, 2);
        DatesPurchases dateOnDB = new DatesPurchases(purchases, date);
        purchases.setDates(new ArrayList<>(Collections.singleton(dateOnDB)));

        Mockito.when(productService.findProductIsActive(productDTO)).thenReturn(product);
        Mockito.when(purchasesRepository.findByUserAndProduct(bayer, product)).thenReturn(purchases);
        Mockito.doNothing().when(datesPurchasesRepository).delete(dateOnDB);
        Mockito.when(userRepository.save(bayer)).thenReturn(bayer);

        //when
        userService.returnProduct(purchaseDTO, bayer);
        Mockito.verify(datesPurchasesRepository).delete(dateOnDB);
        Mockito.verify(userRepository).save(bayer);

        //then
        Assertions.assertEquals(50, bayer.getBalance());
        Assertions.assertEquals(1, product.getCount());
        Assertions.assertEquals(0, salesman.getBalance());
    }
}
