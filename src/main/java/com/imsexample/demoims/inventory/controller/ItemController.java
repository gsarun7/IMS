package com.imsexample.demoims.inventory.controller;



import com.imsexample.demoims.inventory.dto.ItemDto;
import com.imsexample.demoims.inventory.entity.Category;
import com.imsexample.demoims.inventory.entity.Item;
import com.imsexample.demoims.inventory.repository.CategoryRepository;
import com.imsexample.demoims.inventory.repository.ItemRepository;
import com.imsexample.demoims.inventory.service.ItemService;
import com.imsexample.demoims.inventory.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemRepository itemRepo;
    private final CategoryRepository categoryRepo;
    private final ItemService itemService;

    public ItemController(ItemRepository itemRepo, CategoryRepository categoryRepo, ItemService itemService) {
        this.itemRepo = itemRepo;
        this.categoryRepo = categoryRepo;
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> list() {
        return itemRepo.findAllActive();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> get(@PathVariable Long id) {
        Optional<Item> oi = itemRepo.findById(id);
        return oi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Item> create(@RequestBody ItemDto dto) {
        Item it = new Item();
        it.setName(dto.name);
        if(dto.category != null && dto.category.getId() != null) {
            Category category = categoryRepo.findById(dto.category.getId()).orElse(null);
            it.setCategory(category);
        }
        it.setPurchasePrice(dto.purchasePrice != null ? dto.purchasePrice : BigDecimal.ZERO);
        it.setSellingPrice(dto.sellingPrice != null ? dto.sellingPrice : BigDecimal.ZERO);
        it.setStock(dto.stock != null ? dto.stock : 0);
        Item saved = itemRepo.save(it);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody ItemDto dto) {
        Optional<Item> oi = itemRepo.findById(id);
        if (oi.isEmpty()) return ResponseEntity.notFound().build();
        Item it = oi.get();
        it.setName(dto.name != null ? dto.name : it.getName());
        if (dto.category.getId() != null) {
            Category c = categoryRepo.findById(dto.category.getId()).orElse(null);
            it.setCategory(c);
        }
        if (dto.purchasePrice != null) it.setPurchasePrice(dto.purchasePrice);
        if (dto.sellingPrice != null) it.setSellingPrice(dto.sellingPrice);
        if (dto.stock != null) it.setStock(dto.stock);
        itemRepo.save(it);
        return ResponseEntity.ok(it);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.ok().body("Item deleted successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error deleting item: " + ex.getMessage());
        }
    }

}
