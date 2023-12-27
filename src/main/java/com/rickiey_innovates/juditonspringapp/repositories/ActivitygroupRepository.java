package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Activitygroup;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivitygroupRepository extends JpaRepository<Activitygroup, Integer> {
    @Query("select a from Activitygroup a where a.farm = ?1 order by a.groupName")
    List<Activitygroup> findByChurch(Farm farm);
}