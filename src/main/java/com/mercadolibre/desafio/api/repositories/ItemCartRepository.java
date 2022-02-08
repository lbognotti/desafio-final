package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.ItemCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {
}
