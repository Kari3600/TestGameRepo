package com.Kari3600.me.TestGameCommon.account;

public class RegisterResult extends LoginPacket {
    private final byte status;

    public byte getStatus() {
        return status;
    }

    public RegisterResult(byte status) {
        this.status = status;
    }
}
