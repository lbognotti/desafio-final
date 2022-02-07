package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(()->new ApiException("Not Found", "Produto não cadastrado no sistema", 404));
    }
}
