package com.auth.auth.service;

import com.auth.auth.bean.LoginRequest;
import com.auth.auth.bean.LoginResponse;
import com.auth.auth.bean.UserBean;
import com.auth.auth.entity.CustomUserDetails;
import com.auth.auth.entity.RoleMaster;
import com.auth.auth.entity.Users;
import com.auth.auth.entity.UsersRole;
import com.auth.auth.repository.RoleMasterRepository;
import com.auth.auth.repository.UsersRepository;
import com.auth.auth.repository.UsersRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final UsersRepository usersRepository;

    private final RoleMasterRepository roleMasterRepository;

    private final UsersRoleRepository usersRoleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public AuthService(UsersRepository usersRepository, RoleMasterRepository roleMasterRepository, UsersRoleRepository usersRoleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.usersRepository = usersRepository;
        this.roleMasterRepository = roleMasterRepository;
        this.usersRoleRepository = usersRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public Users signup(UserBean userBean) {
        Users userInDB = usersRepository.findByEmailId(userBean.getEmailId());
        if (Objects.nonNull(userInDB)) {
            throw new RuntimeException("User already exists with email: " + userBean.getEmailId());
        }
        userInDB = saveUserInDB(new Users(), userBean);
        insertInUserRoleTable(userInDB, userBean.getRole());
        return userInDB;
    }

    private Users saveUserInDB(Users users, UserBean userBean) {
        if (Objects.nonNull(userBean.getUserName())) {
            users.setUserName(userBean.getUserName());
        }
        if (Objects.nonNull(userBean.getMobileNo())) {
            users.setMobileNo(userBean.getMobileNo());
        }
        if (Objects.nonNull(userBean.getEmailId())) {
            users.setEmailId(userBean.getEmailId());
        }
        if (Objects.nonNull(userBean.getPassword())) {
            users.setPassword(passwordEncoder.encode(userBean.getPassword()));
        }
        return usersRepository.save(users);
    }

    private void insertInUserRoleTable(Users users, String role) {
        RoleMaster roleMaster = roleMasterRepository.findByName(role);
        if (roleMaster == null) {
            throw new RuntimeException("Role not found: " + role);
        }
        UsersRole usersRole = new UsersRole();
        usersRole.setUserId(users.getId());
        usersRole.setRoleId(roleMaster.getId());
        usersRoleRepository.save(usersRole);
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmailId(),
                        loginRequest.getPassword()
                )
        );
        Users users = usersRepository.findByEmailId(loginRequest.getEmailId());
        if (Objects.isNull(users)) {
            throw new UsernameNotFoundException("User not found with email: " + loginRequest.getEmailId());
        }

        CustomUserDetails userDetails = new CustomUserDetails(users);
        String jwtToken = jwtService.generateToken(userDetails);
        loginResponse.setAccessToken(jwtToken);
        loginResponse.setTokenExpiryTime(jwtService.getExpirationTime());
        return loginResponse;
    }
}
