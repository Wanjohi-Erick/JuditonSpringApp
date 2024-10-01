package com.rickiey_innovates.juditonspringapp.crop.repositories;

import com.rickiey_innovates.juditonspringapp.crop.models.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {
}