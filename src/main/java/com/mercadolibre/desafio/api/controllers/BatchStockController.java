package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.BatchStockMinDTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.services.BatchStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fresh-products")
public class BatchStockController {

    private final BatchStockService batchStockService;

    @Autowired
    BatchStockController(BatchStockService batchStockService) {
        this.batchStockService = batchStockService;
    }

    @GetMapping("/due-date/list")
    public ResponseEntity<List<BatchStockMinDTO>>
    listByDueDateAndCategory(@RequestParam("days") Integer numberOfDays, @RequestParam("category") Category category) {
        List<BatchStock> batchStocks = this.batchStockService.listByCategoryExpiringIn(category, numberOfDays);
        List<BatchStockMinDTO> responseBody = batchStocks.stream()
                .map(BatchStockMinDTO::toBatchStockMinDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseBody);
    }
}
