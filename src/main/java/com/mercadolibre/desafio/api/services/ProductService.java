package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.dtos.WarehouseQtyDTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BatchStockRepisitory;
import com.mercadolibre.desafio.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BatchStockRepisitory batchStockRepisitory;
    private WarehouseService warehouseService;

    public ProductService(ProductRepository productRepository, BatchStockRepisitory batchStockRepisitory, WarehouseService warehouseService) {
        this.productRepository = productRepository;
        this.batchStockRepisitory = batchStockRepisitory;
        this.warehouseService = warehouseService;
    }

    public Product getById(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ApiException("Not Found", "Produto não cadastrado no sistema", 404));
    }

    public Category productCategory (Long productId) {
        return this.productRepository.findById(productId).get().getCategory();
    }

    public List<BatchStock> findOneProduct(Long productId) {
        List<BatchStock> batchStocks = this.batchStockRepisitory.findProducts(productId);
        if (batchStocks.size() == 0) throw new ApiException("Not Found", "Produto não cadastrado no sistema", 404);
         List<BatchStock> batchStocksFilter  = batchStocks.stream()
                 .filter(batchStock -> batchStock.getCurrentQuantity() != 0)
                 .filter(batchStock -> batchStock.getDueDate().isAfter(LocalDateTime.now().plusDays(21)))
                 .collect(Collectors.toList());
         if (batchStocksFilter.size() == 0) throw new ApiException("Not Found", "Nenhum produto nao encontrado no estoque", 404);
         return batchStocksFilter;
    }

    public List<BatchStock> findOneProductsCategory(Long productId, String orderValue) {
        List<BatchStock> batchStocks = this.findOneProduct(productId);
        return this.useOrder(batchStocks, orderValue);
    }

    public static List<BatchStock> orderByBatch(List<BatchStock> sortList) {
        Comparator<BatchStock> comparatorByBatch = Comparator.comparing(batchStock -> Integer.parseInt(batchStock.getCode()));
        sortList.sort(comparatorByBatch);
        return sortList;
    }

    public static List<BatchStock> orderByCurrentQuantity(List<BatchStock> sortList) {
        Comparator<BatchStock> comparatorByBatch = Comparator.comparing(BatchStock::getCurrentQuantity);
        sortList.sort(comparatorByBatch);
        return sortList;
    }

    public static List<BatchStock> orderByDueDate(List<BatchStock> sortList) {
        Comparator<BatchStock> comparatorByBatch = Comparator.comparing(BatchStock::getDueDate);
        sortList.sort(comparatorByBatch);
        return sortList;
    }

    public List<BatchStock> useOrder(List<BatchStock> batchList, String orderValue) {
        switch (orderValue) {
            case "L":
                orderByBatch(batchList);
                break;
            case "C":
                orderByCurrentQuantity(batchList);
                break;
            case "F":
                orderByDueDate(batchList);
                break;
            default:
                return batchList;
        }
        return batchList;
    }

    public Boolean existsByCode(Long productId) {
        return this.productRepository.existsById(productId);
    }

    /**
     * Busca um produto pelo seu ID e então conta a quantidade existente em estoque desse produto em cada armazém
     * existente.
     * @param productId ID do produto.
     * @return Lista de objetos com o ID do armazém e a quantidade.
     * @author Ronaldd Pinho.
     */
    public List<WarehouseQtyDTO> getQuantityByWarehouse(Long productId) {
        if (!this.productRepository.existsById(productId)) {
            throw  new ApiException("Not Found", "Produto não encontrado", 404);
        }
        List<BatchStock> batchStocks = this.productRepository.findAllBatchStocks(productId);
        List<Long> warehouseIds = this.warehouseService.getIds();

        List<WarehouseQtyDTO> quantities = new ArrayList<>();

        for (Long warehouseId : warehouseIds) {
            long quantity = batchStocks.stream()
                    .filter(batchStock -> batchStock.getInboundOrder().getSection().getWarehouse().getId().equals(warehouseId))
                    .mapToLong(BatchStock::getCurrentQuantity)
                    .reduce(Long::sum)
                    .orElse(0L);

            if (quantity == 0L) continue;

            WarehouseQtyDTO quantityDto = WarehouseQtyDTO.builder()
                    .warehouseId(warehouseId)
                    .totalQuantity(quantity)
                    .build();

            quantities.add(quantityDto);
        }

        return quantities;
    }
}
