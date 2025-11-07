package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.FarmActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FarmActivityRepository extends JpaRepository<FarmActivity, Long> {
    @Query("select f from FarmActivity f where f.farm = ?1")
    List<FarmActivity> findByFarm(Farm farm);

    @Query("select f from FarmActivity f where f.plantedCrop.id = ?1")
    List<FarmActivity> findByPlantedCrop_Id(Long id);

    @Query("select f from FarmActivity f where f.farmedLivestock.id = ?1")
    List<FarmActivity> findByFarmedLivestock_Id(Long id);
}