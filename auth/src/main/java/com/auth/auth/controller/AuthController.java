package com.auth.auth.controller;

import com.auth.auth.bean.LoginRequest;
import com.auth.auth.bean.LoginResponse;
import com.auth.auth.bean.ResponseBean;
import com.auth.auth.bean.UserBean;
import com.auth.auth.constants.CommonConstants;
import com.auth.auth.entity.Users;
import com.auth.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseBean> signup(@RequestBody UserBean userBean) {
        Users users = authService.signup(userBean);
        ResponseBean response = new ResponseBean(CommonConstants.OK, CommonConstants.ENTRY_POST_SUCCESS, users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<ResponseBean> authenticate(@RequestBody LoginRequest loginRequest) {
        LoginResponse user = authService.authenticate(loginRequest);
        ResponseBean response = new ResponseBean(CommonConstants.OK, CommonConstants.DATA_GET_SUCCESS, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
