package com.imsexample.demoims.inventory.controller;

import com.imsexample.demoims.inventory.entity.Category;
import com.imsexample.demoims.inventory.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repo;
    public CategoryController(CategoryRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Category> list() { return repo.findAll(); }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category c) {
        Category saved = repo.save(c);
        return ResponseEntity.ok(saved);
    }
}
