package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsResponseDTO {
    @JsonProperty("purchaseOrderId")
    private Long purchaseOrderId;

    @Valid
    @JsonProperty("items")
    private List<ItemCartDTO> items;
}
