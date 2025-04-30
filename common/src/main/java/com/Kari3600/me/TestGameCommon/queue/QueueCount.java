package com.Kari3600.me.TestGameCommon.queue;




public class QueueCount extends QueuePacket {
    private final byte count;

    public byte getCount() {
        return count;
    }

    public QueueCount(byte count) {
        this.count = count;
    }
}
