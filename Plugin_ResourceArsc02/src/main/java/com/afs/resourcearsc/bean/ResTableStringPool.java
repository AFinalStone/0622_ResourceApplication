package com.afs.resourcearsc.bean;


import com.afs.resourcearsc.utils.IObjToBytes;

import java.util.ArrayList;

/**
 * 资源字符串常量池
 *
 * @author syl
 * @time 2022/4/26 11:55
 */
public class ResTableStringPool implements IObjToBytes {

    public ResTableStringPoolHeader stringPoolHeader;
    //    public ArrayList<String> stringList;
    //    public ArrayList<String> styleList;
    public byte[] strList;
    public byte[] styleList;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
//        if (stringList != null) {
//            for (String item : stringList) {
//                if (!item.isEmpty())
//                    stringBuilder.append(item).append(" ");
//            }
//        }
        StringBuilder styleBuilder = new StringBuilder();
//        if (styleList != null) {
//            for (String item : styleList) {
//                if (!item.isEmpty())
//                    styleBuilder.append(item).append(" ");
//            }
//        }
        return "string:" + "\n" + stringBuilder.toString()
                + "\n" + "style:" + styleBuilder.toString();
    }

    @Override
    public byte[] toBytes() {
        if (styleList == null || styleList.length == 0) {
            byte[] byteStringPoolHeader = stringPoolHeader.toBytes();
            byte[] byteStringPool = new byte[byteStringPoolHeader.length + strList.length];
            System.arraycopy(byteStringPoolHeader, 0, byteStringPool, 0, byteStringPoolHeader.length);
            System.arraycopy(strList, 0, byteStringPool, byteStringPoolHeader.length, strList.length);
            return byteStringPool;
        }
        byte[] byteStringPoolHeader = stringPoolHeader.toBytes();
        byte[] byteStringPool = new byte[byteStringPoolHeader.length + strList.length + styleList.length];
        System.arraycopy(byteStringPoolHeader, 0, byteStringPool, 0, byteStringPoolHeader.length);
        System.arraycopy(strList, 0, byteStringPool, byteStringPoolHeader.length, strList.length);
        System.arraycopy(styleList, 0, byteStringPool, byteStringPoolHeader.length + strList.length, styleList.length);
        return byteStringPool;
    }


}