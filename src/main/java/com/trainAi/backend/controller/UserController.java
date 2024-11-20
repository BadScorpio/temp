package com.trainAi.backend.controller;

import com.trainAi.backend.common.CommonResultCode;
import com.trainAi.backend.common.Result;
import com.trainAi.backend.entity.request.LoginRequest;
import com.trainAi.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getSignMessage")
    public Result<String> getSignMessage(String walletAddress) throws Exception {
        try{
            String signMessage = this.userService.getSignMessage(walletAddress);
            return Result.ok(signMessage);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CommonResultCode.INTERNAL_ERROR);
        }
    }

    @RequestMapping("/login")
    public Result<String> Login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }


}
