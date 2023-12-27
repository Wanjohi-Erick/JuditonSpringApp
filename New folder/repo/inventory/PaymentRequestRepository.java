package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.PaymentRequest;

import java.util.List;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Integer> {
    @Query("select p from PaymentRequest p where p.church = ?1")
    List<PaymentRequest> findByChurch(Integer church);
}