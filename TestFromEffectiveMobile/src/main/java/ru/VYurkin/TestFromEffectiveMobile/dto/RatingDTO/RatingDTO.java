package ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    private ProductDTO product;

    private Integer rating;

}
