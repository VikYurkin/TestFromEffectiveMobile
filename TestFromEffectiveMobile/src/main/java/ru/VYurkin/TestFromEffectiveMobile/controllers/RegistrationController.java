package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.AuthenticationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.JWTUtil;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;

import java.util.Map;

@RestController
@RequestMapping()
public class RegistrationController {

    private final UserService userService;
    private final Converter converter;

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public RegistrationController(UserService userService, Converter converter, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody UserDTO userDTO){
        User user = converter.convertToUser(userDTO);
        userService.registration(converter.convertToUser(userDTO));
        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String,String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try{
            authenticationManager.authenticate(authInputToken);
        }catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credential");
        }
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jvt-token", token);
    }

}
