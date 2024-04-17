package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Integer> {
    @Query("select p from PaymentRequest p where p.farm = ?1")
    List<PaymentRequest> findByFarm(Integer Farm);
}