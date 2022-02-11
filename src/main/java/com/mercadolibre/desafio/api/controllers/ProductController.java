package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.BatchStockRequisito3DTO;
import com.mercadolibre.desafio.api.dtos.SalesAdDTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.services.ProductService;
import com.mercadolibre.desafio.api.services.SalesAdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mercadolibre.desafio.api.dtos.WarehouseQtyDTO;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import com.mercadolibre.desafio.api.dtos.ProductWarehousesDTO;

@RestController
@RequestMapping("/fresh-products")
public class ProductController {
    private final ProductService productService;

    private final SalesAdService salesAdService;

    public ProductController(ProductService productService,  SalesAdService salesAdService) {
        this.productService = productService;
        this.salesAdService = salesAdService;
    }

    @GetMapping
    public ResponseEntity<List<BatchStockRequisito3DTO>> getProducts(@RequestParam String productId) {
        List<BatchStock> batchStocks = this.productService.findOneProduct(Long.parseLong(productId));
        List<BatchStockRequisito3DTO> batchStockRequisito3DTOList = BatchStockRequisito3DTO.toList(batchStocks);
        return ResponseEntity.ok(batchStockRequisito3DTOList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SalesAdDTO>> getProducts (@RequestParam Category category) {
        List<SalesAdDTO> filteredList = this.salesAdService.findAllSalesAdByCategory(category)
                .stream().map(SalesAdDTO::toSalesAdDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredList);
    }

    @GetMapping("/order")
    public ResponseEntity<List<BatchStockRequisito3DTO>> getProductCategory(@RequestParam Long productId, @RequestParam String order) {
        List<BatchStock> batchStocks = this.productService.findOneProductsCategory(productId, order);
        List<BatchStockRequisito3DTO> batchStockRequisito3DTOList = BatchStockRequisito3DTO.toList(batchStocks);
        return ResponseEntity.ok(batchStockRequisito3DTOList);

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
