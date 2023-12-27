package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.Paymentvoucher;
import com.rickiey_innovates.juditonspringapp.models.Paymentvoucherdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentvoucherdetailRepository extends JpaRepository<Paymentvoucherdetail, Integer> {
    @Query("select p from Paymentvoucherdetail p where p.pv.id = ?1 and p.farm = ?2")
    Paymentvoucherdetail findByPvAndChurch(int pv, Farm farm);
    @Transactional
    @Modifying
    @Query("delete from Paymentvoucherdetail p where p.pv = ?1")
    int deleteByPv(Paymentvoucher pv);
}