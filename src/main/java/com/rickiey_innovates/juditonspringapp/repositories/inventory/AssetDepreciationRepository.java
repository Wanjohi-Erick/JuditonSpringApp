package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.AssetDepreciation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetDepreciationRepository extends JpaRepository<AssetDepreciation, Integer> {
}