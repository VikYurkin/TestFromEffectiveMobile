package ru.VYurkin.TestFromEffectiveMobile.util.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.ProductService;

@Component
public class ProductDTOExistValidator implements Validator {

    private final ProductService productService;

    @Autowired
    public ProductDTOExistValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDTO productDTO = (ProductDTO) o;
        if (productService.findProductIsActive(productDTO) == null) {

            errors.rejectValue("productName", "", "Такого товара не существует в магазине или он заморожен");
            errors.rejectValue("description", "", "Такого товара не существует в магазине или он заморожен");
            errors.rejectValue("description", "", "Такого товара не существует в магазине или он заморожен");
        }
    }
        public void validate(Object o, Errors errors, boolean isProductInObject) {
            ProductDTO productDTO = (ProductDTO) o;
            if(productService.findProductIsActive(productDTO)==null){
                errors.rejectValue("product", "", "Такого товара не существует в магазине или он заморожен");
            }
    }
}
