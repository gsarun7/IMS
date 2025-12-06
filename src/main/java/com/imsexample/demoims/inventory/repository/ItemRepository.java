package com.imsexample.demoims.inventory.repository;


import com.imsexample.demoims.inventory.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.active = true")
    List<Item> findAllActive();
}
