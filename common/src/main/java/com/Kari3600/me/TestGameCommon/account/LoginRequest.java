package com.Kari3600.me.TestGameCommon.account;




public class LoginRequest extends LoginPacket {
    private final String username;

    public String getUsername() {
        return username;
    }

    public LoginRequest(String username) {
        this.username = username;
    }
}
