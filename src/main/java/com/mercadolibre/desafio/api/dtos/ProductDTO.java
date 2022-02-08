package com.mercadolibre.desafio.api.dtos;

import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotNull
    @NotEmpty(message = "Id não pode ser vazio")
    private Long id;

    @NotNull
    @NotEmpty(message = "nome do produto não pode ser vazio")
    private String name;

    @NotNull
    @NotEmpty(message = "volume do produto não pode ser vazio")
    private Double volume;

    @NotNull
    @NotEmpty(message = "categoria do produto não pode ser vazia")
    @Enumerated(value = EnumType.STRING)
    private Category category;

    public static ProductDTO productDTO(Product product) {
        return ProductDTO.builder()
                .name(product.getName())
                .volume(product.getVolume())
                .category(product.getCategory())
                .build();
    }

    public static Product toProduct(ProductDTO productDTO){
        return Product.builder()
                .name(productDTO.getName())
                .volume(productDTO.getVolume())
                .category(productDTO.getCategory())
                .build();
    }

    public static List<ProductDTO> toList(List<Product> products){
        return products.stream().map(ProductDTO::productDTO).collect(Collectors.toList());
    }
}
