package com.rickiey_innovates.juditonspringapp.crop.repositories;

import com.rickiey_innovates.juditonspringapp.crop.models.Season;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {
  List<Season> findByFarm(Farm farm);
}