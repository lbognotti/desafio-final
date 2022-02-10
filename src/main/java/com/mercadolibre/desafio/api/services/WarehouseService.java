package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseService( WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Boolean existsByCode(Long warehouseId) {
        return this.warehouseRepository.existsById(warehouseId);
    }

    /**
     * Retorna uma lista com todos os IDs de armazéns existentes no banco de dados
     * @return lista de IDs
     * @author Ronaldd Pinho
     */
    public List<Long> getIds() {
        return this.warehouseRepository.findAllIds();
    }
}
