package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.desafio.api.entities.Buyer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDTO {
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("lastname")
    private String lastname;

    public static Buyer toBuyer(BuyerDTO dto) {
        return Buyer.builder()
                .name(dto.getName())
                .lastname(dto.lastname)
                .build();
    }

    public static BuyerDTO toBuyerDTO(Buyer buyer) {
        return BuyerDTO.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .lastname(buyer.getLastname())
                .build();
    }
}
