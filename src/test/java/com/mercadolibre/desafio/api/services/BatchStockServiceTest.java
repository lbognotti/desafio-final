package com.mercadolibre.desafio.api.services;

import com.github.javafaker.Faker;
import com.mercadolibre.desafio.api.entities.*;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BatchStockRepisitory;
import com.mercadolibre.desafio.api.repositories.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BatchStockServiceTest {
    private BatchStock fakeBatchStock;

    private BatchStockService batchStockService;

    @Mock
    private BatchStockRepisitory batchStockRepisitoryMock;

    @Mock
    private SectionRepository sectionRepositoryMock;

    private Faker faker;

    public Product createFakeProduct() {
        return Product.builder()
                .id(this.faker.number().randomNumber())
                .name(this.faker.food().vegetable())
                .category(Category.class.getEnumConstants()[this.faker.random().nextInt(0, 2)])
                .volume(this.faker.number().randomDouble(3, 1, 10))
                .build();
    }

    private Warehouse createFakeWarehouse() {
        return Warehouse.builder()
                .id(this.faker.number().randomNumber())
                .code(this.faker.number().digit())
                .build();
    }

    public Section createFakeSection() {
        return Section.builder()
                .id(this.faker.number().randomNumber())
                .code(this.faker.number().digit())
                .category(Category.class.getEnumConstants()[this.faker.random().nextInt(0, 2)])
                .volumeCapacity(1000.0)
                .warehouse(this.createFakeWarehouse())
                .build();
    }

    public InboundOrder createFakeInboundOrder() {
        return InboundOrder.builder()
                .id(this.faker.number().randomNumber())
                .date(LocalDateTime.now())
                .section(this.createFakeSection())
                .build();
    }

    public BatchStock createFakeBatchStock() {
        return BatchStock.builder()
                .id(this.faker.number().randomNumber())
                .code(this.faker.number().digit())
                .maximumTemperature(this.faker.number().randomDouble(3, 1, 100))
                .currentTemperature(this.faker.number().randomDouble(3, 1, 100))
                .minimumTemperature(this.faker.number().randomDouble(3, 1, 100))
                .initialQuantity(this.faker.number().randomNumber())
                .currentQuantity(this.faker.number().randomNumber())
                .manufacturingDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(21))
                .inboundOrder(this.createFakeInboundOrder())
                .product(this.createFakeProduct())
                .build();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.faker = new Faker();
        this.batchStockService = new BatchStockService(this.batchStockRepisitoryMock, this.sectionRepositoryMock);
    }

    @Test
    public void deveRetornarExceptionQuandoNaoEncontrarUmBatchStock() {
       Long id = this.fakeBatchStock.getId();
        //arrange
        Mockito.when(this.batchStockRepisitoryMock.findById(id)).thenReturn(null);
        //act
        ApiException exception = assertThrows(ApiException.class, () -> this.batchStockService.findOneOrFail(Mockito.anyLong()));
       //assertion
       assertEquals("Lote não encontrado", exception.getDescription());
    }

    @Test
    public void deveRetornarExpectionQuandoNaoEncontrarUmSetor() {
        List<BatchStock> batchStocks = new ArrayList<>();
        Long id = this.fakeBatchStock.getInboundOrder().getSection().getId();
        Mockito.when(this.sectionRepositoryMock.findBatchStock(id)).thenReturn(batchStocks);
        ApiException exception = assertThrows(ApiException.class, () -> this.batchStockService.findOneLoteDuedateBatchStock(Mockito.anyLong(), 1L));
        assertEquals("Setor nao cadastrado no sistema", exception.getDescription());
    }

    @Test
    public void deveRetornaUmaListaSalvaDeBatchStock() {
        List<BatchStock> batchStocks = new ArrayList<>();
        batchStocks.add(this.fakeBatchStock);
        Mockito.when(this.batchStockRepisitoryMock.saveAll(batchStocks)).thenReturn(batchStocks);
        List<BatchStock> batchStockResult = this.batchStockService.saveAll(batchStocks);
        assertEquals(batchStocks, batchStockResult);
    }

    @Test
    public void deveRetornaUmaListaOrdenadaCrescenteDeBatchStock() {
        List<BatchStock> batchStocks = new ArrayList<>();
        batchStocks.add(this.createFakeBatchStock());
        batchStocks.add(this.createFakeBatchStock());
        Mockito.when(this.batchStockRepisitoryMock.findAll()).thenReturn(batchStocks);
        List<BatchStock> result = this.batchStockService.listByCategoryExpiringIn(Category.valueOf(batchStocks.get(0).getProduct().getCategory().name()), 5, true);
        // continuar
    }
}
