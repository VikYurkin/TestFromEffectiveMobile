package ru.VYurkin.TestFromEffectiveMobile.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.OrganisationDTO.OrganisationDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.HistoryPurchasesDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO.PurchasesDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewWithoutProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.UserDTO.UserWithoutPassDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.notificationDTO.NotificationDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.product.InfoSale;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Rating;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Notification;
import ru.VYurkin.TestFromEffectiveMobile.models.user.Purchases;
import ru.VYurkin.TestFromEffectiveMobile.models.user.User;
import ru.VYurkin.TestFromEffectiveMobile.repositories.OrganisationRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Converter {
    private final ModelMapper modelMapper;
    private final OrganisationRepository organisationRepository;

    public Converter(ModelMapper modelMapper, OrganisationRepository organisationRepository) {
        this.modelMapper = modelMapper;
        this.organisationRepository = organisationRepository;
    }


    public UserWithoutPassDTO convertToUserWithoutPassDTO(User user){
        return modelMapper.map(user, UserWithoutPassDTO.class);
    }
    public Organisation convertToOrganisation(OrganisationDTO organisationDTO, User user) {
        Organisation organisation=modelMapper.map(organisationDTO, Organisation.class);
        organisation.setUser(user);
        organisation.setIsActive(false);
        organisation.setIsDelete(false);
        organisation=organisationRepository.save(organisation);
        return organisation;
    }


    public List<ProductDTO> convertToProductDTO(List<Product> products){
        return products.stream().map(this::convertToProductDTO).collect(Collectors.toList());
    }

    public ProductDTO convertToProductDTO(Product product){
        return modelMapper.map(product, ProductDTO.class);
    }

    public OrganisationDTO convertToOrganisationDTO(Organisation organisation){
        return modelMapper.map(organisation, OrganisationDTO.class);}

    public InfoSaleDTO convertToInfoSaleDTOWithoutProduct(InfoSale infoSale){return modelMapper.map(infoSale, InfoSaleDTO.class);}
    public List<ReviewWithoutProductDTO> convertToReviewWithoutProductDTO(List<Review> reviews){
        List<ReviewWithoutProductDTO> list = new ArrayList<>();
        reviews.forEach(review ->list.add(modelMapper.map(review, ReviewWithoutProductDTO.class)));
        return list;
    }
    public List<Product> prepareConvertListProductPurchase(List<Product> list){
        return list.stream().map(this::prepareConvertListProductPurchase).collect(Collectors.toList());
    }
    public Product prepareConvertListProductPurchase(Product product){
        product.setRatingForUser(product.getRating().stream()
                .mapToInt(Rating::getRating).average().getAsDouble());
        product.setOrganisationDTO(convertToOrganisationDTO(product.getOrganisation()));
        product.setReviewWithoutProductDTO(convertToReviewWithoutProductDTO(product.getReviews()));
        product.setInfoSaleDTO(convertToInfoSaleDTOWithoutProduct(product.getInfoSale()));
        List <String> productNameList = new ArrayList<>();
        product.getInfoSale().getProductIdWithSale().forEach(name->productNameList.add(name.getProductName()));
        product.getInfoSaleDTO().setProductName(productNameList);
        return product;
    }

    public HistoryPurchasesDTO convertToPurchasesDTO(User user){
        List<PurchasesDTO> historyPurchases = new ArrayList<>();
        for (Purchases purchases:user.getHistoryPurchase()){
            ProductDTO productDTO=convertToProductDTO(prepareConvertListProductPurchase(purchases.getProduct()));
            int count = purchases.getCount();
            List<Date> dates = new ArrayList<>();
            purchases.getDates().forEach(date-> dates.add(date.getDate()));
            historyPurchases.add(new PurchasesDTO(productDTO, dates,count));
        }
        return new HistoryPurchasesDTO(historyPurchases);
    }
    public NotificationDTO convertToNotificationDTO(Notification notification){
        return modelMapper.map(notification, NotificationDTO.class);
    }
    public List<NotificationDTO> convertToNotificationDTO(List<Notification> notifications){
        return notifications.stream().map(this::convertToNotificationDTO).collect(Collectors.toList());
    }

    public List<RatingForAdminDTO>convertToRatingForAdminDTO(List<Rating> ratings){
        return ratings.stream().map(this::convertToRatingForAdminDTO).collect(Collectors.toList());
    }

    public RatingForAdminDTO convertToRatingForAdminDTO(Rating rating){
        return modelMapper.map(rating, RatingForAdminDTO.class);
    }
    public List<ReviewForAdminDTO> convertToReviewForAdminDTO(List<Review> reviews){
        List<ReviewForAdminDTO> list = new ArrayList<>();
        reviews.forEach(review ->list.add(modelMapper.map(review, ReviewForAdminDTO.class)));
        return list;
    }
    public Product converterToProduct(ProductDTO productDTO, Organisation organisation){
        Product product = modelMapper.map(productDTO, Product.class);
        product.setOrganisation(organisation);
        product.setIsActive(false);
        product.setIsDelete(false);
        return product;
    }

    public List<Product> prepareConvertProductForAdmin(List<Product> list){
        return list.stream().map(this::prepareConvertProductForAdmin).collect(Collectors.toList());
    }
    public List<ProductForAdminDTO> convertToForAdminDTO(List<Product> products){
        return products.stream().map(this::convertToForAdminDTO).collect(Collectors.toList());
    }

    public ProductForAdminDTO convertToForAdminDTO(Product product){
        return modelMapper.map(product, ProductForAdminDTO.class);
    }

    public Product prepareConvertProductForAdmin(Product product){
        product.setOrganisationDTO(convertToOrganisationDTO(product.getOrganisation()));
        List<Review> reviews=product.getReviews();
        if(!reviews.isEmpty())
            product.setReviewForAdminDTO(convertToReviewForAdminDTO(reviews));

        InfoSale infoSale = product.getInfoSale();
        if(infoSale!=null)
            product.setInfoSaleForAdminDTO(converterToInfoSaleForAdminDTO(infoSale));

        List<Rating> ratings =product.getRating();
        if(!ratings.isEmpty())
            product.setRatingsForAdminDTO(convertToRatingForAdminDTO(ratings));
        return product;
    }

    public InfoSaleForAdminDTO converterToInfoSaleForAdminDTO(InfoSale infoSale){
        InfoSaleForAdminDTO infoSaleForAdminDTO=
                new InfoSaleForAdminDTO(infoSale.getInfoSaleId(), infoSale.getSale(), infoSale.getTime());
        List<Product> products = infoSale.getProductIdWithSale();
        List<Long> list = new ArrayList<>();
        products.forEach(product->list.add(product.getProductId()));
        infoSaleForAdminDTO.setProductId(list);

        return infoSaleForAdminDTO;
    }
    public User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    public void validate(BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors =  bindingResult.getFieldErrors();
            for(FieldError error:errors){
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new CustomNotCreatedException(errorMsg.toString());
        }
    }
}
