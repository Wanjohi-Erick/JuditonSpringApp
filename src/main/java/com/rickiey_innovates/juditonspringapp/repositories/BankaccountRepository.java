package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Bankaccount;
import com.rickiey_innovates.juditonspringapp.models.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankaccountRepository extends JpaRepository<Bankaccount, Integer> {
    @Query("select b from Bankaccount b where b.farm = ?1")
    List<Bankaccount> findByChurch(Farm farm);
}