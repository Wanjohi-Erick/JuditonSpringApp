package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemGroupRepository extends JpaRepository<ItemGroup, Integer> {
    @Query("select i from ItemGroup i where i.farm = ?1")
    List<ItemGroup> findByFarm(Integer Farm);
}