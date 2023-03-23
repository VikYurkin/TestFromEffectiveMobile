package ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithoutPassDTO {

    private String username;


    private String email;
}
