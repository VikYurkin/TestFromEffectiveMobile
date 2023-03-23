package ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingForAdminDTO {

    private long ratingId;

    private Integer rating;

    private long userId;

}
