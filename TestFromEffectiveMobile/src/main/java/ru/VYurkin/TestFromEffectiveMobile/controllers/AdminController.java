package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO.InfoSaleForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductsForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.RatingDTO.RatingForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ReviewDTO.ReviewForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.product.InfoSale;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Rating;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Review;
import ru.VYurkin.TestFromEffectiveMobile.services.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController extends PurchasesController {

    private final ProductService productService;
    private final ModelMapper modelMapper;


    @Autowired
    public AdminController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/products")
    public @ResponseBody ProductsForAdminDTO getAllProductForAdmin(){
        List<Product> list =productService.findAll();
        return new ProductsForAdminDTO(convertToForAdminDTO(prepareConvertProductForAdmin(list)));
    }

    @PostMapping("/updateproduct")
    public @ResponseBody ProductForAdminDTO updateProduct(@RequestBody ProductForAdminDTO productForAdminDTO) {
        Optional<Product> product = productService.update(productForAdminDTO);
        if (product.isPresent())
            return convertToForAdminDTO(prepareConvertProductForAdmin(product.get()));
        return null;
    }



    private List<Product> prepareConvertProductForAdmin(List<Product> list){

        for(Product product:list){
            product=prepareConvertProductForAdmin(product);
        }

        return list;
    }
    private List<ProductForAdminDTO> convertToForAdminDTO(List<Product> products){
        return products.stream().map(this::convertToForAdminDTO).collect(Collectors.toList());
    }

    private ProductForAdminDTO convertToForAdminDTO(Product product){
        return modelMapper.map(product, ProductForAdminDTO.class);
    }

    private Product prepareConvertProductForAdmin(Product product){
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

    private InfoSaleForAdminDTO converterToInfoSaleForAdminDTO(InfoSale infoSale){
        InfoSaleForAdminDTO infoSaleForAdminDTO=
                new InfoSaleForAdminDTO(infoSale.getInfoSaleId(), infoSale.getSale(), infoSale.getTime());
        List<Product> products = infoSale.getProductIdWithSale();
        List<Long> list = new ArrayList<>();
        for(Product product:products){
            list.add(product.getProductId());
        }
        infoSaleForAdminDTO.setProductId(list);

        return infoSaleForAdminDTO;
    }
    private List<RatingForAdminDTO>convertToRatingForAdminDTO(List<Rating> ratings){
        return ratings.stream().map(this::convertToRatingForAdminDTO).collect(Collectors.toList());
    }

    private RatingForAdminDTO convertToRatingForAdminDTO(Rating rating){
        return modelMapper.map(rating, RatingForAdminDTO.class);
    }
    private List<ReviewForAdminDTO> convertToReviewForAdminDTO(List<Review> reviews){
        List<ReviewForAdminDTO> list = new ArrayList<>();
        reviews.forEach(review ->list.add(modelMapper.map(review, ReviewForAdminDTO.class)));
        return list;
    }
}
