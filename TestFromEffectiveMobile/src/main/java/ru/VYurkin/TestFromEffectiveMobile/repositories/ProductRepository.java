package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductNameAndDescriptionAndCoastAndIsActive(String productName, String description, float coast, boolean isActive);
    List<Product> findByIsActive(boolean isActive);

}
