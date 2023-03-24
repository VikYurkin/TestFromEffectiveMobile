package ru.VYurkin.TestFromEffectiveMobile.services.interfaces;

import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductForAdminDTO;
import ru.VYurkin.TestFromEffectiveMobile.models.product.*;

import java.util.List;
import java.util.Optional;

public interface ProductService {

   Product findProductIsActive(ProductDTO productDTO);
   Product save(Product product);

   List<Product> findAll();
   List<Product> findIsActive();

   Optional<Product> update(ProductForAdminDTO productForAdminDTO);
}
