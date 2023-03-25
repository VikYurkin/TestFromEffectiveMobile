package ru.VYurkin.TestFromEffectiveMobile.util.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserDTO;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;

@Component
public class UserDTOValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserDTOValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDTO userDTO = (UserDTO) o;
        if(userService.findByUsername(userDTO.getUsername())!=null){
           errors.rejectValue("username", "", "Пользователь с таким username уже существует");
        }
        if (userService.findByEmail(userDTO.getEmail()).isPresent()){
            errors.rejectValue("email", "", "Пользователь с таким email уже существует");
        }
    }
}
