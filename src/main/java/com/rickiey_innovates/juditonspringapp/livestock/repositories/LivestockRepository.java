package com.rickiey_innovates.juditonspringapp.livestock.repositories;

import com.rickiey_innovates.juditonspringapp.livestock.models.Livestock;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivestockRepository extends JpaRepository<Livestock, Long> {
  @Query("select l from Livestock l where l.farm = ?1")
  List<Livestock> findByFarm(Farm farm);
}