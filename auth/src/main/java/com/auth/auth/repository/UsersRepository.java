package com.auth.auth.repository;

import com.auth.auth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUserName(String userName);

    Users findByEmailId(String emailId);

    Users findByUserNameOrMobileNo(String userName, String mobileNo);

    Users findByMobileNo(String mobileNo);

}
