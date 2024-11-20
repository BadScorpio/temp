package com.trainAi.backend.entity.request;


public class LoginRequest {
    private String address;
    private String signature;
    private String publicJWK;

    public LoginRequest(String address, String signature, String publicJWK) {
        this.address = address;
        this.signature = signature;
        this.publicJWK = publicJWK;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPublicJWK() {
        return publicJWK;
    }

    public void setPublicJWK(String publicJWK) {
        this.publicJWK = publicJWK;
    }
}
