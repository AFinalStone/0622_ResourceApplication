package com.parse.type;

import static com.parse.util.DebugUtils.assertTrue;

import com.parse.util.ByteUtils;
import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

/**
 * @author syl
 * @time 2022/4/28 10:37
 */
public class ResStringPoolHeader implements IObjToBytes {

    /**
     * 基本 header
     */
    public ResChunkHeader header;

    /**
     * 当前字符串池拥有的字符串数
     * Number of strings in this pool (number of uint32_t indices that follow
     * in the data).
     */
    public int stringCount;

    /**
     * 当前字符串池拥有的style数
     * Number of style span arrays in the pool (number of uint32_t indices
     * follow the string indices).
     */
    public int styleCount;

    public final static int SORTED_FLAG = 1;//排序
    public final static int UTF8_FLAG = (1 << 8);//UTF_8


    interface StringFlags {

        /**
         * If set, the string index is sorted by the string values (based
         * on strcmp16()).
         */
        int SORTED_FLAG = 1 << 0;

        /**
         * String pool is encoded in UTF-8
         */
        int UTF8_FLAG = 1 << 8;
    }

    /**
     * @see StringFlags
     */
    public int flags;
    /**
     * 字符串池的字符串数据 相对于当前 header 起始的偏移
     * Index from header of the string data.
     */
    public int stringsStart;
    /**
     * 字符串池的style数据 相对于当前 header 起始的偏移
     * Index from header of the style data.
     */
    public int stylesStart;


    /**
     * 解析字符串池 chunk header
     *
     * @param src                   源数据
     * @param stringPoolChunkOffset 偏移
     * @return
     */
    public static ResStringPoolHeader parseStringPoolHeader(byte[] src, int stringPoolChunkOffset) {

        ResStringPoolHeader stringPoolHeader = new ResStringPoolHeader();

        //解析 ResChunkHeader 信息
        stringPoolHeader.header = ResChunkHeader.parseResChunkHeader(src, stringPoolChunkOffset);

        // ResChunkHeader 后面是 ResStringPoolHeader 信息
        int offset = stringPoolChunkOffset + stringPoolHeader.header.getResChunkHeaderSize();

        //获取字符串的个数
        byte[] stringCountByte = ByteUtils.copyByte(src, offset, 4);
        stringPoolHeader.stringCount = ByteUtils.byte2int(stringCountByte);

        //解析样式的个数
        byte[] styleCountByte = ByteUtils.copyByte(src, offset + 4, 4);
        stringPoolHeader.styleCount = ByteUtils.byte2int(styleCountByte);

        //这里表示字符串的格式:UTF-8/UTF-16
        byte[] flagByte = ByteUtils.copyByte(src, offset + 8, 4);
        stringPoolHeader.flags = ByteUtils.byte2int(flagByte);


        //字符串内容的开始位置
        byte[] stringStartByte = ByteUtils.copyByte(src, offset + 12, 4);
        stringPoolHeader.stringsStart = ByteUtils.byte2int(stringStartByte);

        //样式内容的开始位置
        byte[] styleStartByte = ByteUtils.copyByte(src, offset + 16, 4);
        stringPoolHeader.stylesStart = ByteUtils.byte2int(styleStartByte);

        assertTrue(offset + 20 == stringPoolHeader.header.debug_offset + stringPoolHeader.header.headerSize);

        return stringPoolHeader;
    }

    @Override
    public String toString() {
        return "ResStringPoolHeader{" +
                "header=" + header +
                ", stringCount=" + stringCount +
                ", styleCount=" + styleCount +
                ", flags=" + flags +
                ", stringsStart=" + stringsStart +
                ", stylesStart=" + stylesStart +
                '}';
    }


    @Override
    public byte[] toBytes() {
        Object[] objects = {header, stringCount, styleCount, flags, stringsStart, stylesStart};
        byte[] byteStringPoolHeader = Object2ByteUtil.object2ByteArray_Little_Endian(objects);
        return byteStringPoolHeader;
    }
}
