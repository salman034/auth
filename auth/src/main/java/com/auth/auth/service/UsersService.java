package com.auth.auth.service;

import com.auth.auth.bean.UserBean;
import com.auth.auth.entity.RoleMaster;
import com.auth.auth.entity.Users;
import com.auth.auth.entity.UsersRole;
import com.auth.auth.exception.ResourceNotFoundException;
import com.auth.auth.repository.RoleMasterRepository;
import com.auth.auth.repository.UsersRepository;
import com.auth.auth.repository.UsersRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    private final RoleMasterRepository roleMasterRepository;

    private final UsersRoleRepository usersRoleRepository;

    public UsersService(UsersRepository usersRepository, RoleMasterRepository roleMasterRepository,
                        UsersRoleRepository usersRoleRepository) {
        this.usersRepository = usersRepository;
        this.roleMasterRepository = roleMasterRepository;
        this.usersRoleRepository = usersRoleRepository;
    }

    public UserBean getUserByEmail(String emailId) {
        Users users = usersRepository.findByEmailId(emailId);
        if (Objects.isNull(users)) {
            throw new ResourceNotFoundException("User not found");
        }
        return mapUserDetail(users);
    }

    public UserBean getUserByMobileNo(String mobileNo) {
        Users users = usersRepository.findByMobileNo(mobileNo);
        if (Objects.isNull(users)) {
            throw new ResourceNotFoundException("User not found");
        }
        return mapUserDetail(users);
    }

    private UserBean mapUserDetail(Users users) {
        UserBean userBean = new UserBean();
        UsersRole usersRole = usersRoleRepository.findByUserId(users.getId());
        userBean.setUserName(users.getUserName());
        userBean.setEmailId(users.getEmailId());
        userBean.setMobileNo(users.getMobileNo());
        if (Objects.nonNull(usersRole)) {
            RoleMaster master = roleMasterRepository.findById(usersRole.getRoleId());
            if (Objects.nonNull(master)) {
                userBean.setRole(master.getCode());
            }
        }
        return userBean;
    }
}
