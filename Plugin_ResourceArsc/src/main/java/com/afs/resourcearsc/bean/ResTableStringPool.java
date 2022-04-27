package com.afs.resourcearsc.bean;


import com.afs.resourcearsc.utils.Byte2ObjectUtil;
import com.afs.resourcearsc.utils.IObjToBytes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * 资源字符串常量池
 *
 * @author syl
 * @time 2022/4/26 11:55
 */
public class ResTableStringPool implements IObjToBytes {

    public ResTableStringPoolHeader stringPoolHeader;
    public ArrayList<String> stringList;
    public ArrayList<String> styleList;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (stringList != null) {
            for (String item : stringList) {
                if (!item.isEmpty())
                    stringBuilder.append(item).append(" ");
            }
        }
        StringBuilder styleBuilder = new StringBuilder();
        if (styleList != null) {
            for (String item : styleList) {
                if (!item.isEmpty())
                    styleBuilder.append(item).append(" ");
            }
        }
        return "string:" + "\n" + stringBuilder.toString()
                + "\n" + "style:" + styleBuilder.toString();
    }

    @Override
    public byte[] toBytes() {
        byte[] byteStrList = null;
        int length = 0;
        if (ResTableStringPoolHeader.UTF8_FLAG == stringPoolHeader.flags) {
            for (int i = 0; i < stringList.size(); i++) {
                int itemLen = stringList.get(i).getBytes(StandardCharsets.UTF_8).length;
                length = length + itemLen + 3;
            }
            for (int i = 0; i < styleList.size(); i++) {
                int itemLen = styleList.get(i).getBytes(StandardCharsets.UTF_8).length;
                length = length + itemLen + 3;
            }
            byteStrList = new byte[length];
            int index = 0;
            for (int i = 0; i < stringList.size(); i++) {
                String item = stringList.get(i);
                int len = item.length();
                int lenByte = item.getBytes(StandardCharsets.UTF_8).length;
                byteStrList[index++] = (byte) len;
                byteStrList[index++] = (byte) lenByte;
                if (item.length() > 0) {
                    byte[] byteStr = item.getBytes(StandardCharsets.UTF_8);
                    System.arraycopy(byteStr, 0, byteStrList, index, byteStr.length);
                    index = index + byteStr.length;
                }
                byteStrList[index++] = 0;
            }
            for (int i = 0; i < styleList.size(); i++) {
                String item = styleList.get(i);
                int len = item.length();
                int lenByte = item.getBytes(StandardCharsets.UTF_8).length;
                byteStrList[index++] = (byte) len;
                byteStrList[index++] = (byte) lenByte;
                if (item.length() > 0) {
                    byte[] byteStr = item.getBytes(StandardCharsets.UTF_8);
                    System.arraycopy(byteStr, 0, byteStrList, index, byteStr.length);
                    index = index + byteStr.length;
                }
                byteStrList[index++] = 0;
            }
        } else {
            for (int i = 0; i < stringList.size(); i++) {
                int itemLen = stringList.get(i).getBytes(StandardCharsets.UTF_16).length;
                length = length + itemLen + 4;
            }
            for (int i = 0; i < styleList.size(); i++) {
                int itemLen = styleList.get(i).getBytes(StandardCharsets.UTF_16).length;
                length = length + itemLen + 4;
            }
            byteStrList = new byte[length];
            int index = 0;
            for (int i = 0; i < stringList.size(); i++) {
                String item = stringList.get(i);
                short lenByte = (short) item.getBytes(StandardCharsets.UTF_16).length;
                byte[] byteLen = Byte2ObjectUtil.short2ByteArray_Little_Endian(lenByte);
                System.arraycopy(byteLen, 0, byteStrList, index, byteLen.length);
                index = index + 2;
                if (item.length() > 0) {
                    byte[] byteStr = item.getBytes(StandardCharsets.UTF_16);
                    System.arraycopy(byteStr, 0, byteStrList, index, byteStr.length);
                    index = index + byteStr.length;
                }
                byteStrList[index++] = 0;
                byteStrList[index++] = 0;
            }
            for (int i = 0; i < styleList.size(); i++) {
                String item = styleList.get(i);
                short lenByte = (short) item.getBytes(StandardCharsets.UTF_16).length;
                byte[] byteLen = Byte2ObjectUtil.short2ByteArray_Little_Endian(lenByte);
                System.arraycopy(byteLen, 0, byteStrList, index, byteLen.length);
                index = index + 2;
                if (item.length() > 0) {
                    byte[] byteStyle = item.getBytes(StandardCharsets.UTF_16);
                    System.arraycopy(byteStyle, 0, byteStrList, index, byteStyle.length);
                    index = index + byteStyle.length;
                }
                byteStrList[index++] = 0;
                byteStrList[index++] = 0;
            }
        }
        byte[] byteStringPoolHeader = stringPoolHeader.toBytes();
        byte[] byteStringPool = new byte[byteStringPoolHeader.length + length];
        System.arraycopy(byteStringPoolHeader, 0, byteStringPool, 0, byteStringPoolHeader.length);
        System.arraycopy(byteStrList, 0, byteStringPool, byteStringPoolHeader.length, byteStrList.length);
        return byteStringPool;
    }


}