package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;
import ru.VYurkin.TestFromEffectiveMobile.models.product.TableProduct;

import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts={"/sql/clearDbs.sql", "/sql/tableProduct/addTableProduct.sql"})
public class TableProductRepositoryIT {

    @Autowired
    private TableProductRepository tableProductRepository;


    @Test
    public void shouldFindByCharacteristicAndProduct(){
        //given
        Product product = new Product();
        product.setProductId(1);
        Optional<TableProduct> tableProduct = tableProductRepository.findByCharacteristicAndProduct("characteristic1", product);

        //then
        Assertions.assertTrue(tableProduct.isPresent());
        Assertions.assertEquals("descriptionChar1", tableProduct.get().getDescriptionChar());
    }

    @Test
    public void shouldFindByDescriptionCharAndProduct(){
        //given
        Product product = new Product();
        product.setProductId(1);
        Optional<TableProduct> tableProduct = tableProductRepository.findByDescriptionCharAndProduct("descriptionChar2", product);

        //then
        Assertions.assertTrue(tableProduct.isPresent());
        Assertions.assertEquals("characteristic2", tableProduct.get().getCharacteristic());
    }


}
