package com.mercadolibre.desafio.api.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Double maximumTemperature;

    private Double currentTemperature;

    private Double minimumTemperature;

    private Long initialQuantity;

    private Long currentQuantity;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime manufacturingDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private InboundOrder inboundOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    public Double getVolume() {
        return this.currentQuantity * this.product.getVolume();
    }

    public Warehouse getWarehouse() {
        return this.getInboundOrder().getSection().getWarehouse();
    }
}
