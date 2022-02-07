package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.BatchStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchStockRepisitory extends JpaRepository<BatchStock, Long> {
}
