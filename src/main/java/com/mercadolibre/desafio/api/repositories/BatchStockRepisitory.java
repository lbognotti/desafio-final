package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchStockRepisitory extends JpaRepository<BatchStock, Long> {
    @Query("SELECT b FROM BatchStock b WHERE b.product.id = :productId")
    List<BatchStock> findProducts(@Param("productId") Long productId);
}
