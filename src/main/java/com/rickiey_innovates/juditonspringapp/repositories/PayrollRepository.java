package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.payrolls.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface PayrollRepository extends JpaRepository<Payroll, Integer> {
    
	@Transactional
    @Modifying
    @Query("update Payroll s set s.date = ?1 where s.id = ?2")
    int updateEarningById(@NonNull String date,@NonNull Integer id);

   
}