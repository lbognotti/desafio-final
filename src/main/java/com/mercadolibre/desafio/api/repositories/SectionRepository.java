package com.mercadolibre.desafio.api.repositories;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.InboundOrder;
import com.mercadolibre.desafio.api.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query("SELECT i FROM InboundOrder i WHERE i.section.id = :sectionId")
    List<InboundOrder> findInboundOrder(@Param("sectionId") Long sectionId);

    @Query("SELECT b FROM Section s INNER JOIN InboundOrder i ON s.id = i.section.id INNER JOIN BatchStock b ON i.id = b.inboundOrder.id WHERE s.id = :sectionId ORDER BY b.dueDate ASC")
    List<BatchStock> findBatchStock(@Param("sectionId") Long sectionId);

//    @Query("SELECT b FROM Section s INNER JOIN InboundOrder i ON s.id = i.section.id INNER JOIN BatchStock b ON i.id = b.inboundOrder.id WHERE s.id = :sectionId AND b.dueDate BETWEEN :dateNow AND :dateAfter")
//    List<BatchStock> findBatchStock(@Param("sectionId, dateNow, dateAfter") Long sectionId, LocalDateTime dateNow, LocalDateTime dateAfter);
}
