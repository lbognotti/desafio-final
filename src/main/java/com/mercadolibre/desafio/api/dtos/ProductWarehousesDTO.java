package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductWarehousesDTO {

    @NotNull(message = "ID do produto não pode ser nulo")
    @NotEmpty(message = "ID do produto não pode ser vazio")
    @JsonProperty("productId")
    private Long productId;

    @Valid
    @JsonProperty("warehouses")
    private List<WarehouseQtyDTO> warehouses;
}
