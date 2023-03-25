package ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    @NotNull(message = "не введен товар")
    private ProductDTO product;
    @NotNull
    @Range(min=0, max=5, message = "Оценка должна находиться в диапазоне 0 до 5")
    private Integer rating;

}
