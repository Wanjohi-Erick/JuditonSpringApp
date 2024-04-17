package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("select i from Item i where i.itemName = ?1 and i.farm = ?2")
    Item findByItemNameAndFarm(String itemName, Integer Farm);
    @Query("select i from Item i where i.id = ?1 and i.farm = ?2")
    Optional<Item> findByIdAndFarm(Integer id, Integer Farm);
    @Query("select i from Item i where i.farm = ?1")
    List<Item> findByFarm(Integer Farm);

    @Query("SELECT b FROM Item b " +
            "LEFT JOIN ItemStock ib ON b.id = ib.itemId " +
            "WHERE (LOWER(b.itemName) LIKE LOWER(concat('%', ?1, '%')) " +
            ") " +
            "AND b.farm = ?2 " +
            "AND (ib.amount != 0)")

    List<Item> findByTitleContainingIgnoreCase(String query, Integer Farm);
}