package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.IssuedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssuedItemRepository extends JpaRepository<IssuedItem, Integer> {
    @Query("select i from IssuedItem i where i.itemId = ?1 and i.status = ?2 and i.farm = ?3")
    IssuedItem findByItemIdAndStatusAndFarm(Integer itemId, String status, Integer Farm);
    @Query("SELECT ii, i FROM IssuedItem ii INNER JOIN Item i ON ii.itemId = i.id WHERE ii.farm = ?1 AND i.farm = ?1 AND ii.status = 'Not Returned'")
    List<Object[]> findAllIssuedItems(int Farm);
}