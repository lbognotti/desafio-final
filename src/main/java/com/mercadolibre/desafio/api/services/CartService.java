package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Cart;
import com.mercadolibre.desafio.api.entities.ItemCart;
import com.mercadolibre.desafio.api.repositories.CartRepository;
import com.mercadolibre.desafio.api.repositories.ItemCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final SalesAdService salesAdService;
    private final CartRepository cartRepository;
    private final ItemCartRepository itemCartRepository;

    public CartService(SalesAdService salesAdService, CartRepository cartRepository, ItemCartRepository itemCartRepository) {
        this.salesAdService = salesAdService;
        this.cartRepository = cartRepository;
        this.itemCartRepository = itemCartRepository;
    }

    public List<ItemCart> getItems(Long id) {
        return this.cartRepository.findItems(id);
    }

    public Cart save(Cart cart) {
        return this.cartRepository.save(cart);
    }

    public List<ItemCart> saveItems(Long cartId, List<ItemCart> items) {
        items.forEach(item -> item.setCartId(cartId));

        for (ItemCart item : items) {
            this.salesAdService.decrementFromStock(item.getSalesAdId(), item.getQuantity());
        }

        return this.itemCartRepository.saveAll(items);
    }

    public List<ItemCart> updateItems(Long cartId, List<ItemCart> items) {
        items.forEach(item -> item.setCartId(cartId));

        for (ItemCart item : items) {
            ItemCart databaseItem = this.itemCartRepository.findById(item.getId()).get();
            Long diff = item.getQuantity() - databaseItem.getQuantity();
            this.salesAdService.decrementFromStock(item.getSalesAdId(), diff);
        }

        return this.itemCartRepository.saveAll(items);
    }

    public void deleteCart(Long id) {
        this.cartRepository.deleteById(id);
    }

}
