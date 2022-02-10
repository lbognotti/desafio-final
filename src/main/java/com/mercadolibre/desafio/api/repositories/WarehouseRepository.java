package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT id FROM Warehouse")
    List<Long> findAllIds();
}
