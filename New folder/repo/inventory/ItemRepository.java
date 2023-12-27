package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("select i from Item i where i.itemName = ?1 and i.church = ?2")
    Item findByItemNameAndChurch(String itemName, Integer church);
    @Query("select i from Item i where i.id = ?1 and i.church = ?2")
    Optional<Item> findByIdAndChurch(Integer id, Integer church);
    @Query("select i from Item i where i.church = ?1")
    List<Item> findByChurch(Integer church);

    /*@Query("SELECT b FROM Item b " +
            "LEFT JOIN ItemStock ib ON b.id = ib.itemId " +
            "WHERE (LOWER(b.itemName) LIKE LOWER(concat('%', ?1, '%')) " +
            "OR LOWER(b.id) LIKE LOWER(concat('%', ?1, '%')) " +
            ") " +
            "AND b.church = ?2 " +
            "AND (ib.amount != 0)")

    List<Item> findByTitleContainingIgnoreCase(String query, Integer church);*/
}