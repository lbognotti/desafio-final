package com.mercadolibre.desafio.api.controllers;


import com.mercadolibre.desafio.api.dtos.ProductWarehousesDTO;
import com.mercadolibre.desafio.api.dtos.WarehouseQtyDTO;
import com.mercadolibre.desafio.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/fresh-products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/warehouses")
    public ResponseEntity<ProductWarehousesDTO> getAllWarehouses(@NotNull @RequestParam("productId") Long productId) {
        List<WarehouseQtyDTO> warehouseQuantities = this.productService.getQuantityByWarehouse(productId);
        ProductWarehousesDTO responseBody = ProductWarehousesDTO.builder()
                .productId(productId)
                .warehouses(warehouseQuantities)
                .build();
        return ResponseEntity.ok(responseBody);
    }
}
