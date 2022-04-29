package com.afs.resourcearsc.bean;

import com.afs.resourcearsc.utils.Byte2ObjectUtil;
import com.afs.resourcearsc.utils.IObjToBytes;

/**
 * Resource.arsc表的头部信息
 *
 * @author syl
 * @time 2022/4/26 11:54
 */
public class ResTableHeader implements IObjToBytes {
    /**
     * 标准的Chunk头部信息格式
     */
    public ResChunkHeader header;
    /**
     * 被编译的资源包个数
     * Android 中一个apk可能包含多个资源包，默认情况下都只有一个就是应用的包名所在的资源包
     */
    public int packageCount;

    public ResTableHeader() {
    }

    /**
     * 获取当前Table Header所占字节数
     *
     * @return
     */
    public int getHeaderSize() {
        return header.getHeaderSize() + 4;
    }

    @Override
    public String toString() {
        return "header:" + "\n" + header.toString() + "\n" + "packageCount:" + packageCount;
    }

    @Override
    public byte[] toBytes() {
        Object[] objects = {header, packageCount};
        return Byte2ObjectUtil.object2ByteArray_Little_Endian(objects);
    }

}
