package ru.VYurkin.TestFromEffectiveMobile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductWithOrganisationNameDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Notification;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.repositories.OrganisationRepository;
import ru.VYurkin.TestFromEffectiveMobile.repositories.ProductRepository;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.OrganisationService;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class OrganisationServiceImpl implements OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final Converter converter;
    private final ProductRepository productRepository;

    @Autowired
    public OrganisationServiceImpl(OrganisationRepository organisationRepository, NotificationService notificationService, UserService userService, Converter converter, ProductRepository productRepository) {
        this.organisationRepository = organisationRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.converter = converter;
        this.productRepository = productRepository;
    }

    public byte[] responseFile(String logo) throws IOException {
        byte[] array=null;
        String filePath=getFileDirectory()+logo;
            array = Files.readAllBytes(Paths.get(filePath));
        return array;
    }

    public Optional<Organisation> findByNameIsActive(String name){
        return organisationRepository.findByNameAndIsActive(name, true);
    }
    public void newOrganisation(String name, String description, MultipartFile logo, User user){
        OrganisationDTO organisationDTO = new OrganisationDTO(name, description);
        Organisation organisation = converter.convertToOrganisation(organisationDTO, user);
        organisation.setLogo(createFile(logo));
        notificationForAdmin("Create organisation", String.format("I ask you to approve my organization %s.", organisation.getName()));
    }

    public Product addProduct(ProductWithOrganisationNameDTO productWithOrganisationNameDTO, User user){
        Optional<Organisation> organisation = findByNameIsActive(productWithOrganisationNameDTO.getName());
        if(organisation.isPresent()|user==null){
            return null;}
        if(!(organisation.get().getUser().getUserId()==user.getUserId()))
            return null;
        Product product = productRepository.save(converter.converterToProduct(productWithOrganisationNameDTO.getProduct(), organisation.get()));
        if (product != null) {
            notificationForAdmin("Create product",
                    String.format("I ask you to approve my product (productId = %s).", product.getProductId()));}
        return product;
    }
    private String getFileDirectory(){
        Properties p = System.getProperties();
        return p.getProperty("user.home")+p.getProperty("file.separator")+"logoOrganisation"+p.getProperty("file.separator");
    }
    private void notificationForAdmin(String headerPost, String post){
        Notification notification = new Notification(headerPost ,new Date(), post);
        List<User> admins=userService.findAdmin();
        for(User admin:admins){
            notification.setUser(admin);
            notificationService.save(notification);
        }
    }


    private String createFile(MultipartFile file) {
        StringBuilder resultFilename =new StringBuilder();
        Properties p = System.getProperties();
        String filePath=getFileDirectory();
        if(file!=null && !file.getOriginalFilename().isEmpty() ){
            File uploadsDir = new File(filePath);
            if(!uploadsDir.exists()){
                uploadsDir.mkdir();
            }
            resultFilename
                    .append(UUID.randomUUID().toString())
                    .append(".")
                    .append(file.getOriginalFilename());
            try {
                file.transferTo(new File(filePath+p.getProperty("file.separator")+resultFilename.toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return resultFilename.toString();
    }

}
