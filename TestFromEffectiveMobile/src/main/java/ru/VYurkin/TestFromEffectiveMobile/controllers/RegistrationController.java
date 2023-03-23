package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.AuthenticationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.JWTUtil;
import ru.VYurkin.TestFromEffectiveMobile.services.UserService;

import java.util.Map;

@RestController
@RequestMapping()
public class RegistrationController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationController(UserService userService, ModelMapper modelMapper, JWTUtil jwtUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody UserDTO userDTO){
        User user = convertToUser(userDTO);
        userService.registration(convertToUser(userDTO));
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

    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
}
