package ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotEmpty(message = "не должен быть пустым")
    @Size(min = 2, max = 50, message = "username должен быть не короче 2 символов, и не длиннее 50 символов")
    private String username;

    @NotEmpty(message = "не должен быть пустым")
    @Email(message = "должен быть вида user@mail.com")
    private String email;

    @NotEmpty(message = "не должен быть пустым")
    private String password;

}
