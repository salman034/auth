package com.auth.auth.repository;

import com.auth.auth.entity.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRoleRepository extends JpaRepository<UsersRole, Long> {

    UsersRole findByUserId(Long userId);

}
