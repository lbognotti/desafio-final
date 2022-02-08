package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.dtos.InsertBatchInFufillment;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.InboundOrder;
import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.entities.Section;
import com.mercadolibre.desafio.api.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public boolean validateWareHouseExist (Long wareHouseId) {
        boolean validate = this.warehouseService.existsByCode(wareHouseId);
        if (!validate) {
            throw new ApiException("Not Found", "Armazém não cadastrado no sistema", 404);
        }
        return validate;
    }

    public boolean validateSectionVolumeAvialiable (Double insertBatchInFufillmentVolumeAvaliable, Long sectionId)  {
        double sectionVolumeAvaliable = this.sectionService.getSectionVolumeAvaliable(sectionId);

        boolean validate = sectionVolumeAvaliable > insertBatchInFufillmentVolumeAvaliable;
        if (!validate) {
            //adicionar erro adequado
            throw new ApiException("Not Available", "Setor não tem capacidade de armazenamento", 400);
        }
        return validate;
    }

    public void validate(InsertBatchInFufillment insertBatchInFulfillmentData){
        Long wareHouseId = insertBatchInFulfillmentData.getWarehouseId();
        validateWareHouseExist(wareHouseId);

        Long sectionId = insertBatchInFulfillmentData.getSectionId();
        Optional<Section> optionSection = this.sectionService.findById(sectionId);
        if (optionSection.isEmpty()) {
            throw new ApiException("Not Found", "Setor não cadastrado no sistema", 404);
        }

        List<BatchStock> batchsStock = insertBatchInFulfillmentData.getBatchStock();
        for (BatchStock batch : batchsStock) {
            Product product = this.productService.getById(batch.getProduct().getId());
            if (!product.getCategory().equals(optionSection.get().getCategory())) throw new ApiException("Is Invalid", "A categoria do produto " + product.getName() + " esta invalido", 400);
        }

        double volumeTotalInbound = insertBatchInFulfillmentData
                .getBatchStock()
                .stream()
                .mapToDouble(stock ->  {
                    Long productId = stock.getProduct().getId();
                    Product product = this.productService.getById(productId);
                    return stock.getCurrentQuantity()*product.getVolume();
                })
                .sum()
                ;
        validateSectionVolumeAvialiable(volumeTotalInbound, sectionId);

    }

    public List<BatchStock> create(InsertBatchInFufillment insertBatchInFulfillmentData) {
        this.validate(insertBatchInFulfillmentData);
        Optional<Section> optionSection = this.sectionService.findById(insertBatchInFulfillmentData.getSectionId());

        InboundOrder inboundOrder = InboundOrder.builder()
                .date(insertBatchInFulfillmentData.getOrderDate())
                .section(optionSection.get())
                .build();

        InboundOrder inboundOrderSave = this.inboundOrderService.save(inboundOrder);
        List<BatchStock> batchsStock = insertBatchInFulfillmentData.getBatchStock();
        batchsStock.forEach(batchStock -> batchStock.setInboundOrder(inboundOrderSave));

        return this.batchStockService.saveAll(batchsStock);
    }

    public InboundOrder update(Long id, InsertBatchInFufillment updateBatchInFulfillmentData) {
        this.validate(updateBatchInFulfillmentData);
        InboundOrder inboundOrder = this.inboundOrderService.findById(id);
        Section section = this.sectionService.findById(updateBatchInFulfillmentData.getSectionId()).get();

        inboundOrder.setDate(updateBatchInFulfillmentData.getOrderDate());
        inboundOrder.setSection(section);

        InboundOrder inboundOrderUpdated = this.inboundOrderService.save(inboundOrder);

        List<BatchStock> batchsStock = updateBatchInFulfillmentData.getBatchStock();

        batchsStock.forEach(batchStock -> batchStock.setInboundOrder(inboundOrder));
        this.batchStockService.saveAll(batchsStock);
        inboundOrderUpdated.setBatchStocks(batchsStock);

        return inboundOrderUpdated;

    }

}
