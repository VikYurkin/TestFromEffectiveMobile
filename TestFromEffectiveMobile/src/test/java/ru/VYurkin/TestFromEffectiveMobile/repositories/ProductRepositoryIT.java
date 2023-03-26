package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Role;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts={"/sql/clearDbs.sql", "/sql/product/AddProduct.sql"})
public class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void shouldFindByProductNameAndDescriptionAndCoastAndIsActive(){
        //given
        Product product = productRepository.findByProductNameAndDescriptionAndCoastAndIsActive("book", "newBook", 10, true);

        //then
        Assertions.assertNotNull(product);
        Assertions.assertEquals(1, product.getProductId());
    }

    @Test
    public void shouldFindByActiveProducts(){
        //given
        List<Product> products = productRepository.findByIsActive(true);

        //then
        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals(1, products.get(0).getProductId());
        Assertions.assertEquals(2, products.get(1).getProductId());
    }
}
