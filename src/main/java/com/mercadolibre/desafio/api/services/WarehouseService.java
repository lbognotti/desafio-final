package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Warehouse;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseService( WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Boolean existsByCode(Long warehouseId) {
        return this.warehouseRepository.existsById(warehouseId);
    }
}
