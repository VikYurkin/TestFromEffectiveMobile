package ru.VYurkin.TestFromEffectiveMobile.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductWithOrganisationNameDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;

import java.io.IOException;
import java.util.*;

public interface OrganisationService {

   byte[] responseFile(String name) throws IOException ;
   Optional<Organisation> findByNameIsActive(String name);
   void addProduct(ProductWithOrganisationNameDTO productWithOrganisationNameDTO, User user);
   void newOrganisation(String name, String description, MultipartFile logo, User user);
}
