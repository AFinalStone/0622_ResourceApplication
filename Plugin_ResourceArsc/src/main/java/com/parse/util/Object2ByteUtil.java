package com.parse.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author syl
 * @time 2022/4/25 13:41
 */
public class Object2ByteUtil {

    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

//    //byte 数组与 int 的相互转换
//    public static int byteArrayToInt(byte[] b) {
//        return b[3] & 0xFF |
//                (b[2] & 0xFF) << 8 |
//                (b[1] & 0xFF) << 16 |
//                (b[0] & 0xFF) << 24;
//    }
//
//    public static byte[] intToByteArray(int a) {
//        return new byte[]{
//                (byte) ((a >> 24) & 0xFF),
//                (byte) ((a >> 16) & 0xFF),
//                (byte) ((a >> 8) & 0xFF),
//                (byte) (a & 0xFF)
//        };
//    }
//
//    //byte 数组与 long 的相互转换
//    public static byte[] longToBytes(long x) {
//        ByteBuffer buffer = ByteBuffer.allocate(8);
//        buffer.putLong(0, x);
//        return buffer.array();
//    }
//
//    public static long bytesToLong(byte[] bytes) {
//        ByteBuffer buffer = ByteBuffer.allocate(8);
//        buffer.put(bytes, 0, bytes.length);
//        buffer.flip();//need flip
//        return buffer.getLong();
//    }

