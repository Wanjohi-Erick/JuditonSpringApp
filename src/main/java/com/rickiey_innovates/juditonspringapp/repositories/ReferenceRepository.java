package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReferenceRepository extends JpaRepository<Reference, Integer> {
    @Query("select r from Reference r where r.farm = ?1")
    Reference findByChurch(Farm farm);
    @Transactional
    @Modifying
    @Query("update Reference r set r.rct = ?1 where r.farm = ?2")
    int updateRctByChurch(Integer rct, Farm farm);
    @Transactional
    @Modifying
    @Query("update Reference r set r.pv = ?1 where r.farm = ?2")
    void updatePvByChurch(Integer pv, Farm farm);
}