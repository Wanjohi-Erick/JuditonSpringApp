package com.rickiey_innovates.juditonspringapp.crop.repositories;

import com.rickiey_innovates.juditonspringapp.crop.models.CropType;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CropTypeRepository extends JpaRepository<CropType, Long> {
    @Query("select c from CropType c where c.farm = ?1")
    List<CropType> findByFarm(Farm farm);
}