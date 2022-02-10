package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.BatchStockRequisito3DTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fresh-products/list")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping
//    public ResponseEntity<List<BatchStockRequisito3DTO>> getProducts(@RequestParam String productId) {
//        List<BatchStock> batchStocks = this.productService.findOneProduct(Long.parseLong(productId));
//        List<BatchStockRequisito3DTO> batchStockRequisito3DTOList = BatchStockRequisito3DTO.toList(batchStocks);
//        BatchStock batchStock = batchStocks.get(0);
//        return ResponseEntity.ok(batchStockRequisito3DTOList);
//    }

    @GetMapping
    public ResponseEntity<List<BatchStockRequisito3DTO>> getProducts(@RequestParam String productId) {
        List<BatchStock> batchStocks = this.productService.findOneProduct(Long.parseLong(productId));
        List<BatchStockRequisito3DTO> batchStockRequisito3DTOList = BatchStockRequisito3DTO.toList(batchStocks);
        batchStockRequisito3DTOList = batchStockRequisito3DTOList.stream().filter(batchStockRequisito3DTO -> batchStockRequisito3DTO.getCurrentQuantity()>3 && ).collect(Collectors.toList());
        BatchStock batchStock = batchStocks.get(0);
        return ResponseEntity.ok(batchStockRequisito3DTOList);
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
