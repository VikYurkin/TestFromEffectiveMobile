package ru.VYurkin.TestFromEffectiveMobile.models.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "table_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private long tableId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "characteristic")
    private String characteristic;

    @Column(name = "descriptionсhar")
    private String descriptionChar;

    public TableProduct(Product product, String characteristic, String descriptionChar) {
        this.product = product;
        this.characteristic = characteristic;
        this.descriptionChar = descriptionChar;
    }
}
