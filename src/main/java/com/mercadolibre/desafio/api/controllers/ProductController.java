package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.BatchStockRequisito3DTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/list")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<BatchStockRequisito3DTO>> getProducts(@RequestParam String productId) {
        List<BatchStock> batchStocks = this.productService.findOneProduct(Long.parseLong(productId));
        List<BatchStockRequisito3DTO> batchStockRequisito3DTOList = BatchStockRequisito3DTO.toList(batchStocks);
        return ResponseEntity.ok(batchStockRequisito3DTOList);
    }

    @GetMapping("/order")
    public ResponseEntity<List<BatchStockRequisito3DTO>> getProductCategory(@RequestParam Long productId, @RequestParam String order) {
        List<BatchStock> batchStocks = this.productService.findOneProductsCategory(productId, order);
        List<BatchStockRequisito3DTO> batchStockRequisito3DTOList = BatchStockRequisito3DTO.toList(batchStocks);
        return ResponseEntity.ok(batchStockRequisito3DTOList);
    }
}
