package com.mercadolibre.desafio.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    @NotNull
    @NotEmpty(message = "Id não pode estar vazio")
    private Long id;

    @NotNull
    @NotEmpty (message = "O codigo nao pode estar vazio")
    private String code;

    @NotNull
    @NotEmpty(message = "O campo armazem nao pode estar vazio. Por favor, insira o codigo do armazem")
    private String warehouseCode;

    @NotNull
    @NotEmpty (message = "O volume da secao nao pode estar vazio")
    @Digits(integer = 15, fraction = 2, message = "O volume nao pode ter mais de duas casas decimais")
    @Min(value = 0, message = "Uma secao nao pode ter tamanho negativo")
    private Double volumeCapacity;
}
