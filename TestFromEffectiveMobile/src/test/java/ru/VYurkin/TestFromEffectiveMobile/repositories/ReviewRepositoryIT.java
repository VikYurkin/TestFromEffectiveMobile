package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;

import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts={"/sql/clearDbs.sql", "/sql/review/addReview.sql"})
public class ReviewRepositoryIT {

    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    public void shouldFindByReviewIdAndUserId(){
        //given

        Optional<Review> review = reviewRepository.findByReviewIdAndUserId(1, 1);

        //then
        Assertions.assertTrue(review.isPresent());
        Assertions.assertEquals("text", review.get().getReview());
    }

}
