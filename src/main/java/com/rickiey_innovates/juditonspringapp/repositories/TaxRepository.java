package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Paymentvoucher;
import com.rickiey_innovates.juditonspringapp.models.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaxRepository extends JpaRepository<Tax, Integer> {
    @Query("select t from Tax t where t.pv = ?1 and t.type = ?2")
    Tax findByPvAndType(Paymentvoucher pv, String type);
}