    /**
     * 字节数组转 short，小端
     */
    public static short byteArray2Short_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 2) {
            return 0;
        }

        short value = 0;
        for (int i = 0; i < array.length; i++) {
            // & 0xff，除去符号位干扰
            value |= ((array[i] & 0xff) << (i * 8));
        }
        return value;
    }

    /**
     * 字节数组转 short，大端
     */
    public static short byteArray2Short_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 2) {
            return 0;
        }

        short value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((array[i] & 0xff) << ((array.length - i - 1) * 8));
        }
        return value;
    }

    /**
     * 字节数组转 int，小端
     */
    public static int byteArray2Int_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 4) {
            return 0;
        }

        int value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((array[i] & 0xff) << (i * 8));

        }
        return value;
    }

    /**
     * 字节数组转 int，大端
     */
    public static int byteArray2Int_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 4) {
            return 0;
        }

        int value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((array[i] & 0xff) << ((array.length - i - 1) * 8));
        }
        return value;
    }

    /**
     * 字节数组转 float，小端
     */
    public static float byteArray2Float_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 4) {
            return 0;
        }

        return Float.intBitsToFloat(byteArray2Int_Little_Endian(array));
    }

    /**
     * 字节数组转 float，大端
     */
    public static float byteArray2Float_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length > 4) {
            return 0;
        }

        return Float.intBitsToFloat(byteArray2Int_Big_Endian(array));
    }

    /**
     * 字节数组转 long，小端
     */
    public static long byteArray2Long_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        long value = 0;
        for (int i = 0; i < array.length; i++) {
            // 需要转long再位移，否则int丢失精度
            value |= ((long) (array[i] & 0xff) << (i * 8));
        }
        return value;
    }

    /**
     * 字节数组转 long，大端
     */
    public static long byteArray2Long_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        long value = 0;
        for (int i = 0; i < array.length; i++) {
            value |= ((long) (array[i] & 0xff) << ((array.length - i - 1) * 8));
        }
        return value;
    }

    /**
     * 字节数组转 double，小端
     */
    public static double byteArray2Double_Little_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        return Double.longBitsToDouble(byteArray2Long_Little_Endian(array));
    }

    /**
     * 字节数组转 double，大端
     */
    public static double byteArray2Double_Big_Endian(byte[] array) {

        // 数组长度有误
        if (array.length != 8) {
            return 0;
        }

        return Double.longBitsToDouble(byteArray2Long_Big_Endian(array));
    }

    /**
     * 字节数组转 HexString
     */
    public static String byteArray2HexString(byte[] array) {

        StringBuilder builder = new StringBuilder();
        for (byte b : array) {

            String s = Integer.toHexString(b & 0xff);
            if (s.length() < 2) {
                builder.append("0");
            }
            builder.append(s);
        }

        return builder.toString().toUpperCase();
    }

    //---------------------------------华丽的分割线-------------------------------------

    /**
     * short 转字节数组，小端
     */
    public static byte[] short2ByteArray_Little_Endian(short s) {

        byte[] array = new byte[2];

        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * short 转字节数组，大端
     */
    public static byte[] short2ByteArray_Big_Endian(short s) {

        byte[] array = new byte[2];

        for (int i = 0; i < array.length; i++) {
            array[array.length - 1 - i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * int 转字节数组，小端
     */
    public static byte[] int2ByteArray_Little_Endian(int s) {

        byte[] array = new byte[4];

        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * int 转字节数组，大端
     */
    public static byte[] int2ByteArray_Big_Endian(int s) {

        byte[] array = new byte[4];

        for (int i = 0; i < array.length; i++) {
            array[array.length - 1 - i] = (byte) (s >> (i * 8));
        }
        return array;
    }

    /**
     * float 转字节数组，小端
     */
    public static byte[] float2ByteArray_Little_Endian(float f) {

        return int2ByteArray_Little_Endian(Float.floatToIntBits(f));
    }

    /**
     * float 转字节数组，大端
     */
    public static byte[] float2ByteArray_Big_Endian(float f) {

        return int2ByteArray_Big_Endian(Float.floatToIntBits(f));
    }

    /**
     * long 转字节数组，小端
     */
    public static byte[] long2ByteArray_Little_Endian(long l) {

        byte[] array = new byte[8];

        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (l >> (i * 8));
        }
        return array;
    }

    /**
     * long 转字节数组，大端
     */
    public static byte[] long2ByteArray_Big_Endian(long l) {

        byte[] array = new byte[8];

        for (int i = 0; i < array.length; i++) {
            array[array.length - 1 - i] = (byte) (l >> (i * 8));
        }
        return array;
    }

    /**
     * double 转字节数组，小端
     */
    public static byte[] double2ByteArray_Little_Endian(double d) {

        return long2ByteArray_Little_Endian(Double.doubleToLongBits(d));
    }

    /**
     * double 转字节数组，大端
     */
    public static byte[] double2ByteArray_Big_Endian(double d) {
        return long2ByteArray_Big_Endian(Double.doubleToLongBits(d));
    }

    /**
     * char[]转byte[]
     *
     * @param chars
     * @return
     */
    public static byte[] charArrayByteArray(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    /**
     * byte转char
     *
     * @param bytes
     * @return
     */
    private char[] ByteArray2CharArray(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }

    /**
     * int[]转byte[]
     *
     * @param intArray
     * @return
     */
    public static byte[] intArrayByteArray(int[] intArray) {
        byte[] bytes = new byte[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            byte[] byteTemp = int2ByteArray_Little_Endian(intArray[i]);
            System.arraycopy(byteTemp, 0, bytes, i * 4, byteTemp.length);
        }
        return bytes;
    }


    /**
     * HexString 转字节数组
     */
    public static byte[] hexString2ByteArray(String hexString) {

        // 两个十六进制字符一个 byte，单数则有误
        if (hexString.length() % 2 != 0) {
            return null;
        }

        byte[] array = new byte[hexString.length() / 2];

        int value = 0;
        for (int i = 0; i < hexString.length(); i++) {

            char s = hexString.charAt(i);

            // 前半个字节
            if (i % 2 == 0) {
                value = Integer.parseInt(String.valueOf(s), 16) * 16;
            } else {
                // 后半个字节
                value += Integer.parseInt(String.valueOf(s), 16);
                array[i / 2] = (byte) value;
                value = 0;
            }
        }

        return array;
    }

    /**
     * 对象 转字节数组，小端
     */
    public static byte[] object2ByteArray_Little_Endian(Object object) {
        byte[] byteValue;
        if (object instanceof Short) {
            byteValue = short2ByteArray_Little_Endian((Short) object);
            return byteValue;
        }
        if (object instanceof byte[]) {
            byteValue = (byte[]) object;
            return byteValue;
        }
        if (object instanceof char[]) {
            byteValue = charArrayByteArray((char[]) object);
            return byteValue;
        }
        if (object instanceof int[]) {
            byteValue = intArrayByteArray((int[]) object);
            return byteValue;
        }
        if (object instanceof IObjToBytes) {
            byteValue = ((IObjToBytes) object).toBytes();
            return byteValue;
        }
        byteValue = int2ByteArray_Little_Endian((Integer) object);
        return byteValue;
    }

    /**
     * 对象 转字节数组，小端
     */
    public static byte[] object2ByteArray_Little_Endian(Object[] objects) {
        int objectLength = objects.length;
        byte[][] byteTemp = new byte[objectLength][];
        int byteValueLength = 0;
        for (int i = 0; i < objectLength; i++) {
            byteTemp[i] = object2ByteArray_Little_Endian(objects[i]);
            byteValueLength = byteValueLength + byteTemp[i].length;
        }
        byte[] byteValue = new byte[byteValueLength];
        int indexStart = 0;
        for (int i = 0; i < objectLength; i++) {
            System.arraycopy(byteTemp[i], 0, byteValue, indexStart, byteTemp[i].length);
            indexStart = indexStart + byteTemp[i].length;
        }
        return byteValue;
    }
}

