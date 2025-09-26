package com.auth.auth.controller;

import com.auth.auth.bean.ResponseBean;
import com.auth.auth.bean.UserBean;
import com.auth.auth.constants.CommonConstants;
import com.auth.auth.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<ResponseBean> findUserByEmailId(@RequestParam("emailId") String emailId) {
        UserBean userInfo = usersService.getUserByEmail(emailId);
        ResponseBean response = new ResponseBean(CommonConstants.OK, CommonConstants.DATA_GET_SUCCESS, userInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/findByMobileNo")
    public ResponseEntity<ResponseBean> findUserByMobileNo(@RequestParam("mobileNo") String mobileNo) {
        UserBean userInfo = usersService.getUserByMobileNo(mobileNo);
        ResponseBean response = new ResponseBean(CommonConstants.OK, CommonConstants.DATA_GET_SUCCESS, userInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
