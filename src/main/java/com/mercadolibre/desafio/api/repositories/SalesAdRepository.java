package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.SalesAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesAdRepository extends JpaRepository<SalesAd, Long> {
}
