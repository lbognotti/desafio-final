package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundOrderRepository extends JpaRepository<InboundOrder, Long> {
}
