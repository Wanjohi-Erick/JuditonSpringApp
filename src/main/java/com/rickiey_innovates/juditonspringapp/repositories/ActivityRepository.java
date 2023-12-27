package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Activity;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    @Query("select a from Activity a where a.farm = ?1 order by a.account")
    List<Activity> findByChurch(Farm farm);
    @Query("select a from Activity a where a.account = ?1")
    Activity findByAccount(String account);
}