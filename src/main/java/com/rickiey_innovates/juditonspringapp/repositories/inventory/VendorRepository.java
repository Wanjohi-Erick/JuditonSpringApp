package com.rickiey_innovates.juditonspringapp.repositories.inventory;

import com.rickiey_innovates.juditonspringapp.models.inventory.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    @Query("select v from Vendor v where v.farm = ?1 order by v.company")
    List<Vendor> findByFarmOrderByCompanyAsc(Integer Farm);
}