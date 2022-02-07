package com.mercadolibre.desafio.api.dtos;

import com.mercadolibre.desafio.api.entities.BatchStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertBatchInFufillment {
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
