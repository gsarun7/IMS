package com.imsexample.demoims.inventory.repository;



import com.imsexample.demoims.inventory.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> { }
