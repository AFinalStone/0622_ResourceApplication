package com.afs.resourcearsc.bean;

import com.afs.resourcearsc.utils.IObjToBytes;

/**
 * @author syl
 * @time 2022/4/26 12:59
 */
public class ResTableTypeType implements IObjToBytes {
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
    /**
     * 资源项数组块相对头部的偏移值
     */
    public int entriesStart;
    /**
     * 指向一个ResTable_config，用来描述配置信息，地区，语言，分辨率等
     */
    public ResTableConfig resConfig;


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
        return "header: " + header.toString() + ",id: " + id + ",res0: " + res0 + ",res1: " + res1 +
                ",entryCount: " + entryCount + ",entriesStart: " + entriesStart;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}