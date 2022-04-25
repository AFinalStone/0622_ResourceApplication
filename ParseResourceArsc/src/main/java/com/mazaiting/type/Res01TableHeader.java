package com.mazaiting.type;

import com.mazaiting.ByteArrayUtil;
import com.mazaiting.IObjToBytes;

/**
 * Resources.arsc文件的第一个结构是资源索引表头部
 * 描述Resources.arsc文件的大小和资源包数量
 * <p>
 * struct ResTable_header
 * {
 * struct ResChunk_header header;
 * <p>
 * // The number of ResTable_package structures.
 * uint32_t packageCount;
 * };
 *
 * @author mazaiting
 */
public class Res01TableHeader implements IObjToBytes {
    /**
     * 标准的Chunk头部信息格式
     */
    public Res00ChunkHeader header;
    /**
     * 被编译的资源包个数
     * Android 中一个apk可能包含多个资源包，默认情况下都只有一个就是应用的包名所在的资源包
     */
    public int packageCount;

    public Res01TableHeader() {
        header = new Res00ChunkHeader();
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
        return "header:" + header.toString() + "\n" + "packageCount:" + packageCount;
    }

    @Override
    public byte[] toBytes() {
        Object[] objects = {header, packageCount};
        return ByteArrayUtil.object2ByteArray_Little_Endian(objects);
    }
}
