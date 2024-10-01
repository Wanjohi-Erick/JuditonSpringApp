package com.rickiey_innovates.juditonspringapp.crop.repositories;

import com.rickiey_innovates.juditonspringapp.crop.models.CropName;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropNameRepository extends JpaRepository<CropName, Long> {
    List<CropName> findByFarm(Farm farm);
}