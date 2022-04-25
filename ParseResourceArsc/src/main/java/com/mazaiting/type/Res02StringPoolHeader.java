package com.mazaiting.type;

import com.mazaiting.ByteArrayUtil;
import com.mazaiting.IObjToBytes;

/**
 * 资源索引表头部的是资源项的值字符串资源池，这个字符串资源池包含了所有的在资源包里面所定义的资源项的值字符串
 * 一个字符串可以对应多个ResStringPool_span和一个ResStringPool_ref
 * struct ResStringPool_header
 * {
 * struct ResChunk_header header;
 * <p>
 * // Number of strings in this pool (number of uint32_t indices that follow
 * // in the data).
 * uint32_t stringCount;
 * <p>
 * // Number of style span arrays in the pool (number of uint32_t indices
 * // follow the string indices).
 * uint32_t styleCount;
 * <p>
 * // Flags.
 * enum {
 * // If set, the string index is sorted by the string values (based
 * // on strcmp16()).
 * SORTED_FLAG = 1<<0,
 * <p>
 * // String pool is encoded in UTF-8
 * UTF8_FLAG = 1<<8
 * };
 * uint32_t flags;
 * <p>
 * // Index from header of the string data.
 * uint32_t stringsStart;
 * <p>
 * // Index from header of the style data.
 * uint32_t stylesStart;
 * };
 *
 * @author mazaiting
 */
public class Res02StringPoolHeader implements IObjToBytes {
    /**
     * 排序标记
     */
    public final static int SORTED_FLAG = 1;
    /**
     * UTF-8编码标识
     */
    public final static int UTF8_FLAG = (1 << 8);
    /**
     * 标准的Chunk头部信息结构
     */
    public Res00ChunkHeader header;
    /**
     * 字符串的个数
     */
    public int stringCount;
    /**
     * 字符串样式的个数
     */
    public int styleCount;
    /**
     * 字符串的属性，可取值包括0x000(UTF-16)，0x001(字符串经过排序)，0x100(UTF-8)和他们的组合值
     */
    public int flags;
    /**
     * 字符串内容块相对于其头部的距离
     */
    public int stringsStart;
    /**
     * 字符串样式块相对于其头部的距离
     */
    public int stylesStart;

    public Res02StringPoolHeader() {
        header = new Res00ChunkHeader();
    }

    /**
     * 获取当前String Pool Header所占字节数
     *
     * @return
     */
    public int getHeaderSize() {
        return header.getHeaderSize() + 4 + 4 + 4 + 4 + 4;
    }

    @Override
    public String toString() {
        return "header: " + header.toString() + "\n" + "stringCount: " + stringCount + ",styleCount: " + styleCount
                + ",flags: " + flags + ",stringStart: " + stringsStart + ",stylesStart: " + stylesStart;
    }

    @Override
    public byte[] toBytes() {
        Object[] objects = {header, stringCount, styleCount, flags, stringsStart, stylesStart};
        return ByteArrayUtil.object2ByteArray_Little_Endian(objects);
    }
}