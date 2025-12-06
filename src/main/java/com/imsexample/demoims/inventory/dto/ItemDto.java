package com.imsexample.demoims.inventory.dto;



import com.imsexample.demoims.inventory.entity.Category;

import java.math.BigDecimal;

public class ItemDto {
    public Long id;
    public String name;
    public Category category;
    public BigDecimal purchasePrice;
    public BigDecimal sellingPrice;
    public Integer stock;
}

