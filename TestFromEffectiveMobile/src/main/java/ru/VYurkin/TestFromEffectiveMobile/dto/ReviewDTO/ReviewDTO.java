package ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private ProductDTO product;

    private String review;
}
