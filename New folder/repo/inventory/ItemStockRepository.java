package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.ItemStock;

import java.util.List;

public interface ItemStockRepository extends JpaRepository<ItemStock, Integer> {
    @Query("select i from ItemStock i where i.church = ?1")
    List<ItemStock> findByChurch(Integer church);
}