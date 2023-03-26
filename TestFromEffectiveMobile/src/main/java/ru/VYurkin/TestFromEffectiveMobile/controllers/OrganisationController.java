package ru.VYurkin.TestFromEffectiveMobile.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductWithOrganisationNameDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.OrganisationService;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;

import java.io.IOException;

@RestController
@RequestMapping("/organisation")
public class OrganisationController{
    private final OrganisationService organisationService;
    private final Converter converter;

    @Autowired
    public OrganisationController(OrganisationService organisationService, Converter converter) {
        this.organisationService = organisationService;
        this.converter = converter;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> newOrganisation(@RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("logo") MultipartFile logo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        organisationService.newOrganisation(name, description, logo, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "/file", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getTableImageFile(@RequestParam("name") String name) throws IOException {
        return organisationService.responseFile(name);
    }

    @PostMapping("/addproduct")
    public ResponseEntity<HttpStatus> addProduct(@RequestBody @Valid ProductWithOrganisationNameDTO productWithOrganisationNameDTO,
                                                 BindingResult bindingResult) {
        converter.validate(bindingResult);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        organisationService.addProduct(productWithOrganisationNameDTO, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}



