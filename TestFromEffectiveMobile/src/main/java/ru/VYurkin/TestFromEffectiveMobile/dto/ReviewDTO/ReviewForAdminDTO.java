package ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewForAdminDTO {
    private long reviewId;

    private long userId;

    private String review;
}
