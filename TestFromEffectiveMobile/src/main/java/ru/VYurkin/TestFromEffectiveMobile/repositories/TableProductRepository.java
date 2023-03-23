package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.TableProduct;

import java.util.Optional;

@Repository
public interface TableProductRepository extends JpaRepository<TableProduct, Long> {
   Optional<TableProduct>  findByCharacteristicAndProduct(String characteristic, Product product);
   Optional<TableProduct>  findByDescriptionCharAndProduct(String descriptionChar, Product product);
}