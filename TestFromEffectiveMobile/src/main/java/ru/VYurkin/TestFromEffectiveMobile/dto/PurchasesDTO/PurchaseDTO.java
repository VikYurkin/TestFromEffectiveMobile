package ru.VYurkin.TestFromEffectiveMobile.dto.PurchasesDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.VYurkin.TestFromEffectiveMobile.dto.ProductDTO.ProductDTO;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
    @NotNull(message = "не введен товар")
    private ProductDTO product;

    @NotNull(message = "не введена дата покупки")
    private Date date;
}
