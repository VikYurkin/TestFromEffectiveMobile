package ru.VYurkin.TestFromEffectiveMobile.models.product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private long ratingId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "user_id")
    private long userId;

    public Rating(Product product, Integer rating, long userId) {
        this.product = product;
        this.rating = rating;
        this.userId = userId;
    }
}
