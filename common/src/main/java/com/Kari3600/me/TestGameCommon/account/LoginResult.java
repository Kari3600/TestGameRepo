package com.Kari3600.me.TestGameCommon.account;




public class LoginResult extends LoginPacket {
    private final byte status;

    public byte getStatus() {
        return status;
    }

    public LoginResult(byte status) {
        this.status = status;
    }
}
