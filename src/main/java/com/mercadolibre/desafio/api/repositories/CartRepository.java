package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.Cart;
import com.mercadolibre.desafio.api.entities.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT i FROM ItemCart i WHERE i.cartId = :cartId")
    List<ItemCart> findItems(@Param("cartId") Long cartId);
}
