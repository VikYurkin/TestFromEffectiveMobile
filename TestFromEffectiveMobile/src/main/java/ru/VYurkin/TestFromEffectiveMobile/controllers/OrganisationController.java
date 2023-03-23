package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductWithOrganisationNameDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.security.UsersDetails;
import ru.VYurkin.TestFromEffectiveMobile.services.OrganisationService;
import ru.VYurkin.TestFromEffectiveMobile.services.ProductService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/organisation")
public class OrganisationController extends PurchasesController {
    private final OrganisationService organisationService;
    private final ProductService productService;


    @Autowired
    public OrganisationController(OrganisationService organisationService, ProductService productService) {
        this.organisationService = organisationService;
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> newOrganisation(@RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("logo") MultipartFile logo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UsersDetails) authentication.getPrincipal()).user();
        OrganisationDTO organisationDTO = new OrganisationDTO(name, description);
        Organisation organisation = convertToOrganisation(organisationDTO, user);
        organisation.setLogo(organisationService.createFile(logo));
        notificationForAdmin("Create organisation", String.format("I ask you to approve my organization %s.", organisation.getName()));
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
        Product product = productService.save(organisationService.addProduct(productWithOrganisationNameDTO, user));
        if (product != null) {
            notificationForAdmin("Create product",
                    String.format("I ask you to approve my product (productId = %s).", product.getProductId()));
            return ResponseEntity.ok(HttpStatus.OK);
        } else
            return ResponseEntity.of(Optional.of(HttpStatus.BAD_REQUEST));
    }
}



