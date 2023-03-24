package ru.VYurkin.TestFromEffectiveMobile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductsForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.product.Product;
import ru.VYurkin.TestFromEffectiveMobile.services.interfaces.ProductService;
import ru.VYurkin.TestFromEffectiveMobile.util.Converter;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final Converter converter;


    @Autowired
    public AdminController(ProductService productService, Converter converter) {
        this.productService = productService;
        this.converter = converter;
    }

    @GetMapping("/products")
    public @ResponseBody ProductsForAdminDTO getAllProductForAdmin(){
        List<Product> list =productService.findAll();
        return new ProductsForAdminDTO(converter.convertToForAdminDTO(converter.prepareConvertProductForAdmin(list)));
    }

    @PostMapping("/updateproduct")
    public @ResponseBody ProductForAdminDTO updateProduct(@RequestBody ProductForAdminDTO productForAdminDTO) {
        Optional<Product> product = productService.update(productForAdminDTO);
        if (product.isPresent())
            return converter.convertToForAdminDTO(converter.prepareConvertProductForAdmin(product.get()));
        return null;
    }

}
