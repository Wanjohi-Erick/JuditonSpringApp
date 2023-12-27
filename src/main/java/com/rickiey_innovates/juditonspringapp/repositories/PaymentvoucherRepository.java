package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.Paymentvoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentvoucherRepository extends JpaRepository<Paymentvoucher, Integer> {
    @Query("select p from Paymentvoucher p where p.voucher = ?1 and p.farm = ?2")
    Paymentvoucher findByVoucherAndChurch(String voucher, Farm farm);
    @Query("select p from Paymentvoucher p where p.payeeName not like ?1 order by p.id DESC LIMIT 7")
    List<Paymentvoucher> findByPayeeNameNotLikeOrderByIdDesc(String payeeName);
}