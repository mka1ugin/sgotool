package com.mkalugin;

public class Datablock {
    int offset;
    int number;
    int startAddress;
    int endAddress;
    int format;
    int size;
    int checksum;
    byte[] payload;

    public Datablock(int number)
    {
        this.number = number;
    }
}
