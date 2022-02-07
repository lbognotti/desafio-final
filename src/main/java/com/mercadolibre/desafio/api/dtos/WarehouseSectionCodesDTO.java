package com.mercadolibre.desafio.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseSectionCodesDTO {

    @JsonProperty("sectionId")
    @NotNull(message = "Código de setor não pode ser nulo")
    @NotEmpty(message = "Código de setor não pode ser vazio")
    @Min(value = 0L, message = "SectionId dever ser um numero")
    private Long sectionId;

    @JsonProperty("warehouseId")
    @NotNull(message = "Código de armazém não pode ser nulo")
    @NotEmpty(message = "Código de armazém não pode ser vazio")
    @Size(min = 12, max = 12, message = "Código deve ter 12 caracteres")
    @Min(value = 0L, message = "WareHouseId dever ser um numero")
    private Long warehouseId;
}
