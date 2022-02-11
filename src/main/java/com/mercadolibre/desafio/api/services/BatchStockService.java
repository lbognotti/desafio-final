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
import java.util.Comparator;
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
        if (batchStock.isEmpty()) {
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
     * Ordena uma lista de Lotes por data de nascimento, da mais próxima para a mais distante.
     * @param batchStocks lista de lotes para ordenar.
     */
    private void sortBatchStocksByDueDateAsc(List<BatchStock> batchStocks) {
        Comparator<BatchStock> comparator = Comparator
                .comparing(BatchStock::getDueDate,
                        (date1, date2) -> date1.isBefore(date2) ? -1 : date1.equals(date2) ? 0 : 1);
        batchStocks.sort(comparator);
    }

    /**
     * Ordena uma lista de Lotes por data de nascimento, da mais distante para a mais próxima.
     * @param batchStocks lista de lotes para ordenar.
     */
    private void sortBatchStocksByDueDateDesc(List<BatchStock> batchStocks) {
        Comparator<BatchStock> comparator = Comparator
                .comparing(BatchStock::getDueDate,
                        (date1, date2) -> date1.isAfter(date2) ? -1 : date1.equals(date2) ? 0 : 1);
        batchStocks.sort(comparator);
    }

    /**
     * Retorna uma lista de Lotes que vencem em um determinado número de dias.
     *
     * @param numberOfDays números de dias para o vencimento
     * @return lista de lotes
     * @author Ronaldd Pinho
     */
    public List<BatchStock> listExpiringIn(Integer numberOfDays) {
        List<BatchStock> batchStocks = this.batchStockRepository.findAll();
        LocalDateTime until = LocalDateTime.now().plusDays(numberOfDays);

        return batchStocks.stream()
                .filter(b -> b.getDueDate().isBefore(until))
                .collect(Collectors.toList());
    }

    /**
     * Retorna uma lista de Lotes que vencem em um determinado número de dias filtrando por uma categoria de produto
     *
     * @param category categoria de produto
     * @param days número de dias até o vencimento
     * @return lista de lotes categorizada e ordenada por vencimento
     */
    public List<BatchStock> listByCategoryExpiringIn(Category category, Integer days, boolean ascendent) {
        List<BatchStock> batchStocks = this.listExpiringIn(days)
                .stream()
                .filter(b -> b.getProduct().getCategory().equals(category))
                .collect(Collectors.toList());

        if (ascendent) {
            this.sortBatchStocksByDueDateAsc(batchStocks);
        } else {
            this.sortBatchStocksByDueDateDesc(batchStocks);
        }

        return batchStocks;
    }
}
