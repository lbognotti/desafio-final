package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrderRequestDTO {
    @Valid
    @NotNull(message = "Objeto inboundOrder não pode ser nulo")
    @JsonProperty("inboundOrder")
    private InboundOrderDTO inboundOrder;
}
