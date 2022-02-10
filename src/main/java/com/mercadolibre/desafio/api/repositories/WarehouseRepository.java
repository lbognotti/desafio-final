package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.dtos.WarehouseQtyDTO;
import com.mercadolibre.desafio.api.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("SELECT id FROM Warehouse")
    List<Long> findAllIds();

    @Query("SELECT w, p FROM Warehouse w INNER JOIN Section s ON w.id = s.warehouse.id INNER JOIN InboundOrder i ON s.id = i.section.id INNER JOIN BatchStock b ON i.id = b.inboundOrder.id INNER JOIN Product p ON b.product.id = p.id WHERE p.id = :productId")
    List<WarehouseQtyDTO> findAllProductId(@Param("productId") Long productId);
}
