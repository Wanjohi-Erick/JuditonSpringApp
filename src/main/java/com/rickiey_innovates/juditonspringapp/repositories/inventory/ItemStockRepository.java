package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemStockRepository extends JpaRepository<ItemStock, Integer> {
    @Query("select i from ItemStock i where i.farm = ?1")
    List<ItemStock> findByFarm(Integer Farm);
}