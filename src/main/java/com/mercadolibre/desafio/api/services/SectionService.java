package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Section;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.SectionRepository;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section findById(Long sectionId) {
        return this.sectionRepository.findById(sectionId).orElseThrow(()->new ApiException("Not Found", "Setor não cadastrado no sistema", 404));
    }
}
