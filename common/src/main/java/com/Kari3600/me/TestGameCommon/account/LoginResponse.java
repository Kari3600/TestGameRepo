package com.Kari3600.me.TestGameCommon.account;




public class LoginResponse extends LoginPacket {
    private final String password;

    public String getPassword() {
        return password;
    }

    public LoginResponse(String password) {
        this.password = password;
    }
}
