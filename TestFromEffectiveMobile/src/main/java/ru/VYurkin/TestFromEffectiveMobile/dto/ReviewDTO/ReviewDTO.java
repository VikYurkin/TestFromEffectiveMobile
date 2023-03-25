package ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    @NotNull(message = "не введен товар")
    private ProductDTO product;

    @NotBlank(message = "комментарий не должен быть пустым")
    private String review;
}
