package com.auth.auth.repository;

import com.auth.auth.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster, Integer> {

    RoleMaster findByName(String name);

    RoleMaster findById(Long id);

}
