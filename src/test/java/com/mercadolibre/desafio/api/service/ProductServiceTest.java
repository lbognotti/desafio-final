package com.mercadolibre.desafio.api.service;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BatchStockRepisitory;
import com.mercadolibre.desafio.api.repositories.ProductRepository;
import com.mercadolibre.desafio.api.repositories.WarehouseRepository;
import com.mercadolibre.desafio.api.services.ProductService;
import com.mercadolibre.desafio.api.services.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private ProductService productService;

    public List<BatchStock> batchStocks;

    @Mock
    public ProductRepository productRepositoryMock;

    public Product createFakeProduct(){
        Product fakeProduct = Product.builder().id(3L).name("Arroz").build();
       return fakeProduct;
    }

    public BatchStock createFakeBatch1(){
        return BatchStock.builder()
                .id(1L)
                .code("3")
                .currentQuantity(10L)
                .product(Product.builder().id(1L).build())
                .dueDate(LocalDateTime.of(2016, Month.JULY, 29, 19, 30, 40))
                .build();
    }

    public BatchStock createFakeBatch2(){
        return BatchStock.builder()
                .id(3L)
                .code("2")
                .currentQuantity(70L)
                .product(Product.builder().id(1L).build())
                .dueDate(LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40))
                .build();
    }

    public BatchStock createFakeBatch3(){
        return BatchStock.builder()
                .id(3L)
                .code("1")
                .currentQuantity(50L)
                .product(Product.builder().id(1L).build())
                .dueDate(LocalDateTime.of(2018, Month.JULY, 29, 19, 30, 40))
                .build();
    }

    public List<BatchStock> batchStockList(){
        List<BatchStock> batchStocks = new ArrayList<>();
        batchStocks.add(createFakeBatch1());
        batchStocks.add(createFakeBatch2());
        batchStocks.add(createFakeBatch3());
        return batchStocks;
    }

    @BeforeEach
    public void init(){
        WarehouseRepository warehouseRepositoryMock = Mockito.mock(WarehouseRepository.class);
        BatchStockRepisitory batchStockRepositoryMock = Mockito.mock(BatchStockRepisitory.class);
        ProductRepository productRepositoryMock = Mockito.mock(ProductRepository.class);
        WarehouseService warehouseService = new WarehouseService(warehouseRepositoryMock);
        this.productService = new ProductService(productRepositoryMock, batchStockRepositoryMock, warehouseService);

        batchStocks = batchStockList();
    }


    @Test
    public void deveRetornarListaOrdenadaPorBatchstock(){
        // arrange
        this.productService.useOrder(batchStocks, "L");
        List<BatchStock> expectedResult = new ArrayList<>();
        expectedResult.add(createFakeBatch3());
        expectedResult.add(createFakeBatch2());
        expectedResult.add(createFakeBatch1());

        assertEquals(batchStocks, expectedResult);
    }

    @Test
    public void deveRetornarListaOrdenadaPorQuantidade(){
        // arrange
        this.productService.useOrder(batchStocks, "C");
        List<BatchStock> expectedResult = new ArrayList<>();
        expectedResult.add(createFakeBatch1());
        expectedResult.add(createFakeBatch3());
        expectedResult.add(createFakeBatch2());

        assertEquals(batchStocks, expectedResult);
    }

    @Test
    public void deveRetornarListaOrdenadaPorDataDeValidade(){
        // arrange
        this.productService.useOrder(batchStocks, "F");
        List<BatchStock> expectedResult = new ArrayList<>();
        expectedResult.add(createFakeBatch2());
        expectedResult.add(createFakeBatch1());
        expectedResult.add(createFakeBatch3());

        assertEquals(batchStocks, expectedResult);
    }


//    @Test
//    public void deveRetornarUmaApiExceptionQuandoNaoEncontraProduto() {
//        Product product = this.createFakeProduct();
//
//        Mockito.when(this.productService.getById(1L)).thenReturn(product);
//
//        ApiException nullProduct = assertThrows(ApiException.class, () -> this.productService.getById(3L));
//
//        nullProduct.getMessage();
//        assertTrue(false);
//    }

}
