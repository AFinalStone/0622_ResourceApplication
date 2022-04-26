package com.afs.resourcearsc.bean;

import com.afs.resourcearsc.utils.Byte2ObjectUtil;
import com.afs.resourcearsc.utils.IObjToBytes;

public class ResTableStringPoolHeader implements IObjToBytes {
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
    public ResChunkHeader header;
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

    public ResTableStringPoolHeader() {
        header = new ResChunkHeader();
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
        return "header: " + "\n" + header.toString() + "\n" + "stringCount: " + stringCount + "\n" + "styleCount: " + styleCount
                + "\n" + "flags: " + flags + "\n" + "stringStart: " + stringsStart + "\n" + "stylesStart: " + stylesStart;
    }

    @Override
    public byte[] toBytes() {
        Object[] objects = {header, stringCount, styleCount, flags, stringsStart, stylesStart};
        return Byte2ObjectUtil.object2ByteArray_Little_Endian(objects);
    }
}