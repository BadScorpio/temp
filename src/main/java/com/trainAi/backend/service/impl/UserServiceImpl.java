package com.trainAi.backend.service.impl;

import com.trainAi.backend.common.ErrorResultCode;
import com.trainAi.backend.common.Result;
import com.trainAi.backend.entity.User;
import com.trainAi.backend.entity.request.LoginRequest;
import com.trainAi.backend.repository.UserRepository;
import com.trainAi.backend.service.UserService;
import com.trainAi.backend.utils.CustomCache;
import com.trainAi.backend.utils.JWTTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomCache<String, String> customCache;
    @Autowired
    private UserRepository userRepository;
    @Value("${wallet.sign.secretKey}")
    private String secretKey;

    @Override
    public String getSignMessage(String walletAddress) throws Exception{
        // Create an HMAC-SHA256 Mac instance with the secret key
        Key key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);

        // Generate the HMAC signature from the data
        byte[] hmac = mac.doFinal(walletAddress.getBytes());

        // Return the Base64 encoded signature
        String signMessage = Base64.getEncoder().encodeToString(hmac);
        customCache.put(walletAddress, signMessage,5, TimeUnit.MINUTES );
        return signMessage;
    }

    @Override
    public Result<String> login(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getAddress() == null || loginRequest.getSignature() == null) {
            return Result.error(ErrorResultCode.E000002);
        }
        String s = customCache.get(loginRequest.getAddress());
        if(s == null){
            return Result.error(ErrorResultCode.E000001);
        }
        if(verifySignature(s, loginRequest.getSignature(),loginRequest.getPublicJWK())){

            User user = userRepository.findByAddress(loginRequest.getAddress());
            if (user == null) {
                User newUser = new User();
                newUser.setPassword("");
                newUser.setUsername("");
                newUser.setAddress(loginRequest.getAddress());
                this.userRepository.insert(newUser);
            }
            return Result.ok(JWTTokenUtil.generateToken(loginRequest.getAddress()));
        }
        return Result.error(ErrorResultCode.E000001);
    }

    public static boolean verifySignature(String message, String signedMessage, String publicKeyStr) {
        try {

            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);


            Signature signature = Signature.getInstance("SHA256withRSA/PSS");
            signature.initVerify(publicKey);


            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            signature.update(messageBytes);


            byte[] signatureBytes = Base64.getDecoder().decode(signedMessage);

            boolean verify = signature.verify(signatureBytes);
            return verify;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
