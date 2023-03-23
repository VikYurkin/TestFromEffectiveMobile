package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserWithoutPassDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/show")
    @ResponseBody
    public UserWithoutPassDTO showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return convertToUserWithoutPassDTO(((UsersDetails) authentication.getPrincipal()).user());
    }

    protected UserWithoutPassDTO convertToUserWithoutPassDTO(User user){
        return modelMapper.map(user, UserWithoutPassDTO.class);
    }
}
