package com.afs.resourcearsc.bean;


import com.afs.resourcearsc.utils.IObjToBytes;

import java.util.ArrayList;


public class Res02StringPool implements IObjToBytes {

    public ArrayList<String> string;
    public ArrayList<String> style;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (string != null) {
            for (String item : string) {
                stringBuilder.append(item).append("\n");
            }
        }
        StringBuilder styleBuilder = new StringBuilder();
        if (style != null) {
            for (String item : style) {
                styleBuilder.append(item).append("\n");
            }
        }
        return "string:" + stringBuilder.toString() + "\n" + "style:" + styleBuilder.toString();
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}