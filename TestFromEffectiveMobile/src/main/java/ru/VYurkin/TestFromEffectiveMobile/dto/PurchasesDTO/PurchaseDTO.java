package ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {

    private ProductDTO product;

    private Date date;
}
