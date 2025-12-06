package com.imsexample.demoims.inventory.controller;


import com.imsexample.demoims.inventory.dto.SaleRequest;
import com.imsexample.demoims.inventory.dto.SaleResponse;
import com.imsexample.demoims.inventory.entity.Sale;
import com.imsexample.demoims.inventory.service.SaleService;
import com.imsexample.demoims.inventory.repository.SaleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;
    private final SaleRepository saleRepository;

    public SaleController(SaleService saleService, SaleRepository saleRepository) {
        this.saleService = saleService;
        this.saleRepository = saleRepository;
    }

    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleRequest request) {
        try {
            Sale saved = saleService.createSale(request);
            return ResponseEntity.ok(buildResponse(saved));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error creating sale: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSale(@PathVariable Long id) {
        return saleRepository.findById(id)
                .map(s -> ResponseEntity.ok(buildResponse(s)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private SaleResponse buildResponse(Sale s) {
        SaleResponse r = new SaleResponse();
        r.id = s.getId();
        r.saleDate = s.getSaleDate();
        r.totalAmount = s.getTotalAmount();
        r.items = s.getItems().stream().map(si -> {
            SaleResponse.SaleLine line = new SaleResponse.SaleLine();
            line.itemId = si.getItem().getId();
            line.itemName = si.getItem().getName();
            line.qty = si.getQty();
            line.priceEach = si.getPriceEach();
            return line;
        }).collect(Collectors.toList());
        return r;
    }
}

