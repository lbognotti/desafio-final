package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty(value = "salesAdId")
    private Long salesAdId;

    @JsonProperty("quantity")
    private Long quantity;
}
