package com.mercadolibre.desafio.api.services;

import com.github.javafaker.Faker;
import com.mercadolibre.desafio.api.dtos.PurchaseOrderItemDTO;
import com.mercadolibre.desafio.api.entities.SalesAd;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.SalesAdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SalesAdServiceTest {

    private final Faker faker = new Faker();

    private SalesAdService salesAdService;

    @Mock
    private SalesAdRepository salesAdRepositoryMock;

    @Mock
    private BatchStockService batchStockServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.salesAdService = new SalesAdService(this.salesAdRepositoryMock, this.batchStockServiceMock);
    }

    public SalesAd createFakeSalesAd() {
        return SalesAd.builder()
                .id(this.faker.number().randomNumber())
                .price(BigDecimal.valueOf(this.faker.number().randomDouble(2, 10L, 2000L)))
                .title(this.faker.name().title())
                .build();
    }

    public PurchaseOrderItemDTO createFakePurchaseOrderItemDTO() {
        return PurchaseOrderItemDTO.builder()
                .salesAdId(this.faker.number().randomNumber())
                .quantity(this.faker.number().numberBetween(10L, 20L))
                .build();
    }

    @Test
    public void shouldReturnSalesAdWhenFindOnePass() {
        SalesAd fakeAd = this.createFakeSalesAd();
        when(this.salesAdRepositoryMock.findById(anyLong())).thenReturn(Optional.of(fakeAd));
        assertEquals(fakeAd, this.salesAdService.findOne(1L));
    }

    @Test
    public void shouldReturnSalesAdWhenFindOneFails() {
        when(this.salesAdRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        ApiException ex = assertThrows(ApiException.class, () -> this.salesAdService.findOne(1L));
        assertEquals("Anúncio não encontrado", ex.getDescription());
    }

    @Test
    public void shouldReturnsTheSumOfPrices() {
        List<SalesAd> salesAdListMock = new ArrayList<>();
        salesAdListMock.add(this.createFakeSalesAd());
        salesAdListMock.add(this.createFakeSalesAd());
        salesAdListMock.add(this.createFakeSalesAd());

        when(this.salesAdRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.of(salesAdListMock.get(0)))
                .thenReturn(Optional.of(salesAdListMock.get(1)))
                .thenReturn(Optional.of(salesAdListMock.get(2)));

        List<PurchaseOrderItemDTO> itemListMock = new ArrayList<>();
        itemListMock.add(this.createFakePurchaseOrderItemDTO());
        itemListMock.add(this.createFakePurchaseOrderItemDTO());
        itemListMock.add(this.createFakePurchaseOrderItemDTO());
        Double expectedSum = 0.0;
        for (int i = 0; i < salesAdListMock.size(); i++) {
            expectedSum += salesAdListMock.get(i).getPrice().doubleValue() * itemListMock.get(i).getQuantity();
        }

        Double resultSum = this.salesAdService.getTotalPriceByIds(itemListMock);
        assertEquals(expectedSum, resultSum);
    }

    @Test
    public void shouldReturnTrueWhenAllIdsExists() {
        List<Long> mockedIds = new ArrayList<>();
        mockedIds.add(this.faker.number().randomNumber());
        mockedIds.add(this.faker.number().randomNumber());
        mockedIds.add(this.faker.number().randomNumber());
        mockedIds.add(this.faker.number().randomNumber());
        when(this.salesAdRepositoryMock.existsById(anyLong())).thenReturn(true);
        assertTrue(this.salesAdService.allExists(mockedIds));
    }

    @Test
    public void shouldReturnFalseWhenOneIdNotExists() {
        List<Long> mockedIds = new ArrayList<>();
        mockedIds.add(this.faker.number().randomNumber());
        mockedIds.add(this.faker.number().randomNumber());
        mockedIds.add(this.faker.number().randomNumber());
        mockedIds.add(this.faker.number().randomNumber());

        when(this.salesAdRepositoryMock.existsById(anyLong()))
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(true);

        assertFalse(this.salesAdService.allExists(mockedIds));
    }
}
