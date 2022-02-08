package com.mercadolibre.desafio.api.controllers;

import com.mercadolibre.desafio.api.dtos.CartItemsResponseDTO;
import com.mercadolibre.desafio.api.dtos.CreatePurchaseOrderDTO;
import com.mercadolibre.desafio.api.dtos.ItemCartDTO;
import com.mercadolibre.desafio.api.dtos.PurchaseOrderItemDTO;
import com.mercadolibre.desafio.api.entities.ItemCart;
import com.mercadolibre.desafio.api.entities.PurchaseOrder;
import com.mercadolibre.desafio.api.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fresh-products/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Double>> registerPurchaseOrder(@RequestBody CreatePurchaseOrderDTO requestBody) throws URISyntaxException {
        PurchaseOrder order = requestBody.toPurchaseOrder();
        List<PurchaseOrderItemDTO> itens = requestBody.getProducts();
        Double price = this.orderService.registerPurchaseOrder(order, itens);
        Map<String, Double> responseBody = new HashMap<>();
        responseBody.put("totalPrice", price);
        return ResponseEntity.created(new URI("/orders")).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePurchaseOrder(@RequestBody CreatePurchaseOrderDTO requestBody, @PathVariable Long id) {
        PurchaseOrder order = requestBody.toPurchaseOrder();
        List<PurchaseOrderItemDTO> itens = requestBody.getProducts();
        PurchaseOrder purchaseOrder = this.orderService.updatePurchaseOrder(id, order, itens);
        return ResponseEntity.ok(purchaseOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CartItemsResponseDTO> listItemsByPurchaseOrder(@PathVariable("orderId") Long orderId) {
        List<ItemCart> itemCarts = this.orderService.getAllItems(orderId);

        CartItemsResponseDTO response = CartItemsResponseDTO.builder()
                .purchaseOrderId(orderId)
                .items(itemCarts.stream().map(ItemCartDTO::toItemCartDTO).collect(Collectors.toList()))
                .build();

        return ResponseEntity.ok(response);
    }
}
