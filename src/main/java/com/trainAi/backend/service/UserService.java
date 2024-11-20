package com.trainAi.backend.service;


import com.trainAi.backend.common.Result;
import com.trainAi.backend.entity.User;
import com.trainAi.backend.entity.request.LoginRequest;

import java.security.SignatureException;


public interface UserService {

    String getSignMessage(String walletAddress) throws Exception;

    Result<String> login(LoginRequest loginRequest);
}
