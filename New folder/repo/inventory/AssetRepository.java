package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.Asset;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    @Query("select a from Asset a where a.church = ?1")
    List<Asset> findByChurch(Integer church);
}