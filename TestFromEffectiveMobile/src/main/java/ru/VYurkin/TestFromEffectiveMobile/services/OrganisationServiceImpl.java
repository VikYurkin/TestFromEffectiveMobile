package ru.VYurkin.TestFromEffectiveMobile.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.ProductService;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.UserService;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;
import ru.VYurkin.TestFromEffectiveMobile.util.CustomNotCreatedException;

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
    private final ProductService productService;

    @Autowired
    public OrganisationServiceImpl(OrganisationRepository organisationRepository, NotificationService notificationService, UserService userService, Converter converter, ProductRepository productRepository, ProductService productService) {
        this.organisationRepository = organisationRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.converter = converter;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public byte[] responseFile(String name) throws IOException {
        Optional<Organisation> organisation = findByNameIsActive(name);
        if(organisation.isPresent()){
            String filePath=getFileDirectory()+organisation.get().getLogo();
            return Files.readAllBytes(Paths.get(filePath));
        }
        else
            throw new CustomNotCreatedException("организация с таким именем не зарегистрирована или заморожена");
    }

    public Optional<Organisation> findByNameIsActive(String name){
        return organisationRepository.findByNameAndIsActive(name, true);
    }
    public void newOrganisation(String name, String description, MultipartFile logo, User user){
        if(organisationRepository.findByName(name).isPresent())
            throw new CustomNotCreatedException("организация с таким именем уже зарегистрирована");
        if(description==null)
            throw new CustomNotCreatedException("описание организации не должно быть пустым");
        else if (description.isEmpty())
            throw new CustomNotCreatedException("описание организации не должно быть пустым");

        OrganisationDTO organisationDTO = new OrganisationDTO(name, description);
        Organisation organisation = converter.convertToOrganisation(organisationDTO, user);
        organisation.setLogo(createFile(logo));
        notificationForAdmin("Create organisation", String.format("I ask you to approve my organization %s.", organisation.getName()));
    }

    public void addProduct(ProductWithOrganisationNameDTO productWithOrganisationNameDTO, User user){
        Optional<Organisation> organisation = findByNameIsActive(productWithOrganisationNameDTO.getName());
        if(organisation.isEmpty())
            throw new CustomNotCreatedException("организация с таким именем не зарегистрирована или заморожена");
        if(!(organisation.get().getUser().getUserId()==user.getUserId()))
            throw new CustomNotCreatedException("Вы не являетесь владельцем данной организации и не можете добавлять товары от ее лица");
        if(productService.findProductIsActive(productWithOrganisationNameDTO.getProduct())!=null)
            throw new CustomNotCreatedException("Такой товар уже зарегистрирован");
        Product product = productRepository.save(converter.converterToProduct(productWithOrganisationNameDTO.getProduct(), organisation.get()));
        notificationForAdmin("Create product",
                String.format("I ask you to approve my product (productId = %s).", product.getProductId()));
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
        }else{
            throw new CustomNotCreatedException("приложите логотип организации");
        }
        return resultFilename.toString();
    }

}
