package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.ItemAdjustment;

import java.util.List;

public interface ItemAdjustmentRepository extends JpaRepository<ItemAdjustment, Integer> {
    @Query("select i from ItemAdjustment i where i.church = ?1")
    List<ItemAdjustment> findByChurch(Integer church);
}