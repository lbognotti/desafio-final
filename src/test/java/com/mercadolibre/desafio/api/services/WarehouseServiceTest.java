package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.repositories.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseServiceTest {

    private WarehouseService warehouseService;

    private WarehouseRepository warehouseRepositoryMock;

    @BeforeEach
    public void init(){
        this.warehouseRepositoryMock = Mockito.mock(WarehouseRepository.class);
        this.warehouseService = new WarehouseService(warehouseRepositoryMock);
    }

    @Test
    public void deveRetornarSeCodigoWarehouseExiste(){
        Mockito.when(this.warehouseRepositoryMock.existsById(5L)).thenReturn(true);
        assertEquals(warehouseService.existsByCode(5L),true);
    }

    @Test
    public void deveRetornarListaDeId(){
        List<Long> longList = new ArrayList<>();
        longList.add(1L);
        longList.add(2L);
        Mockito.when(this.warehouseRepositoryMock.findAllIds()).thenReturn(longList);
        assertEquals(this.warehouseService.getIds(),(longList));
    }
}