package ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithOrganisationNameDTO {
    @NotNull(message = "не должно быть пустым")
    private String name;

    @NotNull(message = "не должно быть пустым")
    private ProductDTO product;
}
