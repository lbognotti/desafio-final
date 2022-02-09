package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.BatchStockMinDTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.services.BatchStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fresh-products/due-date")
public class BatchStockController {
    private final BatchStockService batchStockService;

    public BatchStockController(BatchStockService batchStockService) {
        this.batchStockService = batchStockService;
    }

    @GetMapping
    public ResponseEntity<List<BatchStockMinDTO>> findAllBatchStock(@RequestParam String sectionId, @RequestParam String dueDate) {
        List<BatchStock> batchStocks = this.batchStockService.findOneLoteDuedateBatchStock(Long.parseLong(sectionId), Long.parseLong(dueDate));
        List<BatchStockMinDTO> batchStockMinDTOS = batchStocks.stream()
                .map(BatchStockMinDTO::toBatchStockMinDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(batchStockMinDTOS);
    }

    @GetMapping("/list")
    public ResponseEntity<List<BatchStockMinDTO>>
    listByDueDateAndCategory(@RequestParam("days") Integer numberOfDays, @RequestParam("category") Category category) {
        List<BatchStock> batchStocks = this.batchStockService.listByCategoryExpiringIn(category, numberOfDays);
        List<BatchStockMinDTO> responseBody = batchStocks.stream()
                .map(BatchStockMinDTO::toBatchStockMinDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseBody);
    }
}
