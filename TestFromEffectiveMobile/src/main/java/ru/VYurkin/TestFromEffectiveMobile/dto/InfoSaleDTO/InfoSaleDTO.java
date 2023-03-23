package ru.VYurkin.TestFromEffectiveMobile.dto.InfoSaleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoSaleDTO {

private List<String> productName;

    private Integer sale;

    private Integer time;

}
