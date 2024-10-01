package com.rickiey_innovates.juditonspringapp.crop.repositories;

import com.rickiey_innovates.juditonspringapp.crop.models.CropVariety;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CropVarietyRepository extends JpaRepository<CropVariety, Long> {
    @Query("select c from CropVariety c where c.farm = ?1")
    List<CropVariety> findByFarm(Farm farm);
}