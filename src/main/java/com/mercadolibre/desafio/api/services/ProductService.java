package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BatchStockRepisitory;
import com.mercadolibre.desafio.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BatchStockRepisitory batchStockRepisitory;

    public ProductService(ProductRepository productRepository, BatchStockRepisitory batchStockRepisitory) {
        this.productRepository = productRepository;
        this.batchStockRepisitory = batchStockRepisitory;
    }

    public Product getById(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ApiException("Not Found", "Produto não cadastrado no sistema", 404));
    }

    public Category productCategory (Long productId){
        return this.productRepository.findById(productId).get().getCategory();
    }

    public List<BatchStock> findOneProduct(Long productId) {
         List<BatchStock> batchStock = this.batchStockRepisitory.findProducts(productId);
         if (batchStock.size() == 0) throw new ApiException("Not Found", "Produto não cadastrado no sistema", 404);
         return batchStock;
    }

    public Boolean existsByCode(Long productId) {
        return this.productRepository.existsById(productId);
    }
}
