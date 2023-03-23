package ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoSaleForAdminDTO {

    private long infoSaleId;

    private List<Long> productId;

    private Integer sale;

    private Integer time;

    public InfoSaleForAdminDTO(long infoSaleId, Integer sale, Integer time) {
        this.infoSaleId = infoSaleId;
        this.sale = sale;
        this.time = time;
    }
}
