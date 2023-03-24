package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductWithOrganisationNameDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.OrganisationService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/organisation")
public class OrganisationController{
    private final OrganisationService organisationService;

    @Autowired
    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
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
    public @ResponseBody byte[] getTableImageFile(@RequestParam("name") String organisationName) throws IOException {
        Optional<Organisation> organisation = organisationService.findByNameIsActive(organisationName);
        if(organisation.isPresent())
            return organisationService.responseFile(organisation.get().getLogo());
        else
            return null;
    }

    @PostMapping("/addproduct")
    public ResponseEntity<HttpStatus> addProduct(@RequestBody ProductWithOrganisationNameDTO productWithOrganisationNameDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        if (organisationService.addProduct(productWithOrganisationNameDTO, user) != null) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else
            return ResponseEntity.of(Optional.of(HttpStatus.BAD_REQUEST));
    }
}



