package com.imsexample.demoims.inventory.repository;



import com.imsexample.demoims.inventory.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
