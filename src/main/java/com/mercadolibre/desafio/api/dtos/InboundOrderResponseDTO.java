package com.mercadolibre.desafio.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrderResponseDTO {
    @NotNull(message = "Estoque de entrada não pode ser nulo")
    @NotEmpty(message = "Estoque de entrada não pode ser vazio")
    private List<BatchStockDTO> batchStock;
}
