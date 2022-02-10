package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchStockMinDTO {

    @JsonProperty("batchStockId")
    private Long id;

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("productCategory")
    private Category productCategory;

    @JsonProperty("dueDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dueDate;

    @JsonProperty("quantity")
    private Long quantity;

    public static BatchStockMinDTO toBatchStockMinDTO(BatchStock batchStock) {
        return BatchStockMinDTO.builder()
                .id(batchStock.getId())
                .productId(batchStock.getProduct().getId())
                .productCategory(batchStock.getProduct().getCategory())
                .dueDate(batchStock.getDueDate())
                .quantity(batchStock.getCurrentQuantity())
                .build();
    }
}
