package ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDTO {

    @NotEmpty(message = "не должен быть пустым")
    @Size(min = 2, max = 50, message = "должен быть не короче 2 символов, и не длиннее 50 символов")
    private String username;

    @NotEmpty(message = "не должен быть пустым")
    private String password;

}
