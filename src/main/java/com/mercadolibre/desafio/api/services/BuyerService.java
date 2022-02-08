package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Buyer;
import com.mercadolibre.desafio.api.entities.Cart;
import com.mercadolibre.desafio.api.entities.ItemCart;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyerService {
    private final CartService cartService;
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository, CartService cartService) {
        this.cartService = cartService;
        this.buyerRepository = buyerRepository;
    }

    public Buyer findOneOrFail(Long buyerId) {
        return this.buyerRepository.findById(buyerId).orElseThrow(() -> new ApiException("Not Found", "Comprador não encontrado", 404));
    }

    public List<ItemCart> saveItemsInNewCart(Long buyerId, List<ItemCart> items) {
        Buyer buyer = this.findOneOrFail(buyerId);
        if (buyer.getCart() != null) {
            this.cartService.deleteCart(buyer.getCart().getId());
        }

        Cart newCart = this.cartService.save(Cart.builder().buyer(buyer).build());
        if (!items.isEmpty()) {
            List<ItemCart> saveditems = this.cartService.saveItems(newCart.getId(), items);

        }
        return items;
    }
}
