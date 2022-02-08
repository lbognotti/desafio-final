package com.mercadolibre.desafio.api.services;

import com.mercadolibre.desafio.api.dtos.PurchaseOrderItemDTO;
import com.mercadolibre.desafio.api.entities.Buyer;
import com.mercadolibre.desafio.api.entities.ItemCart;
import com.mercadolibre.desafio.api.entities.PurchaseOrder;
import com.mercadolibre.desafio.api.exception.ApiException;
import com.mercadolibre.desafio.api.repositories.ItemCartRepository;
import com.mercadolibre.desafio.api.repositories.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final ItemCartRepository itemCartRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CartService cartService;
    private final SalesAdService salesAdService;
    private final BuyerService buyerService;

    public OrderService(ItemCartRepository itemCartRepository, PurchaseOrderRepository purchaseOrderRepository, CartService cartService, SalesAdService salesAdService, BuyerService buyerService) {
        this.itemCartRepository = itemCartRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.cartService = cartService;
        this.salesAdService = salesAdService;
        this.buyerService = buyerService;
    }

    public PurchaseOrder findOneOrFail(Long id) throws ApiException {
        return this.purchaseOrderRepository.findById(id).orElseThrow(() -> new ApiException("Not Found", "Pedido não encontrado", 404));
    }

    public List<ItemCart> getAllItems(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = this.findOneOrFail(purchaseOrderId);
        Buyer buyer = purchaseOrder.getBuyer();
        Long cartId = buyer.getCart().getId();
        return this.cartService.getItems(cartId);
    }

    public void validateIfAllSalesAdsExists(List<PurchaseOrderItemDTO> items) throws ApiException {
        List<Long> salesAdIds = items.stream()
                .map(PurchaseOrderItemDTO::getSalesAdId)
                .collect(Collectors.toList());
        if (!this.salesAdService.allExists(salesAdIds)) {
            throw new ApiException("Not Found", "Há anúncio(s) inexistente(s)", 404);
        }
    }

    public void validateItems(List<PurchaseOrderItemDTO> items) throws ApiException {
        this.validateIfAllSalesAdsExists(items);
        for (PurchaseOrderItemDTO item : items) {
            // Verifica se os anúncios possuem estoque suficiente
            Long quantityInStock = this.salesAdService.getQuantityInStock(item.getSalesAdId());
            if (item.getQuantity() > quantityInStock) {
                throw new ApiException("Bad Request",
                        "Anúncio "+ item.getSalesAdId() +" não tem produtos suficientes em estoque", 400);
            }

            // Verifica se o prazo de vencimento de é maior que 3 semanas
            boolean expires = this.salesAdService.expiresIn(item.getSalesAdId(), Duration.ofDays(21));
            if (expires) {
                throw new ApiException("Bad Request",
                        "Lote do anúncio "+ item.getSalesAdId() +" vence nas próximas 3 semanas", 400);
            }
        }
    }

    public void validateItemsToUpdate(List<PurchaseOrderItemDTO> items) throws ApiException {
        this.validateIfAllSalesAdsExists(items);
        for (PurchaseOrderItemDTO item : items) {
            // Verifica se os anúncios possuem estoque suficiente
            ItemCart itemCart = this.itemCartRepository.findById(item.getId()).get();
            Long quantityInStock = this.salesAdService.getQuantityInStock(item.getSalesAdId());
            Long totalInStock = itemCart.getQuantity() + quantityInStock;
            if (item.getQuantity() > totalInStock) {
                throw new ApiException("Bad Request",
                        "Anúncio "+ item.getSalesAdId() +" não tem produtos suficientes em estoque", 400);
            }

            // Verifica se o prazo de vencimento de é maior que 3 semanas
            boolean expires = this.salesAdService.expiresIn(item.getSalesAdId(), Duration.ofDays(21));
            if (expires) {
                throw new ApiException("Bad Request",
                        "Lote do anúncio "+ item.getSalesAdId() +" vence nas próximas 3 semanas", 400);
            }
        }
    }

    public List<ItemCart> convertItems(List<PurchaseOrderItemDTO> items) {
        List<ItemCart> itemCarts = new ArrayList<>();
        for (PurchaseOrderItemDTO item : items) {
            ItemCart newItemCart = ItemCart.builder()
                    .id(item.getId())
                    .quantity(item.getQuantity())
                    .salesAdId(item.getSalesAdId())
                    .build();
            itemCarts.add(newItemCart);
        }
        return itemCarts;
    }

    public Double registerPurchaseOrder(PurchaseOrder order, List<PurchaseOrderItemDTO> items) {
        this.validateItems(items);
        Buyer buyer = this.buyerService.findOneOrFail(order.getBuyer().getId());

        // Converte cada item para um ItemCart
        List<ItemCart> itemCartsToInsert = this.convertItems(items);

        // Salva os items no carrinho do comprador e qecrementa as quantidades atuais dos lotes
        this.buyerService.saveItemsInNewCart(buyer.getId(), itemCartsToInsert);

        // Salva o pedido de compra
        PurchaseOrder newOrder = PurchaseOrder.builder()
                .status(order.getStatus())
                .date(order.getDate())
                .build();
        newOrder.setBuyer(buyer);
        this.purchaseOrderRepository.save(newOrder);

        // Retorna o preço total
        return this.salesAdService.getTotalPriceByIds(items);
    }

    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder order, List<PurchaseOrderItemDTO> items) {
        this.validateItemsToUpdate(items);
        Buyer buyer = this.buyerService.findOneOrFail(order.getBuyer().getId());
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(id).orElseThrow(() -> new ApiException("Not Found", "Pedido nao cadastrado no sistema", 404));

        // Converte cada item para um ItemCart
        List<ItemCart> itemCarts = this.convertItems(items);
        itemCarts.forEach(itemCart -> this.itemCartRepository.findById(itemCart.getId()).orElseThrow(() -> new ApiException("Not Found", "Item nao cadastrado no sistema", 404)));

        // Salva os items no carrinho do comprador e qecrementa as quantidades atuais dos lotes
        this.cartService.updateItems(buyer.getId(), itemCarts);

        purchaseOrder.setBuyer(buyer);
        purchaseOrder.setStatus(order.getStatus());
        purchaseOrder.setDate(order.getDate());

        this.purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrder;
    }
}
