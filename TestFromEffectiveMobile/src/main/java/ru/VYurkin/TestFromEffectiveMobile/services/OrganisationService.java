package ru.VYurkin.TestFromEffectiveMobile.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductWithOrganisationNameDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.repositories.OrganisationRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Service
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganisationService(OrganisationRepository organisationRepository, ModelMapper modelMapper) {
        this.organisationRepository = organisationRepository;
        this.modelMapper = modelMapper;
    }

    public Organisation save(Organisation organisation){
        return organisationRepository.save(organisation);
    }

    public String createFile(MultipartFile file) {
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
    public byte[] responseFile(String logo) throws IOException {
        byte[] array=null;
        String filePath=getFileDirectory()+logo;
            array = Files.readAllBytes(Paths.get(filePath));
        return array;
    }
    public Optional<Organisation> findByNameIsActive(String name){
        return organisationRepository.findByNameAndIsActive(name, true);
    }
    public Optional<Organisation> findByName(String name){
       return organisationRepository.findByName(name);}

    public Product addProduct(ProductWithOrganisationNameDTO productWithOrganisationNameDTO, User user){
        Optional<Organisation> organisation = findByNameIsActive(productWithOrganisationNameDTO.getName());
        if(organisation.isPresent()|user==null){
            return null;}
        if(!(organisation.get().getUser().getUserId()==user.getUserId()))
            return null;
        Product product = converterToProduct(productWithOrganisationNameDTO.getProduct(), organisation.get());
        return product;
    }
    private String getFileDirectory(){
        Properties p = System.getProperties();
        return p.getProperty("user.home")+p.getProperty("file.separator")+"logoOrganisation"+p.getProperty("file.separator");
    }
    private Product converterToProduct(ProductDTO productDTO, Organisation organisation){
        Product product = modelMapper.map(productDTO, Product.class);
        product.setOrganisation(organisation);
        product.setIsActive(false);
        product.setIsDelete(false);
        return product;
    }


}
