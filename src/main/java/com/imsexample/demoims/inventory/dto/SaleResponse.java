package com.imsexample.demoims.inventory.dto;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleResponse {
    public Long id;
    public LocalDateTime saleDate;
    public BigDecimal totalAmount;
    public List<SaleLine> items;

    public static class SaleLine {
        public Long itemId;
        public String itemName;
        public Integer qty;
        public BigDecimal priceEach;
    }
}
