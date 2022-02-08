package com.mercadolibre.desafio.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastname;

    @OneToOne(mappedBy = "buyer")
    @JsonBackReference
    private Cart cart;

    @OneToOne(mappedBy = "buyer")
    @JsonBackReference
    private PurchaseOrder purchaseOrder;
}
