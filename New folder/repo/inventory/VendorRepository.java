package com.walgotech.juditonspringapp.repositories.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.walgotech.juditonspringapp.models.inventory.Vendor;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    @Query("select v from Vendor v where v.church = ?1 order by v.company")
    List<Vendor> findByChurchOrderByCompanyAsc(Integer church);
}