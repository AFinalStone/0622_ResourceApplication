package com.parse.type;

import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

/**
 * 资源配置
 *
 * @author syl
 * @time 2022/4/28 10:40
 */
public class ResTableTypeSpecHeader implements IObjToBytes {

    public final static int SPEC_PUBLIC = 0x40000000;
    /**
     * chunk header
     */
    public ResChunkHeader header;
    /**
     * 资源类型id
     */
    public byte id;
    //保留
    public byte res0;
    //保留
    public short res1;
    /**
     * 当前类型，同名资源的个数
     */
    public int entryCount;

    public ResTableTypeSpecHeader() {

    }

    @Override
    public String toString() {
        return "ResTableTypeSpecHeader{" +
                "header=" + header +
                ", id=" + id +
                ", res0=" + res0 +
                ", res1=" + res1 +
                ", entryCount=" + entryCount +
                '}';
    }

    @Override
    public byte[] toBytes() {
        return Object2ByteUtil.object2ByteArray_Little_Endian(new Object[]{header, id, res0, res1, entryCount});
    }
}
