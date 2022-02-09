package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.services.BatchStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/fresh-products/due-date")
public class BatchStockController {
    private final BatchStockService batchStockService;

    public BatchStockController(BatchStockService batchStockService) {
        this.batchStockService = batchStockService;
    }

    @GetMapping
    public ResponseEntity<List<BatchStock>> findAllBatchStock(@RequestParam String sectionId, @RequestParam String dueDate) {
        List<BatchStock> batchStocks = this.batchStockService.findOneLoteDuedateBatchStock(Long.parseLong(sectionId), Long.parseLong(dueDate));
//        List<BatchStock> batchStockMinDTOS = batchStocks.stream()
//                .map(BatchStockMinDTO::toBatchStockMinDTO)
//                .collect(Collectors.toList());
        return ResponseEntity.ok(batchStocks);
    }
}
