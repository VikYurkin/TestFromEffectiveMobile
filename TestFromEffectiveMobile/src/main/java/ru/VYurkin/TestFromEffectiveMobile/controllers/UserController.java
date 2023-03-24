package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserWithoutPassDTO;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Converter converter;

    public UserController(Converter converter) {
        this.converter = converter;
    }

    @GetMapping("/show")
    @ResponseBody
    public UserWithoutPassDTO showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return converter.convertToUserWithoutPassDTO(((UsersDetails) authentication.getPrincipal()).user());
    }

}
