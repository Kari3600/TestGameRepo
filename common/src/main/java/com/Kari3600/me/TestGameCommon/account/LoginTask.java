package com.Kari3600.me.TestGameCommon.account;




public class LoginTask extends LoginPacket {
    private final String salt;

    public String getSalt() {
        return salt;
    }

    public LoginTask(String salt) {
        this.salt = salt;
    }
}
