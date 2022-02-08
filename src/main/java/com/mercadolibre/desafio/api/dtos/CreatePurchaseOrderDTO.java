package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import com.mercadolibre.desafio.api.entities.Buyer;
import com.mercadolibre.desafio.api.entities.PurchaseOrder;
import com.mercadolibre.desafio.api.enums.Status;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseOrderDTO {
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    @JsonProperty("buyerId")
    private Long buyerId;

    @JsonProperty("orderStatus")
    @Enumerated(value = EnumType.STRING)
    private Status orderStatus;

    @JsonProperty("products")
    private List<PurchaseOrderItemDTO> products;

    public PurchaseOrder toPurchaseOrder() {
        return PurchaseOrder.builder()
                .date(this.date)
                .status(this.orderStatus)
                .buyer(Buyer.builder().id(this.buyerId).build())
                .build();
    }
}
