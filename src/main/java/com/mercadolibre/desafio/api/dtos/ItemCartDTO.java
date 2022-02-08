package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.mercadolibre.desafio.api.entities.ItemCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCartDTO {
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("quantity")
    private Long quantity;

    @JsonProperty("salesAdId")
    private Long salesAdId;

    @JsonProperty("cartId")
    private Long cartId;

    public static ItemCartDTO toItemCartDTO(ItemCart itemCart) {
        return ItemCartDTO.builder()
                .id(itemCart.getId())
                .cartId(itemCart.getCartId())
                .salesAdId(itemCart.getSalesAdId())
                .quantity(itemCart.getQuantity())
                .build();
    }

    public static ItemCart toItemCart(ItemCartDTO dto) {
        return ItemCart.builder()
                .quantity(dto.getQuantity())
                .cartId(dto.getCartId())
                .salesAdId(dto.getSalesAdId())
                .build();
    }
}
