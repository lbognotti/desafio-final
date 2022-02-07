package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.dtos.InsertBatchInFufillment;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.InboundOrder;
import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.entities.Section;
import com.mercadolibre.desafio.api.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsertBatchInFulfillmentService {
    private final SectionService sectionService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final InboundOrderService inboundOrderService;
    private final BatchStockService batchStockService;

    public InsertBatchInFulfillmentService(SectionService sectionService, WarehouseService warehouseService, ProductService productService, InboundOrderService inboundOrderService, BatchStockService batchStockService) {
        this.sectionService = sectionService;
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.inboundOrderService = inboundOrderService;
        this.batchStockService = batchStockService;
    }

    public List<BatchStock> create(InsertBatchInFufillment insertBatchInFufillment) {
        this.warehouseService.findById(insertBatchInFufillment.getWarehouseId());
        Section section = this.sectionService.findById(insertBatchInFufillment.getSectionId());

        List<BatchStock> batchStocks = insertBatchInFufillment.getBatchStock();
        for (BatchStock batchStock : batchStocks) {
            Product product = this.productService.findById(batchStock.getProduct().getId());

            if (!product.getCategory().equals(section.getCategory())) throw new ApiException("Is Invalid", "A categoria do produto " + product.getName() + " esta invalido", 400);
        }

        InboundOrder inboundOrder = InboundOrder.builder()
                .date(insertBatchInFufillment.getOrderDate())
                .section(section)
                .build();

        InboundOrder inboundOrderSave = this.inboundOrderService.save(inboundOrder);

        batchStocks.forEach(batchStock -> batchStock.setInboundOrder(inboundOrderSave));

        return this.batchStockService.saveAll(batchStocks);
    }

    public List<BatchStock> update(Long id, InsertBatchInFufillment updateBatchInFulfillment) {
        InboundOrder inboundOrder = this.inboundOrderService.findById(id);
        Section section = this.sectionService.findById(updateBatchInFulfillment.getSectionId());

        inboundOrder.setDate(updateBatchInFulfillment.getOrderDate());
        inboundOrder.setSection(section);

        this.inboundOrderService.save(inboundOrder);

        List<BatchStock> batchStocks = updateBatchInFulfillment.getBatchStock();
        for (BatchStock batchStock : batchStocks) {
            this.batchStockService.findById(batchStock.getId());

            this.batchStockService.save(batchStock);
        }

       return batchStocks;
    }

    public BatchStock findOne(Long id) {
        return this.batchStockService.findById(id);
    }
}
