package com.mercadolibre.desafio.api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mercadolibre.desafio.api.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private Double volumeCapacity;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @OneToMany
    @JsonManagedReference
    private List<InboundOrder> inboundOrders;

    @ManyToOne(fetch = FetchType.EAGER)
    private Warehouse warehouse;

    public Double getVolume() {
        Double volume = 0.0;
        for (InboundOrder inboundOrder : inboundOrders) {
            volume += inboundOrder.getVolume();
        }
        return volume;
    }

    public  List<BatchStock> retornaBatchStocksflat() {
        List<List<BatchStock>> batchStockLists = new ArrayList<>();
        for (InboundOrder b : this.inboundOrders) {
            List<BatchStock> batchStocks = b.getBatchStocks();
            batchStockLists.add(batchStocks);
        }
        List<BatchStock> flat = batchStockLists.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
        return flat;
    }
}
