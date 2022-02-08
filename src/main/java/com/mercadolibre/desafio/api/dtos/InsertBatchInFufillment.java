package com.mercadolibre.desafio.api.dtos;

import com.mercadolibre.desafio.api.entities.BatchStock;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class InsertBatchInFufillment {
    private Integer code;

    private LocalDateTime orderDate;

    private Long sectionId;

    private Long warehouseId;

    private List<BatchStock> batchStock;

    public static InsertBatchInFufillment fromInboundOrderDTO(InboundOrderDTO dto) {
        return InsertBatchInFufillment.builder()
                .orderDate(dto.getOrderDate())
                .sectionId(dto.getSection().getSectionId())
                .warehouseId(dto.getSection().getWarehouseId())
                .batchStock(dto.getBatchStock().stream().map(BatchStockDTO::toBatchStock).collect(Collectors.toList()))
                .build();
    }
}
