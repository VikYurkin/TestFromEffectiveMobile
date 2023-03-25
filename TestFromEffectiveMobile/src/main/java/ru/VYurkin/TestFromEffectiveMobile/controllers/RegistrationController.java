package ru.VYurkin.TestFromEffectiveMobile.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.AuthenticationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.JWTUtil;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;
import ru.VYurkin.TestFromEffectiveMobile.util.*;
import ru.VYurkin.TestFromEffectiveMobile.util.Validator.UserDTOValidator;

import java.util.Map;

@RestController
@RequestMapping()
public class RegistrationController {

    private final UserService userService;
    private final Converter converter;

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDTOValidator userDTOValidator;

    @Autowired
    public RegistrationController(UserService userService,
                                  Converter converter,
                                  JWTUtil jwtUtil,
                                  AuthenticationManager authenticationManager,
                                  UserDTOValidator userDTOValidator) {
        this.userService = userService;
        this.converter = converter;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDTOValidator = userDTOValidator;
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult){
        userDTOValidator.validate(userDTO, bindingResult);
        converter.validate(bindingResult);
        User user = converter.convertToUser(userDTO);
        userService.registration(converter.convertToUser(userDTO));
        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String,String> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult bindingResult ){
        converter.validate(bindingResult);
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
    @ExceptionHandler
    private ResponseEntity<CustomErrorResponse> handleException(CustomNotCreatedException e){
        CustomErrorResponse response = new CustomErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
