package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mercadolibre.desafio.api.entities.InboundOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InboundOrderDTO {
    @JsonProperty("orderDate")
    @NotNull(message = "Data do pedido não pode ser nula")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime orderDate;

    @JsonProperty("section")
    @Valid
    @NotNull(message = "Códigos de seção e armazém não podem ser nulos")
    private WarehouseSectionCodesDTO section;

    @JsonProperty("batchStock")
    @Valid
    @NotNull(message = "Lista de lotes não pode ser nula")
    @NotEmpty(message = "A lista de lotes não pode ser vazia")
    private List<BatchStockDTO> batchStock;

    public static InboundOrderDTO toInboundOrder (InboundOrder inboundOrder) {
        return InboundOrderDTO.builder()
                .orderDate(inboundOrder.getDate())
                .section(WarehouseSectionCodesDTO.builder()
                        .sectionId(inboundOrder.getSection().getId())
                        .warehouseId(inboundOrder.getSection().getWarehouse().getId()).build())
                .batchStock(inboundOrder.getBatchStocks().stream().map(BatchStockDTO::toBatchStockDTO).collect(Collectors.toList()))
                .build();
    }
}
