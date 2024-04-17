package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.SmsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SmsHistoryRepository extends JpaRepository<SmsHistory, Integer> {
    @Query("select s from SmsHistory s where s.farm = ?1")
    SmsHistory findByFarm(Farm farm);
}