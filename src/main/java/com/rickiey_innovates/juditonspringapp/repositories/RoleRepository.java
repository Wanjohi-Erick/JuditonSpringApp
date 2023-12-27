package com.rickiey_innovates.juditonspringapp.repositories;

import com.rickiey_innovates.juditonspringapp.models.ERole;
import com.rickiey_innovates.juditonspringapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}