package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;

@Repository
public interface PurchasesRepository extends JpaRepository<Purchases, Long> {

    Purchases findByUserAndProduct(User user, Product product);
}
