package com.afs.resourcearsc.bean;

import com.afs.resourcearsc.utils.Byte2ObjectUtil;
import com.afs.resourcearsc.utils.IObjToBytes;

import java.io.UnsupportedEncodingException;

/**
 * @author syl
 * @time 2022/4/26 12:59
 */
public class ResTablePackageHeader implements IObjToBytes {
    /**
     * Chunk的头部信息数据结构
     */
    public ResChunkHeader header;
    /**
     * 包的ID，等于package id,一般用户包的值Package Id为0X7F，系统资源包Pacage Id为0X01
     * 这个值会在构建public.xml中的id值时用到
     */
    public int id;
    /**
     * 包名
     */
//    public char[] name = new char[128];
    public String name;
    /**
     * 类型字符串资源池相对头部的偏移
     */
    public int typeStrings;
    /**
     * 最后一个到处的public类型字符串在类型字符串资源池中的索引，目前这个值设置为类型字符串资源池的元素格尔书
     */
    public int lastPublicType;
    /**
     * 资源项名称字符串相对头部的偏移
     */
    public int keyStrings;
    /**
     * 最后一个导出的Public资源项名称字符串在资源项名称字符串资源池中的索引，目前这个值设置为资源项名称字符串资源池的元素个数
     */
    public int lastPublicKey;

    public ResTablePackageHeader() {
        header = new ResChunkHeader();
    }

    @Override
    public String toString() {
        return "header: " + "\n" + header.toString() + "\n" + "id= " + id + "\n" + "name: " + name.toString() + "\n" +
                "typeStrings:" + typeStrings + "\n" + "lastPublicType: " + lastPublicType + "\n" + "keyStrings: " + keyStrings
                + "\n" + "lastPublicKey: " + lastPublicKey;
    }

    @Override
    public byte[] toBytes() {
        byte[] byteStrName = new byte[256];
        try {
            byteStrName = ((String) name).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Object[] objects = {header, id, byteStrName, typeStrings, lastPublicType, keyStrings, lastPublicKey};
        return Byte2ObjectUtil.object2ByteArray_Little_Endian(objects);
    }
}