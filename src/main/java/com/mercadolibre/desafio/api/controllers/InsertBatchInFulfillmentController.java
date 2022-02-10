package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.BatchStockDTO;
import com.mercadolibre.desafio.api.dtos.InboundOrderDTO;
import com.mercadolibre.desafio.api.dtos.InboundOrderRequestDTO;
import com.mercadolibre.desafio.api.dtos.InsertBatchInFufillment;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.InboundOrder;
import com.mercadolibre.desafio.api.services.InsertBatchInFulfillmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fresh-products/inboundorder")
public class InsertBatchInFulfillmentController {
    private final InsertBatchInFulfillmentService insertBatchInFulfillmentService ;

    public InsertBatchInFulfillmentController (InsertBatchInFulfillmentService insertBatchInFulfillmentService) {
        this.insertBatchInFulfillmentService = insertBatchInFulfillmentService;
    }

    @PostMapping()
    public ResponseEntity<List<BatchStockDTO>>
    registerInboundOrder(@Valid @RequestBody InboundOrderRequestDTO requestDto) throws URISyntaxException {
        InsertBatchInFufillment insertBatchInFufillmentData = InsertBatchInFufillment.fromInboundOrderDTO(requestDto.getInboundOrder());
        List<BatchStock> batchStocks = this.insertBatchInFulfillmentService.create(insertBatchInFufillmentData);
        List<BatchStockDTO> dtos = batchStocks.stream().map(BatchStockDTO::toBatchStockDTO).collect(Collectors.toList());
        return ResponseEntity.created(new URI("/fresh-products/inboundorder")).body(dtos);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<InboundOrderDTO>
    updateInboundOrder(@Valid @RequestBody InboundOrderRequestDTO inboundOrderDTO, @PathVariable Long id) {
        InsertBatchInFufillment insertBatchInFufillmentData = InsertBatchInFufillment.fromInboundOrderDTO(inboundOrderDTO.getInboundOrder());
        InboundOrder inboundOrderUpdated = this.insertBatchInFulfillmentService.update(id, insertBatchInFufillmentData);
        InboundOrderDTO dto = InboundOrderDTO.toInboundOrder(inboundOrderUpdated);
        return ResponseEntity.ok(dto);
    }
}
