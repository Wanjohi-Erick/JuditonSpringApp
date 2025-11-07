package com.rickiey_innovates.juditonspringapp.livestock.repositories;

import com.rickiey_innovates.juditonspringapp.livestock.models.FarmedLivestock;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FarmedLivestockRepository extends JpaRepository<FarmedLivestock, Long> {
  @Query("select f from FarmedLivestock f where f.farm = ?1")
  List<FarmedLivestock> findByFarm(Farm farm);
}