package ru.VYurkin.TestFromEffectiveMobile.models.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "info_sale")
@Data
public class InfoSale {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "info_sale_id")
   private long infoSaleId;

   @OneToMany(mappedBy = "infoSale")
   private List<Product> productIdWithSale;

   @Column(name = "sale")
   private Integer sale;

   @Column(name = "time")
   private Integer time;

}
