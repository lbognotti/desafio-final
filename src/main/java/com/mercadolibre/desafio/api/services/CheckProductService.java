package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.entities.BatchStock;
import com.mercadolibre.desafio.api.entities.Product;
import com.mercadolibre.desafio.api.entities.Section;
import com.mercadolibre.desafio.api.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckProductService {

    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final SectionService sectionService;
    private final BatchStockService batchStockService;

    public CheckProductService (WarehouseService warehouseService, ProductService productService, SectionService sectionService, BatchStockService batchStockService)
    {
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.sectionService = sectionService;
        this.batchStockService = batchStockService;
    }

    //Valida se um warehouseexiste pelo ID
    //lanca exception
    private boolean validateWareHouseExist (Long wareHouseId) {
        boolean validate = this.warehouseService.existsByCode(wareHouseId);
        if (!validate) {
            throw new ApiException("Not Found", "Armazém não cadastrado no sistema", 404);
        }
        return validate;
    }

    //Valida se um warehouseexiste pelo ID
    //lanca exception
    private boolean validateWareHouseExist (BatchStock batchStock) {
        boolean validate = this.warehouseService.existsByCode(batchStock.getInboundOrder().getSection().getWarehouse().getId());
        if (!validate) {
            throw new ApiException("Not Found", "Armazém não cadastrado no sistema", 404);
        }
        return validate;
    }

    //PROVAVELMENTE NAO SERA USADO, ESTA SENDO SUBSTITUIDO
    //valida se um batchstock tem quantidade > 0.
    //provavelmente nao sera usado. Pois estamos filtrando na busca por quantidade > 0
    //Foi feito um metodo para validar o resultado da busca ja transformado em DTO.
    private   boolean validateBatchStockHasQuantity (BatchStock batchStock)
    {
        boolean validate = (batchStock.getCurrentQuantity() > 0);
        return validate;
    }

    //Valida se o produto esta registrado
    //Caso nao lanca uma excecao
    private boolean validateProductRegister (Long productId) {
        boolean validate = this.productService.existsByCode(productId);
        if (!validate) {
            throw new ApiException("Not Found", "O produto do Vendedor é registrado", 404);
        }
        return validate;
    }

    //Metodo que retorna se o produto tem validade de pelo menos 21 dias
    //Usado para filtrar na exibicao na busca por estoque
    private boolean validateDueDate21 (BatchStock batchStock)
    {
        boolean validate = batchStock.daysUntilDueDate() >=21;
        return validate;
    }
public List<BatchStock> execute (Long productId, Long warehouseId)
{
    validateProductRegister(productId);
    validateWareHouseExist(warehouseId);
    List<BatchStock> batchStockList= this.productService.findOneProduct(productId);
    List <BatchStock> batchFilterList = batchStockList
            .stream()
            .filter(batchStockIter -> (batchStockIter.getInboundOrder().getSection().getWarehouse().getId() == warehouseId) && (batchStockIter.getCurrentQuantity() >0) && validateDueDate21(batchStockIter) )
            .collect(Collectors.toList())
            ;

    return batchStockList;
}

}
