package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import com.walgotech.juditonspringapp.models.inventory.AssetDepreciation;

public interface AssetDepreciationRepository extends JpaRepository<AssetDepreciation, Integer> {
}