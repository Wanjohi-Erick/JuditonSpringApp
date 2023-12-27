package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.Companyofficial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyofficialRepository extends JpaRepository<Companyofficial, Integer> {
    @Query("select c from Companyofficial c where c.farm = ?1")
    Companyofficial findByChurch(Farm farm);
}