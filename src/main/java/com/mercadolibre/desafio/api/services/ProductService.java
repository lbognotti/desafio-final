package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.dtos.WarehouseQtyDTO;
import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    private WarehouseService warehouseService;

    public Product getById(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ApiException("Not Found", "Produto não cadastrado no sistema", 404));
    }

    public Category productCategory (Long productId){
        return this.productRepository.findById(productId).get().getCategory();
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
                    .filter(batchStock -> batchStock.getWarehouse().getId().equals(warehouseId))
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
