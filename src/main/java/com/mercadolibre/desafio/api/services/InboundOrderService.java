package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.InboundOrder;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.InboundOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class InboundOrderService {
    private final InboundOrderRepository inboundOrderRepository;

    public InboundOrderService (InboundOrderRepository inboundOrderRepository) {
        this.inboundOrderRepository = inboundOrderRepository;
    }

    public InboundOrder save (InboundOrder inboundOrder) {
        return this.inboundOrderRepository.save(inboundOrder);
    }

    public InboundOrder findById (Long id) {
        return this.inboundOrderRepository.findById(id).orElseThrow(()->new ApiException("Not Found", "Ordem de entrada não cadastrado no sistema", 404));
    }
}
