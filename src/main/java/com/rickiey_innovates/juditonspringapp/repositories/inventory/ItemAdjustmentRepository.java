package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.ItemAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemAdjustmentRepository extends JpaRepository<ItemAdjustment, Integer> {
    @Query("select i from ItemAdjustment i where i.farm = ?1")
    List<ItemAdjustment> findByFarm(Integer Farm);
}