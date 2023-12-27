package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.ItemGroup;

import java.util.List;

public interface ItemGroupRepository extends JpaRepository<ItemGroup, Integer> {
    @Query("select i from ItemGroup i where i.church = ?1")
    List<ItemGroup> findByChurch(Integer church);
}