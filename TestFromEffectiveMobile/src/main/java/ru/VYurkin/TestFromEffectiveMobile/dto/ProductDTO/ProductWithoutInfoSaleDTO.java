package ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingWithoutProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewWithoutProductDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithoutInfoSaleDTO {

    private String productName;

    private String description;

    private OrganisationDTO organisation;

    private float coast;

    private int count;
    private RatingWithoutProductDTO rating;
    private ReviewWithoutProductDTO review;
}
