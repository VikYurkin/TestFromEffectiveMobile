package ru.VYurkin.TestFromEffectiveMobile.models.product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long reviewId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "review")
    private String review;

    public Review(Product product, long userId, String review) {
        this.product = product;
        this.userId = userId;
        this.review = review;
    }
}
