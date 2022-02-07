package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchStockDTO {
    @JsonProperty("id")
    @NotNull
    @NotEmpty(message = "Id não pode estar vazio")
    private Long id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("productId")
    @NotNull
    @NotEmpty(message = "productId não pode estar vazio")
    private Long productId;

    @JsonProperty("maximumTemperature")
    private Double maximumTemperature;

    @JsonProperty("currentTemperature")
    private Double currentTemperature;

    @JsonProperty("minimumTemperature")
    private Double minimumTemperature;

    @JsonProperty("initialQuantity")
    @NotNull
    @NotEmpty(message = "Quantidade inicial não pode estar vazio")
    @Min(value = 1, message = "A quantidade inicial mínima é 1")
    private Long initialQuantity;

    @JsonProperty("currentQuantity")
    @NotNull
    @NotEmpty(message = "Quantidade atual não pode estar vazio")
    @Min(value = 0, message = "A quantidade atual não pode ser negativa")
    private Long currentQuantity;

    @JsonProperty("manufacturingDate")
    @NotNull
    @NotEmpty(message = "Data de fabricação não pode estar vazio")
    @PastOrPresent
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime manufacturingDate;

    @JsonProperty("dueDate")
    @NotNull
    @NotEmpty(message = "Data de validade não pode estar vazio")
    @Future
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dueDate;

    public static BatchStockDTO toBatchStockDTO(BatchStock batchStock) {
        return BatchStockDTO.builder()
                .code(batchStock.getCode())
                .productId(batchStock.getProduct().getId())
                .maximumTemperature(batchStock.getMaximumTemperature())
                .currentTemperature(batchStock.getCurrentTemperature())
                .minimumTemperature(batchStock.getMinimumTemperature())
                .initialQuantity(batchStock.getInitialQuantity())
                .currentQuantity(batchStock.getCurrentQuantity())
                .manufacturingDate(batchStock.getManufacturingDate())
                .dueDate(batchStock.getDueDate())
                .build();
    }

    public static BatchStock toBatchStock(BatchStockDTO dto) {
        return BatchStock.builder()
                .id(dto.id)
                .code(dto.getCode())
                .maximumTemperature(dto.getMaximumTemperature())
                .currentTemperature(dto.getCurrentTemperature())
                .minimumTemperature(dto.getMinimumTemperature())
                .initialQuantity(dto.getInitialQuantity())
                .currentQuantity(dto.getCurrentQuantity())
                .manufacturingDate(dto.getManufacturingDate())
                .dueDate(dto.getDueDate())
                .product(Product.builder().id(dto.productId).build())
                .build();
    }
}
