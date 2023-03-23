package ru.VYurkin.TestFromEffectiveMobile.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.TableProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.TagDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;
import ru.VYurkin.TestFromEffectiveMobile.models.product.*;
import ru.VYurkin.TestFromEffectiveMobile.repositories.*;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrganisationService organisationService;
    private final InfoSaleRepository infoSaleRepository;
    private final ReviewRepository reviewRepository;
    private final TagRepository tagRepository;
    private final TableProductRepository tableProductRepository;
    private final RatingRepository ratingRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, OrganisationService organisationService, InfoSaleRepository infoSaleRepository, ReviewRepository reviewRepository, TagRepository tagRepository, TableProductRepository tableProductRepository, RatingRepository ratingRepository) {
        this.productRepository = productRepository;
        this.organisationService = organisationService;
        this.infoSaleRepository = infoSaleRepository;
        this.reviewRepository = reviewRepository;
        this.tagRepository = tagRepository;
        this.tableProductRepository = tableProductRepository;
        this.ratingRepository = ratingRepository;
    }

    public Product findProductIsActive(ProductDTO productDTO){
        return productRepository.findByProductNameAndDescriptionAndCoastAndIsActive(
                productDTO.getProductName(),
                productDTO.getDescription(),
                productDTO.getCoast(),
                true);
    }
    public Product save(Product product){
        return productRepository.save(product);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public List<Product> findIsActive(){
        return productRepository.findByIsActive(true);
    }

    public Optional<Product> update(ProductForAdminDTO productForAdminDTO){
        Optional<Product> optProduct = productRepository.findById(productForAdminDTO.getProductId());
        if(optProduct.isEmpty())
            return optProduct;
        Product product =optProduct.get();
        product.setProductName(productForAdminDTO.getProductName());
        product.setDescription(productForAdminDTO.getDescription());
        product.setIsActive(productForAdminDTO.getIsActive());
        product.setIsDelete(productForAdminDTO.getIsDelete());
        Optional<Organisation> organisation=organisationService.findByName(productForAdminDTO.getOrganisation().getName());
        if(organisation.isPresent()){
        Organisation oldOrganisation =product.getOrganisation();
        if(oldOrganisation.getOrganisationId()==organisation.get().getOrganisationId()){
            oldOrganisation.getProducts().remove(product);
            organisationService.save(oldOrganisation);
        }
        product.setOrganisation(organisation.get());
        }
        product.setCoast(productForAdminDTO.getCoast());
        product.setCount(productForAdminDTO.getCount());

        InfoSaleForAdminDTO infoSaleForAdminDTO= productForAdminDTO.getInfoSale();
        InfoSale oldInfoSale =product.getInfoSale();
        if(oldInfoSale!=null){
            oldInfoSale.getProductIdWithSale().remove(product);
            product.setInfoSale(null);
        }
        if(infoSaleForAdminDTO!=null) {
            Optional<InfoSale> infoSale = infoSaleRepository.findById(infoSaleForAdminDTO.getInfoSaleId());
            if (infoSale.isEmpty()) {
                infoSale = Optional.of(new InfoSale());
            }
            List<Product> infoSaleProducts = productRepository.findAllById(infoSaleForAdminDTO.getProductId());
            infoSale.get().setSale(infoSaleForAdminDTO.getSale());
            infoSale.get().setTime(infoSaleForAdminDTO.getTime());
            infoSale.get().setProductIdWithSale(infoSaleProducts);
            infoSaleRepository.save(infoSale.get());
            product.setInfoSale(infoSale.get());
            if (!infoSaleProducts.isEmpty()) {
                for (Product infoSaleProduct : infoSaleProducts) {
                    InfoSale oldInfoSaleProduct = infoSaleProduct.getInfoSale();
                    infoSaleProduct.setInfoSale(null);
                    infoSaleProduct.setInfoSale(infoSale.get());
                    productRepository.save(infoSaleProduct);
                    if (oldInfoSaleProduct != null) {
                        if (oldInfoSaleProduct.getProductIdWithSale().size() == 1) {
                            infoSaleRepository.delete(oldInfoSaleProduct);
                        }
                    }
                }
            }
            if (oldInfoSale != null) {
                if (oldInfoSale.getProductIdWithSale().size() == 1) {
                    infoSaleRepository.delete(oldInfoSale);
                }
            }
        }


        List<ReviewForAdminDTO> reviewsReviewForAdminDTO=productForAdminDTO.getReviews();
        if(reviewsReviewForAdminDTO!=null) {

            for (ReviewForAdminDTO reviewReviewForAdminDTO : reviewsReviewForAdminDTO) {
                long userId = reviewReviewForAdminDTO.getUserId();
                StringBuilder reviewText = new StringBuilder(reviewReviewForAdminDTO.getReview());
                Optional<Review> review = reviewRepository.findByReviewIdAndUserId(reviewReviewForAdminDTO.getReviewId(), userId);
                if(review.isPresent()){
                review.get().setReview(reviewText.toString());
                reviewRepository.save(review.get());
                }
                reviewText.setLength(0);
            }
        }


        List<TagDTO> tagsDTO=productForAdminDTO.getTags();

            if(tagsDTO!=null){

                for(TagDTO tagDTO:tagsDTO){
                    StringBuilder tagText = new StringBuilder(tagDTO.getTag());
                    Optional<Tag> tag=tagRepository.findById(tagDTO.getTagId());
                    if(tag.isPresent()){
                    tag.get().setTag(tagText.toString());
                    tagRepository.save(tag.get());
                    }
                    tagText.setLength(0);
                }
        }


        List<TableProductDTO> tablesProductDTO=productForAdminDTO.getTableProduct();

        if(tablesProductDTO!=null){

            for(TableProductDTO tableProductDTO:tablesProductDTO){
                StringBuilder characteristic = new StringBuilder(tableProductDTO.getCharacteristic());
                StringBuilder descriptionChar = new StringBuilder(tableProductDTO.getDescriptionChar());
                Optional<TableProduct> tableProductCharacteristic=tableProductRepository.
                        findByCharacteristicAndProduct(characteristic.toString(), product);
                Optional<TableProduct> tableProductDescriptionChar=tableProductRepository.
                        findByDescriptionCharAndProduct(descriptionChar.toString(), product);
                if(tableProductDescriptionChar.isPresent()|tableProductCharacteristic.isPresent()){
                    if(tableProductDescriptionChar.isPresent()){
                        tableProductDescriptionChar.get().setCharacteristic(characteristic.toString());
                        tableProductRepository.save(tableProductDescriptionChar.get());
                    }else{
                        tableProductCharacteristic.get().setDescriptionChar(descriptionChar.toString());
                        tableProductRepository.save(tableProductCharacteristic.get());
                    }
                }else {
                    TableProduct tableProduct=new TableProduct(product, characteristic.toString(), descriptionChar.toString());
                    tableProductRepository.save(tableProduct);
                }
                characteristic.setLength(0);
                descriptionChar.setLength(0);
            }
        }

        List<RatingForAdminDTO> newRatingsForAdminDTO =productForAdminDTO.getRating();
        if(!newRatingsForAdminDTO.isEmpty()) {
        for(RatingForAdminDTO newRatingForAdminDTO:newRatingsForAdminDTO){
            Optional<Rating> rating =ratingRepository.findByRatingIdAndProductAndUserId(newRatingForAdminDTO.getRatingId(), product, newRatingForAdminDTO.getUserId());
            if(rating.isPresent()){
                rating.get().setRating(newRatingForAdminDTO.getRating());
                ratingRepository.save(rating.get());
            }
        }
        }

        productRepository.save(product);
        return Optional.of(product);
    }



}
