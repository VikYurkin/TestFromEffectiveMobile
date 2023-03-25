package ru.VYurkin.TestFromEffectiveMobile.services.interfaces;

import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchaseDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingWithDoubleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
   User findByUsername(String username);
   User registration(User user);
   PurchaseDTO buyProduct(User user, ProductDTO productDTO);
   ReviewDTO setReview(User user, ReviewDTO reviewDTO);
   RatingWithDoubleDTO setRating(User user, RatingDTO ratingDTO);
   void returnProduct(PurchaseDTO purchaseDTO, User user);
   List<User> findAdmin();
   Optional<User> findByEmail(String email);
   void getMoney(Product product);

}
