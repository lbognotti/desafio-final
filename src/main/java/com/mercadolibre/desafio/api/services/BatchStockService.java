package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BatchStockRepisitory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchStockService {
    private final BatchStockRepisitory batchStockRepisitory;

    public BatchStockService(BatchStockRepisitory batchStockRepisitory) {
        this.batchStockRepisitory = batchStockRepisitory;
    }

    public List<BatchStock> saveAll(List<BatchStock> batchStocks) {
        return this.batchStockRepisitory.saveAll(batchStocks);
    }

    public BatchStock save(BatchStock batchStock) {
        return this.batchStockRepisitory.save(batchStock);
    }

    public BatchStock findById(Long id) {
        return this.batchStockRepisitory.findById(id).orElseThrow(()->new ApiException("Not Found", "Lote não cadastrado no sistema", 404));
    }
}
