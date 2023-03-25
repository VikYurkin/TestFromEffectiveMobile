package ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWithoutProductDTO {
    @NotBlank(message = "комментарий не должен быть пустым")
    private String review;
}
