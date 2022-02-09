package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BatchStockRepisitory;
import com.mercadolibre.desafio.api.repositories.SectionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatchStockService {
    private final BatchStockRepisitory batchStockRepository;
    private final SectionRepository sectionRepository;

    public BatchStockService(BatchStockRepisitory batchStockRepository, SectionRepository sectionRepository) {
        this.batchStockRepository = batchStockRepository;
        this.sectionRepository = sectionRepository;
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

    public List<BatchStock> findOneLoteDuedateBatchStock(Long sectionId, Long dueDate) {
        LocalDateTime date = LocalDateTime.now().plusDays(dueDate);
        List<BatchStock> batchStocks = this.sectionRepository.findBatchStock(sectionId);
        if (batchStocks.size() == 0) throw new ApiException("Not Null", "Setor nao cadastrado no sistema", 404);
        return batchStocks.stream().filter(batchStock -> batchStock.getDueDate().isBefore(date)).collect(Collectors.toList());
    }
    /**
     * Retorna uma lista de Lotes que vencem em um determinado número de dias.
     * @param numberOfDays números de dias para o vencimento
     * @return lista de lotes
     * @author Ronaldd Pinho
     */
    public List<BatchStock> listExpiringIn(Integer numberOfDays) {
        List<BatchStock> batchStocks = this.batchStockRepository.findAll();
        LocalDateTime until = LocalDateTime.now().plusDays(numberOfDays);

        List<BatchStock> batchStocksToExpires = batchStocks.stream()
                .filter(b -> b.getDueDate().isBefore(until))
                .collect(Collectors.toList());

        return batchStocksToExpires;
    }

    public List<BatchStock> listByCategoryExpiringIn(Category category, Integer days) {
        List<BatchStock> batchStocks = this.listExpiringIn(days);
        return batchStocks.stream()
                .filter(b -> b.getProduct().getCategory().equals(category))
                .collect(Collectors.toList());
    }
}