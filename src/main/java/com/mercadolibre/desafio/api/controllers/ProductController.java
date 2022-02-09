package com.mercadolibre.desafio.api.controllers;

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
    public ResponseEntity<List<BatchStock>> getProducts(@RequestParam String productId) {
        List<BatchStock> batchStocks = this.productService.findOneProduct(Long.parseLong(productId));
        BatchStock batchStock = batchStocks.get(0);
        return ResponseEntity.ok(batchStocks);
    }
// , batchStock.getInboundOrder().getSection().getWarehouse().getId()
//    @GetMapping("/{category}")
//    public ResponseEntity<List<SalesAdDTO>> getProducts (@PathVariable Category category){
//        List<SalesAdDTO> filteredList = this.salesAdService.findAllSalesAdByCategory(category)
//                .stream().map(SalesAdDTO::toSalesAdDTO)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(filteredList);
//    }
}
