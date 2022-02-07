package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
