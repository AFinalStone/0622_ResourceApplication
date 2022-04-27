package com.afs.resourcearsc.bean;

import com.afs.resourcearsc.utils.IObjToBytes;

/**
 * @author syl
 * @time 2022/4/26 12:59
 */
public class ResTableTypeSpec implements IObjToBytes {
    /**
     * NO_ENTRY常量
     */
    public final static int NO_ENTRY = 0xFFFFFFFF;
    /**
     * Chunk的头部信息结构
     */
    public ResChunkHeader header;
    /**
     * 标识资源的Type ID
     * Type ID是指资源的类型ID，从1开始。资源的类型有animator、anim、color、drawable、layout、menu、raw、string和xml等等若干种，每一种都会被赋予一个ID。
     */
    public byte id;
    /**
     * 保留，始终为0
     */
    public byte res0;
    /**
     * 保留，始终为0
     */
    public short res1;
    /**
     * 本类型资源项个数，指名称相同的资源项的个数
     */
    public int entryCount;
    public int[] entryArray;
    /**
     * 资源项数组块相对头部的偏移值
     */
    public int entriesStart;

    /**
     * 获取当前资源类型所占的字节数
     *
     * @return
     */
    public int getSize() {
        return header.getHeaderSize() + 1 + 1 + 2 + 4 + 4;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (entryArray != null) {
            for (Integer item : entryArray) {
                stringBuilder.append(item).append(" ");
            }
        }
        return "header: " + "\n" + header.toString() + "\n" + "id: " + id + "\n" + "res0: " + res0 + "\n" + "res1: " + res1
                + "\n" + "entryCount: " + entryCount + "\n" + "entryArray: " + stringBuilder.toString() + "\n" + "entriesStart: " + entriesStart;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}