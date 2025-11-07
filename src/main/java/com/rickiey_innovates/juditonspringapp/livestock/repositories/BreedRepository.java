package com.rickiey_innovates.juditonspringapp.livestock.repositories;

import com.rickiey_innovates.juditonspringapp.livestock.models.Breed;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BreedRepository extends JpaRepository<Breed, Long> {
  @Query("select b from Breed b where b.farm = ?1")
  List<Breed> findByFarm(Farm farm);
}