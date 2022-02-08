package com.mercadolibre.desafio.api.dtos;

import com.mercadolibre.desafio.api.entities.SalesAd;
import com.mercadolibre.desafio.api.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAdDTO {
    private Long id;

    private String title;

    private BigDecimal price;

    private String productName;

    private Category category;

    public static SalesAdDTO toSalesAdDTO(SalesAd salesAd){
        return SalesAdDTO.builder()
                .id(salesAd.getId())
                .title(salesAd.getTitle())
                .price(salesAd.getPrice())
                .productName(salesAd.getBatchStock().getProduct().getName())
                .category(salesAd.getBatchStock().getProduct().getCategory())
                .build();
    }
}
