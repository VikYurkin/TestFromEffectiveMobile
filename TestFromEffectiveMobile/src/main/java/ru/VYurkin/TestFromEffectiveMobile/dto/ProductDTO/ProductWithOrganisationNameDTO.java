package ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithOrganisationNameDTO {

    private String name;

    private ProductDTO product;

}
