package com.ztai.controller;

import com.opensymphony.xwork2.ActionSupport;

public abstract class EncryptDecryptBase extends ActionSupport {
    protected String encryptedId;
    protected String decryptedId;

    // Getter and setter for encryptedId
    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }

    // Getter for decryptedId
    public String getDecryptedId() {
        return decryptedId;
    }
}
