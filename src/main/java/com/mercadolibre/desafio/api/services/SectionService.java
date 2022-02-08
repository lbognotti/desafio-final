package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.Section;
import com.mercadolibre.desafio.api.enums.Category;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Optional<Section> findById(Long sectionId) {
        return this.sectionRepository.findById(sectionId);
    }

    public double getSectionVolumeUsed (Long sectionId) {
        Section section = this.sectionRepository.findById(sectionId).orElseThrow(()->new ApiException("Not Found", "Setor não cadastrado no sistema", 404));
        return section.getVolume();
    }

    public double getSectionVolumeAvaliable(Long sectionId) {
        double volumeAvaliable = 0;
        Section section = this.sectionRepository.findById(sectionId).orElseThrow(()->new ApiException("Not Found", "Setor não cadastrado no sistema", 404));
        volumeAvaliable = section.getVolumeCapacity() - this.getSectionVolumeUsed(sectionId);
        return volumeAvaliable;
    }

    public Category sectionCategory (Long sectionId){
        return this.sectionRepository.findById(sectionId).get().getCategory();
    }
}
