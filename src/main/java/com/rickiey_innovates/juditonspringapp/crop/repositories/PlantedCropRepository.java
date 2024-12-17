package com.rickiey_innovates.juditonspringapp.crop.repositories;

import com.rickiey_innovates.juditonspringapp.crop.models.PlantedCrop;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlantedCropRepository extends JpaRepository<PlantedCrop, Long> {
    @Query("select p from PlantedCrop p where p.farm = ?1")
    List<PlantedCrop> findByFarm(Farm farm);
}