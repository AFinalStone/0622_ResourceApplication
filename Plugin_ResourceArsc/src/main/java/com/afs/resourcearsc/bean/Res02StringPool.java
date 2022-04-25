package com.afs.resourcearsc.bean;


import com.afs.resourcearsc.utils.IObjToBytes;


public class Res02StringPool implements IObjToBytes {

    public String string;
    public String style;


    @Override
    public String toString() {
        return "string:" + string + "\n" + "style:" + style;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}