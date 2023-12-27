package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Allowances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface AllowancesRepository extends JpaRepository<Allowances, Integer> {
    
	@Transactional
    @Modifying
    @Query("update Allowances s set s.Earning = ?1, s.type = ?2,s.costperunit = ?3,s.visible = ?4 where s.id = ?5")
    int updateEarningById(@NonNull String earning,@NonNull Integer type,@NonNull Double costperunit, @NonNull String visible,@NonNull Integer id);

   
}