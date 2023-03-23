package ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.TableProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.TagDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForAdminDTO {

    private long productId;

    private String productName;

    private String description;

    private OrganisationDTO organisation;

    private float coast;

    private int count;

    private InfoSaleForAdminDTO infoSale;

    private List<ReviewForAdminDTO> reviews;

    private List<TagDTO> tags;

    private List<TableProductDTO> tableProduct;

    private List<RatingForAdminDTO> rating;

    private Boolean isActive;

    private Boolean isDelete;


}
