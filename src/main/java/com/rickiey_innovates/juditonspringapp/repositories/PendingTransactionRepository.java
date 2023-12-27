package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.PendingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PendingTransactionRepository extends JpaRepository<PendingTransaction, Integer> {
    @Query("select (count(p) > 0) from PendingTransaction p where p.voucher.id = ?1")
    boolean existsByVoucher_Id(Integer id);
    @Transactional
    @Modifying
    @Query("delete from PendingTransaction p where p.voucher = ?1")
    void deleteByVoucher(Integer voucher);
    @Query("select p from PendingTransaction p where p.ref = ?1 and p.farm = ?2")
    PendingTransaction findByRefAndChurch(String ref, Farm farm);
    @Query("select p from PendingTransaction p where p.voucher.id = ?1")
    PendingTransaction findByVoucher_Id(Integer id);

}
