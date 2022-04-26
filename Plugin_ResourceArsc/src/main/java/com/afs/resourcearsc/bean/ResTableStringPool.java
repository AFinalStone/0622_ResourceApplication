package com.afs.resourcearsc.bean;


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

    public ArrayList<String> stringList;
    public ArrayList<String> styleList;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (stringList != null) {
            for (String item : stringList) {
                if (!item.isEmpty())
                    stringBuilder.append(item).append("\n");
            }
        }
        StringBuilder styleBuilder = new StringBuilder();
        if (styleList != null) {
            for (String item : styleList) {
                if (!item.isEmpty())
                    styleBuilder.append(item).append("\n");
            }
        }
        return "string:" + "\n" + stringBuilder.toString()
                + "\n" + "style:" + styleBuilder.toString();
    }

    @Override
    public byte[] toBytes() {
        int length = 0;
        for (int i = 0; i < stringList.size(); i++) {
            int itemLen = stringList.get(i).getBytes(StandardCharsets.UTF_8).length;
            length = length + itemLen + 3;
        }
        for (int i = 0; i < styleList.size(); i++) {
            int itemLen = styleList.get(i).getBytes(StandardCharsets.UTF_8).length;
            length = length + itemLen + 3;
        }
        byte[] resultBytes = new byte[length];
        int index = 0;
        for (int i = 0; i < stringList.size(); i++) {
            String item = stringList.get(i);
            int len = item.length();
            resultBytes[index++] = (byte) len;
            resultBytes[index++] = (byte) len;
            if (item.length() > 0) {
                byte[] byteStr = item.getBytes(StandardCharsets.UTF_8);
                System.arraycopy(byteStr, 0, resultBytes, index, byteStr.length);
                index = index + byteStr.length;
            }
            resultBytes[index++] = 0;
        }
        for (int i = 0; i < styleList.size(); i++) {
            String item = stringList.get(i);
            int len = item.length();
            resultBytes[index++] = (byte) len;
            resultBytes[index++] = (byte) len;
            if (item.length() > 0) {
                byte[] byteStr = item.getBytes(StandardCharsets.UTF_8);
                System.arraycopy(byteStr, 0, resultBytes, index, byteStr.length);
                index = index + byteStr.length;
            }
            resultBytes[index++] = 0;
        }
        return resultBytes;
    }


}