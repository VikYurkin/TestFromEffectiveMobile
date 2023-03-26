package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts={"/sql/clearDbs.sql", "/sql/purchases/addPurchases.sql"})
public class PurchasesRepositoryIT {

    @Autowired
    private PurchasesRepository purchasesRepository;


    @Test
    public void shouldFindPurchasesByUserAndProduct(){
        //given
        Product product = new Product();
        product.setProductId(1);
        User user = new User();
        user.setUserId(1);
        Purchases purchases = purchasesRepository.findByUserAndProduct(user, product);

        //then
        Assertions.assertNotNull(purchases);
        Assertions.assertEquals(1, purchases.getPurchasesId());
    }

}
