package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
