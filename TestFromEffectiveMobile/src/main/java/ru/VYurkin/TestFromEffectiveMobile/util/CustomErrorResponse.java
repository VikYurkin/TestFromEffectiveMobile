package ru.VYurkin.TestFromEffectiveMobile.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse {

    private String message;

    private long timestamp;

}
