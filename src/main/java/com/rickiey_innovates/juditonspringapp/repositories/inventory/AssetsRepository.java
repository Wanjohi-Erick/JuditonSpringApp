package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.inventory.Assets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetsRepository extends JpaRepository<Assets, Integer> {
    @Query("select a from Assets a where a.farm = ?1")
    List<Assets> findByFarm(Farm church);
}