package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.InboundOrderRequestDTO;
import com.mercadolibre.desafio.api.dtos.InsertBatchInFufillment;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.services.InsertBatchInFulfillmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/inboundorder")
public class InsertBatchInFulfillmentController {
    private final InsertBatchInFulfillmentService insertBatchInFulfillmentService ;

    public InsertBatchInFulfillmentController (InsertBatchInFulfillmentService insertBatchInFulfillmentService) {
        this.insertBatchInFulfillmentService = insertBatchInFulfillmentService;
    }

    @PostMapping()
    public ResponseEntity<List<BatchStock>> insertInboundOrder(@Valid @RequestBody InboundOrderRequestDTO inboundOrderRequestDTO) {
        InsertBatchInFufillment insertBatchInFufillment = InsertBatchInFufillment.fromInboundOrderDTO(inboundOrderRequestDTO.getInboundOrder());
        List<BatchStock> batchStocks = this.insertBatchInFulfillmentService.create(insertBatchInFufillment);
        System.out.println(batchStocks);
        return ResponseEntity.ok(batchStocks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchStock> findOne(@PathVariable Long id) {
       return ResponseEntity.ok(this.insertBatchInFulfillmentService.findOne(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<List<BatchStock>> updateInboundOrder(@Valid @RequestBody InboundOrderRequestDTO inboundOrderRequestDTO, @PathVariable Long id) {
        InsertBatchInFufillment insertBatchInFufillment = InsertBatchInFufillment.fromInboundOrderDTO(inboundOrderRequestDTO.getInboundOrder());
        List<BatchStock> batchStocks = this.insertBatchInFulfillmentService.update(id, insertBatchInFufillment);
        return ResponseEntity.ok(batchStocks);
    }
}
