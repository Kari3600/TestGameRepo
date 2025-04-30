package com.Kari3600.me.TestGameCommon.account;




public class RegisterRequest extends LoginPacket {
    private final String username;
    private final String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
