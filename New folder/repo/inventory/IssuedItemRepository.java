package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.IssuedItem;

import java.util.List;

public interface IssuedItemRepository extends JpaRepository<IssuedItem, Integer> {
    @Query("select i from IssuedItem i where i.itemId = ?1 and i.status = ?2 and i.church = ?3")
    IssuedItem findByItemIdAndStatusAndChurch(Integer itemId, String status, Integer church);
    @Query("SELECT ii, i FROM IssuedItem ii INNER JOIN Item i ON ii.itemId = i.id WHERE ii.church = ?1 AND i.church = ?1 AND ii.status = 'Not Returned'")
    List<Object[]> findAllIssuedItems(int church);
}