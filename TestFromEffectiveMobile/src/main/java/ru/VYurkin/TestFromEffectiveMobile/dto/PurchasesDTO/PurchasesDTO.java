package ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasesDTO {

    private ProductDTO product;

    private List<Date> date;

    private int count;
}
