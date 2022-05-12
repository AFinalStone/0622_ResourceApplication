package com.parse.type;

import com.parse.util.IObjToBytes;

/**
 * 例如 string-hdpi.xml, string-zh.xml,string-xxhdpi-cn.xml 对应不同的  ResTableType
 *
 * @author syl
 * @time 2022/4/28 10:40
 */
public class ResTableTypeHeader implements IObjToBytes {

    public ResChunkHeader header;

    public final static int NO_ENTRY = 0xFFFFFFFF;

    public byte id;
    //保留
    public byte res0;
    //保留
    public short res1;
    /**
     * 资源实体个数
     */
    public int entryCount;
    /**
     * 资源实体相对于本chunk开始的偏移
     */
    public int entriesStart;

    /**
     * 当前资源类型的配置，分辨率，屏幕大小等。
     */
    public ResTableConfig resConfig;

    public ResTableTypeHeader() {
        resConfig = new ResTableConfig();
    }

    @Override
    public String toString() {
        return "ResTableTypeHeader{" +
                "header=" + header +
                ", id=" + id +
                ", res0=" + res0 +
                ", res1=" + res1 +
                ", entryCount=" + entryCount +
                ", entriesStart=" + entriesStart +
                ", resConfig=" + resConfig +
                '}';
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}
