package ru.VYurkin.TestFromEffectiveMobile.models.product;

import jakarta.persistence.*;
import lombok.Data;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewWithoutProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;

import java.util.List;
@Entity
@Table(name = "product")
@Data
public class Product {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "product_id")
   private long productId;

   @Column(name = "product_name")
   private String productName;

   @Column(name = "description")
   private String description;

   @ManyToOne
   @JoinColumn(name = "organisation_id", referencedColumnName = "organisation_id")
   private Organisation organisation;

   @Column(name = "coast")
   private float coast;

   @Column(name = "count")
   private int count;

   @ManyToOne
   @JoinColumn(name = "info_sale_id", referencedColumnName = "info_sale_id")
   private InfoSale infoSale;

   @OneToMany(mappedBy = "product")
   private List<Review> reviews;

   @OneToMany(mappedBy = "product")
   private List<Tag> tags;

   @OneToMany(mappedBy = "product")
   private List<TableProduct> tableProduct;

   @OneToMany(mappedBy = "product")
   private List<Rating> rating;

   @Column(name = "is_active")
   private Boolean isActive;

   @Column(name = "is_delete")
   private Boolean isDelete;

   @OneToMany(mappedBy = "product")
   private List<Purchases> purchases;

   @Transient
   private double ratingForUser;

   @Transient
   private OrganisationDTO organisationDTO;

   @Transient
   private List<ReviewWithoutProductDTO> reviewWithoutProductDTO;

   @Transient
   private InfoSaleDTO infoSaleDTO;

   @Transient
   private InfoSaleForAdminDTO infoSaleForAdminDTO;

   @Transient
   private List<RatingForAdminDTO> ratingsForAdminDTO;

   @Transient
   private List<ReviewForAdminDTO> reviewForAdminDTO;

}
