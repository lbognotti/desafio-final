package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Buyer;
import com.mercadolibre.desafio.api.entities.Cart;
import com.mercadolibre.desafio.api.entities.ItemCart;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BuyerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static  org.mockito.MockitoAnnotations.*;
import static org.junit.jupiter.api.Assertions.*;

public class BuyerServiceTest {

    private final Faker faker = new Faker();

    private BuyerService buyerService;

    @Mock
    private BuyerRepository buyerRepositoryMock;

    @Mock
    private CartService cartServiceMock;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        this.buyerService = new BuyerService(this.buyerRepositoryMock, this.cartServiceMock);
    }

    private Buyer createFakeBuyer() {
        return Buyer.builder()
                .id(this.faker.number().randomNumber())
                .name(this.faker.name().firstName())
                .lastname(this.faker.name().lastName())
                .cart(Cart.builder()
                        .id(this.faker.number().randomNumber())
                        .build())
                .build();
    }

    private Cart createFakeCart(Buyer buyer) {
        return Cart.builder()
                .id(this.faker.number().randomNumber())
                .buyer(buyer)
                .build();
    }

    private ItemCart createFakeItemCart() {
        return ItemCart.builder()
                .id(this.faker.number().randomNumber())
                .quantity(this.faker.number().numberBetween(2L, 10L))
                .salesAdId(this.faker.number().randomNumber())
                .build();
    }

    @Test
    public void shouldReturnABueyrWhenFindMethodPass() {
        Buyer fakeBuyer = this.createFakeBuyer();
        when(this.buyerRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.of(fakeBuyer));
        Buyer resultBuyer = this.buyerService.findOneOrFail(1L);
        assertEquals(fakeBuyer, resultBuyer);
    }

    @Test
    public void shouldReturnApiExceptionWhenFindMethodFails() {
        when(this.buyerRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.empty());
        ApiException e = assertThrows(ApiException.class, () -> this.buyerService.findOneOrFail(1L));

        assertEquals("Comprador não encontrado", e.getDescription());
    }

    @Test
    public void shouldReturnTheListOfItemCartsWhenSuccess() {
        Buyer buyer = this.createFakeBuyer();
        Cart cartMock = this.createFakeCart(buyer);
        List<ItemCart> itemsMock = new ArrayList<>();
        itemsMock.add(this.createFakeItemCart());
        itemsMock.add(this.createFakeItemCart());
        itemsMock.add(this.createFakeItemCart());

        when(this.buyerRepositoryMock.findById(anyLong())).thenReturn(Optional.of(buyer));
        doNothing().when(this.cartServiceMock).deleteCart(anyLong());
        when(this.cartServiceMock.save(isA(Cart.class))).thenReturn(cartMock);
        when(this.cartServiceMock.saveItems(cartMock.getId(), itemsMock)).thenReturn(itemsMock);

        List<ItemCart> resultItems = this.buyerService.saveItemsInNewCart(cartMock.getId(), itemsMock);
        assertEquals(itemsMock, resultItems);
    }
}
