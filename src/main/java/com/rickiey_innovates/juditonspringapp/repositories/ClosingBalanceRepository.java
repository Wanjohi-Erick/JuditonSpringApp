package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.ClosingBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface ClosingBalanceRepository extends JpaRepository<ClosingBalance, Integer> {
    @Query("select c from ClosingBalance c where c.date = ?1 and c.farm = ?2")
    ClosingBalance findByDateAndChurch(LocalDate date, Farm farm);
    @Query("select (count(c) > 0) from ClosingBalance c where c.date = ?1 and c.farm = ?2")
    boolean existsByDateAndChurch(LocalDate date, Farm farm);
}