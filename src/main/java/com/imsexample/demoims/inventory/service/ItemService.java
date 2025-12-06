package com.imsexample.demoims.inventory.service;



import com.imsexample.demoims.inventory.dto.ItemDto;
import com.imsexample.demoims.inventory.entity.Category;
import com.imsexample.demoims.inventory.entity.Item;
import com.imsexample.demoims.inventory.repository.CategoryRepository;
import com.imsexample.demoims.inventory.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

//    public List<ItemDto> getAllItems() {
//        return itemRepository.findAll().stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
    public List<ItemDto> getAllItems() {
        return itemRepository.findAllActive().stream()
                .map(this::toDto)
                //.toList();
                .collect(Collectors.toList());
    }


    public ItemDto getItemById(Long id) {
        return itemRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Item not found: " + id));
    }

    public ItemDto createItem(ItemDto dto) {
        Item item = fromDto(dto);
        return toDto(itemRepository.save(item));
    }

    public ItemDto updateItem(Long id, ItemDto dto) {
        Item existing = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found: " + id));

        existing.setName(dto.name);
        existing.setPurchasePrice(new BigDecimal(dto.purchasePrice.toString()));
        existing.setSellingPrice(new BigDecimal(dto.sellingPrice.toString()));
        existing.setStock(dto.stock);

        Category category = categoryRepository.findById(dto.category.getId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + dto.category.getId()));
        existing.setCategory(category);

        return toDto(itemRepository.save(existing));
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        // Soft delete
        item.setActive(false);
        itemRepository.save(item);
    }

    // ===== Mapping Helpers ===== //
    private ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.id = item.getId();
        dto.name = item.getName();
        dto.category.setId(item.getCategory().getId());
        dto.category.setName(item.getCategory().getName());
        dto.purchasePrice = item.getPurchasePrice();
        dto.sellingPrice = item.getSellingPrice();
        dto.stock = item.getStock();
        return dto;
    }

    private Item fromDto(ItemDto dto) {
        Item item = new Item();
        item.setName(dto.name);
        item.setPurchasePrice(new BigDecimal(dto.purchasePrice.toString()));
        item.setSellingPrice(new BigDecimal(dto.sellingPrice.toString()));
        item.setStock(dto.stock);

        Category category = categoryRepository.findById(dto.category.getId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + dto.category.getId()));
        item.setCategory(category);

        return item;
    }
}
