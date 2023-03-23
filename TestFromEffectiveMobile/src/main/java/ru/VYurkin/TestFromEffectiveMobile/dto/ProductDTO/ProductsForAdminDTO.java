package ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsForAdminDTO {

    List<ProductForAdminDTO> products;

}
