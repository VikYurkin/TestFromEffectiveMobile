package ru.VYurkin.TestFromEffectiveMobile.models.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchases")
@Data
@NoArgsConstructor
public class Purchases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchases_id")
    private long purchasesId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "purchases")
    private List<DatesPurchases> dates;

    @Column(name = "count")
    private int count;

    public Purchases(Product product, User user, int count) {
        this.product = product;
        this.user = user;
        this.count = count;
    }
}
