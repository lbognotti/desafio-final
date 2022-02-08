package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.dtos.PurchaseOrderItemDTO;
import com.mercadolibre.desafio.api.entities.SalesAd;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.SalesAdRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesAdService {
    private final SalesAdRepository salesAdRepository;
    private final BatchStockService batchStockService;

    public SalesAdService(SalesAdRepository salesAdRepository, BatchStockService batchStockService) {
        this.salesAdRepository = salesAdRepository;
        this.batchStockService = batchStockService;
    }

    public SalesAd findOne(Long id) {
        return this.salesAdRepository.findById(id).orElseThrow(() -> new ApiException("Not Fund", "Anúncio não encontrado", 404));
    }

    public Double getTotalPriceByIds(List<PurchaseOrderItemDTO> items) {
        return items.stream().map(item -> { SalesAd ad = this.findOne(item.getSalesAdId());
            return ad.getPrice().doubleValue() * item.getQuantity(); }).reduce(Double::sum).get();
    }

    public boolean allExists(List<Long> ids) {
        for (Long id : ids) {
            if (!this.salesAdRepository.existsById(id)) {
                return false;
            }
        }
        return true;
    }

    public Long getQuantityInStock(Long id) {
        SalesAd ad = this.findOne(id);
        return ad.getBatchStock().getCurrentQuantity();
    }

    public boolean decrementFromStock(Long id, Long quantityToDecrement) {
        SalesAd ad = this.findOne(id);
        Long batchStockId = ad.getBatchStock().getId();
        return this.batchStockService.decrementQuantityInStock(batchStockId, quantityToDecrement);
    }

    public boolean expiresIn(Long salesAdId, Duration duration) {
        SalesAd ad = this.findOne(salesAdId);
        LocalDateTime dueDate = ad.getBatchStock().getDueDate();
        Duration timeUntilExpiration = Duration.between(LocalDateTime.now(), dueDate);
        return timeUntilExpiration.compareTo(duration) <= 0;
    }

    public List<SalesAd> findAllSalesAd() {
        List<SalesAd> salesAdList = this.salesAdRepository.findAll();
        if (salesAdList.isEmpty()){
            throw new ApiException("Not found", "Lista de produtos não encontrada", 404);
        }
        return salesAdList;
    }

    public List<SalesAd> findAllSalesAdByCategory(Category category){
        List<SalesAd> salesAdList = findAllSalesAd();
        salesAdList = salesAdList
                .stream()
                .filter(a -> a.getBatchStock().getProduct().getCategory() == category)
                .collect(Collectors.toList());
        if (salesAdList.isEmpty()){
            throw new ApiException("Not found", "Lista de produtos não encontrada", 404);
        }
        return salesAdList;
    }
}
