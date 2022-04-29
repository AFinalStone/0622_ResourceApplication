package com.parse.type;

import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

/**
 * @author syl
 * @time 2022/4/28 10:38
 */
public class ResStringPoolRef implements IObjToBytes {

    /**
     * Index into the string pool table (uint32_t-offset from the indices
     * immediately after ResStringPool_header) at which to find the location
     * of the string data in the pool.
     */
    public int index;

    public static int getSize() {
        return 4;
    }

    @Override
    public String toString() {
        return "ResStringPoolRef{" +
                "index=" + index +
                '}';
    }

    @Override
    public byte[] toBytes() {
        return Object2ByteUtil.int2ByteArray_Little_Endian(index);
    }
}
