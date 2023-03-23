package ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewWithoutProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.TableProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.TagDTO;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String productName;

    private String description;

    private OrganisationDTO organisation;

    private float coast;

    private int count;

    private InfoSaleDTO infoSale;

    private List<ReviewWithoutProductDTO> reviews;

    private List<TagDTO> tags;

    private List<TableProductDTO> tableProduct;

    private double ratingForUser;

}
