package ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "не должно быть пустым")
    private String productName;
    @NotNull(message = "не должно быть пустым")
    private String description;
    @NotNull(message = "не должно быть пустым")
    private OrganisationDTO organisation;
    @Min(value = 0, message = "не может быть отрицательным")
    private float coast;
    @Min(value = 0, message = "не может быть отрицательным")
    private int count;

    private InfoSaleDTO infoSale;

    private List<ReviewWithoutProductDTO> reviews;

    private List<TagDTO> tags;

    private List<TableProductDTO> tableProduct;

    private double ratingForUser;

}
