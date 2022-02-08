package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BatchStockRepisitory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BatchStockService {
    private final BatchStockRepisitory batchStockRepository;

    public BatchStockService(BatchStockRepisitory batchStockRepository) {
        this.batchStockRepository = batchStockRepository;
    }

    public BatchStock findOneOrFail(Long id) {
        return this.batchStockRepository.findById(id).orElseThrow(() -> new ApiException("Not Found", "Lote não encontrado", 404));
    }

    public List<BatchStock> saveAll(List<BatchStock> batchStocks) {
        return this.batchStockRepository.saveAll(batchStocks);
    }

    public boolean decrementQuantityInStock(Long batchStockId, Long quantityToDecrement) {
        BatchStock batchStock = this.findOneOrFail(batchStockId);
        Long quantityInStock = batchStock.getCurrentQuantity();

        if (quantityToDecrement > quantityInStock) {
            return false;
        }
        batchStock.setCurrentQuantity(quantityInStock - quantityToDecrement);
        this.batchStockRepository.save(batchStock);
        return true;
    }

    public long daysUntilDueDate(Long batchId) {
        Optional<BatchStock> batchStock = this.batchStockRepository.findById(batchId);
        if (!batchStock.isPresent()) {
            throw new ApiException("Not Found", "BatchStock não cadastrado no sistema", 404);
        }
        return Duration.between(LocalDate.now(), batchStock.get().getDueDate()).toDays();
    }
}
