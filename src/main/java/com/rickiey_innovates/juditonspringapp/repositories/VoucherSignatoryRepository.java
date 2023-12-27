package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.Farm;
import com.rickiey_innovates.juditonspringapp.models.VoucherSignatory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoucherSignatoryRepository extends JpaRepository<VoucherSignatory, Integer> {
    @Query("select v from VoucherSignatory v where v.voucher.farm = ?1")
    List<VoucherSignatory> findByVoucher_Church(Farm farm);
    @Query("select v from VoucherSignatory v where v.voucher.id = ?1")
    VoucherSignatory findByVoucher_Id(Integer id);
}
