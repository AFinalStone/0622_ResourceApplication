package com.parse.type;

import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

/**
 * Reference to a string in a string pool.
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospo
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
    public byte[] toBytes() {
        return Object2ByteUtil.int2ByteArray_Little_Endian(index);
    }
}
