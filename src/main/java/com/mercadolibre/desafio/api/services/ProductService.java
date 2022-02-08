package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getById(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ApiException("Not Found", "Produto não cadastrado no sistema", 404));
    }

    public Category productCategory (Long productId){
        return this.productRepository.findById(productId).get().getCategory();
    }
}
