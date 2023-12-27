package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.payrolls.Deductions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface DeductionsRepository extends JpaRepository<Deductions, Integer> {
    
	@Transactional
    @Modifying
    @Query("update Deductions s set s.Deduction = ?1,s.type = ?2,s.calculationtype=?3,s.costperunit = ?4, s.visible = ?5 where s.id = ?6")
    int updateDeductionById(@NonNull String deduction,@NonNull Integer type,@NonNull Integer calculationtype,@NonNull Double costperunit, @NonNull String visible,@NonNull Integer id);

   
}