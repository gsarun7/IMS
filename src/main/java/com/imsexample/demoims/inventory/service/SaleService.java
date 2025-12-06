package com.imsexample.demoims.inventory.service;



import com.imsexample.demoims.inventory.dto.SaleItemRequest;
import com.imsexample.demoims.inventory.dto.SaleRequest;
import com.imsexample.demoims.inventory.entity.Item;
import com.imsexample.demoims.inventory.entity.Sale;
import com.imsexample.demoims.inventory.entity.SaleItem;
import com.imsexample.demoims.inventory.repository.ItemRepository;
import com.imsexample.demoims.inventory.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SaleService {

    private final ItemRepository itemRepository;
    private final SaleRepository saleRepository;

    public SaleService(ItemRepository itemRepository, SaleRepository saleRepository) {
        this.itemRepository = itemRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional
    public Sale createSale(SaleRequest request) {
        // validate input
        if (request.items == null || request.items.isEmpty()) {
            throw new IllegalArgumentException("Sale must contain at least one item.");
        }

        Sale sale = new Sale();
        BigDecimal total = BigDecimal.ZERO;

        for (SaleItemRequest sir : request.items) {
            Optional<Item> oi = itemRepository.findById(sir.itemId);
            Item item = oi.orElseThrow(() -> new IllegalArgumentException("Item not found: " + sir.itemId));
            if (sir.qty == null || sir.qty <= 0) {
                throw new IllegalArgumentException("Qty must be > 0 for item " + sir.itemId);
            }

            if (item.getStock() < sir.qty) {
                throw new IllegalArgumentException("Not enough stock for item " + item.getName() + ". Available: " + item.getStock());
            }

            // reduce stock
            item.setStock(item.getStock() - sir.qty);
            itemRepository.save(item);

            // create sale item
            SaleItem si = new SaleItem();
            si.setItem(item);
            si.setQty(sir.qty);
            si.setPriceEach(item.getSellingPrice() != null ? item.getSellingPrice() : BigDecimal.ZERO);
            sale.addItem(si);

            total = total.add(si.getPriceEach().multiply(BigDecimal.valueOf(sir.qty)));
        }

        sale.setTotalAmount(total);
        Sale saved = saleRepository.save(sale);
        return saved;
    }
}
