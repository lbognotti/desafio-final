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
public class BatchStockRequisito3DTO {

    private String batchNumber;
    private Long currentQuantity;
    private LocalDateTime dueDate;
    private Long productId;
    private Long sectionId;
    private Long wareHouseId;

    public static BatchStockRequisito3DTO toBatchStockDTO (BatchStock batchStock1) {
        return BatchStockRequisito3DTO.builder()
                .batchNumber(batchStock1.getCode())
                .currentQuantity(batchStock1.getCurrentQuantity())
                .dueDate(batchStock1.getDueDate())
                .productId(batchStock1.getProduct().getId())
                .sectionId(batchStock1.getInboundOrder().getSection().getId())
                .wareHouseId(batchStock1.getInboundOrder().getSection().getWarehouse().getId())
                .build();
    }

    public static List<BatchStockRequisito3DTO> toList (List<BatchStock> batchStocks1) {
        return batchStocks1
                .stream().
                        map(BatchStockRequisito3DTO::toBatchStockDTO)
                .collect(Collectors.toList());
    }
}
