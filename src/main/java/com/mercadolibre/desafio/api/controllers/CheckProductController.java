package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.BatchStockRequisito3DTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.services.CheckProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/list1")
public class CheckProductController {

    private final CheckProductService checkProductService;


    public CheckProductController(CheckProductService checkProductService) {
        this.checkProductService = checkProductService;
    }

    @GetMapping
    public ResponseEntity<List<BatchStockRequisito3DTO>> getProducts(@RequestParam String productId) {
        Long warehouseId = 1L;
        List<BatchStock> batchStocks = this.checkProductService.execute(Long.parseLong(productId),1L);
        List<BatchStockRequisito3DTO> batchStockRequisito3DTOList = BatchStockRequisito3DTO.toList(batchStocks);
        BatchStock batchStock = batchStocks.get(0);
        return ResponseEntity.ok(batchStockRequisito3DTOList);
    }
}
