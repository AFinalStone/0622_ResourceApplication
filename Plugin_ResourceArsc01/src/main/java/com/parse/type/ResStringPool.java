package com.parse.type;

import static com.parse.util.DebugUtils.assertTrue;

import com.parse.util.ByteUtils;
import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串池
 *
 * @author syl
 * @time 2022/4/28 10:38
 */
public class ResStringPool implements IObjToBytes {

    /**
     * 字符串池 chunk header
     */
    public ResStringPoolHeader header;
    /**
     * 字符串索引
     */
    public int[] stringIndexArray;
    /**
     * style索引
     */
    public int[] styleIndexArray;
    /**
     * 当前字符串池拥有的字符串
     */
    public List<String> strings;
    /**
     * 当前字符串池拥有的style
     */
    public List<ResStringPoolSpanList> styles;

    public ResStringPool(ResStringPoolHeader header) {
        this.header = header;
    }

    /**
     * 解析字符串池chunk 的 body 部分
     *
     * @param src
     * @param stringPoolChunkOffset
     * @return
     */
    public static ResStringPool parseStringPoolChunk(byte[] src, int stringPoolChunkOffset) {
        /*
          头部信息
         */
        ResStringPoolHeader stringPoolHeader = ResStringPoolHeader.parseStringPoolHeader(src, stringPoolChunkOffset);
        ResStringPool pool = new ResStringPool(stringPoolHeader);

        /*
           内容信息
         */
        //获取字符串内容的索引数组和样式内容的索引数组
        int[] stringIndexArray = new int[stringPoolHeader.stringCount];
        int[] styleIndexArray = new int[stringPoolHeader.styleCount];

        //        int stringIndex = offset + 20;
        // stringPool header 后面就是 stringIndex 数组
        int stringIndex = stringPoolHeader.header.debug_offset + stringPoolHeader.header.headerSize;
        for (int i = 0; i < stringPoolHeader.stringCount; i++) {
            stringIndexArray[i] = ByteUtils.byte2int(ByteUtils.copyByte(src, stringIndex + i * 4, 4));
        }
        // stringIndex 数组后面是 style 数组
        int styleIndex = stringIndex + 4 * stringPoolHeader.stringCount;
        for (int i = 0; i < stringPoolHeader.styleCount; i++) {
            styleIndexArray[i] = ByteUtils.byte2int(ByteUtils.copyByte(src, styleIndex + i * 4, 4));
        }

        pool.stringIndexArray = stringIndexArray;
        pool.styleIndexArray = styleIndexArray;

        parseStrings(pool, src);
        parseStyles(pool, src);
        return pool;
    }

    /**
     * 解析字符串池的 字符串数据
     *
     * @param pool
     * @param src
     */
    private static void parseStyles(ResStringPool pool, byte src[]) {

        ResStringPoolHeader stringPoolHeader = pool.header;
        boolean encodeUtf16 = stringPoolHeader.flags == 0;

        int spanStart = pool.header.header.debug_offset + pool.header.stylesStart;
        int stingPoolEnd = pool.header.header.debug_offset + pool.header.header.size;
        pool.styles = new ArrayList<>();
        for (int i = 0; i < pool.header.styleCount; i++) {
            int spOff = pool.styleIndexArray[i] + spanStart;
            List<ResStringPoolSpan> spans = new ArrayList<>();
            while (spOff < stingPoolEnd) {
                byte[] nameBytes = ByteUtils.copyByte(src, spOff, 4);
                if ((nameBytes[0] == ResStringPoolSpan.END && nameBytes[1] == ResStringPoolSpan.END)) {
                    break;
                }
                ResStringPoolSpan span = new ResStringPoolSpan();
                span.name = new ResStringPoolRef();
                span.name.index = ByteUtils.byte2int(nameBytes);
                span.firstChar = ByteUtils.byte2int(ByteUtils.copyByte(src, spOff + 4, 4));
                span.lastChar = ByteUtils.byte2int(ByteUtils.copyByte(src, spOff + 8, 4));
                spans.add(span);
                spOff += 12;
            }
            ResStringPoolSpanList stringType = new ResStringPoolSpanList();
            stringType.spans = spans;
            pool.styles.add(stringType);
        }
    }


    /**
     * 解析字符串池的 style 数据
     *
     * @param pool
     * @param src
     */
    private static void parseStrings(ResStringPool pool, byte src[]) {

        ResStringPoolHeader stringPoolHeader = pool.header;
        boolean encodeUtf16 = stringPoolHeader.flags == 0;

        //每个字符串的头两个字节的最后一个字节是字符串的长度
        //这里获取所有字符串的内容
        //int stringContentIndex = styleIndex + stringPoolHeader.styleCount * 4;
        //assertTrue(stringContentIndex == pool.header.header.debug_offset + pool.header.stringsStart);
        int stringContentIndex = pool.header.header.debug_offset + pool.header.stringsStart;
        int stringStartOffset = pool.header.header.debug_offset + pool.header.stringsStart;
        int index = 0;
        List<String> strings = new ArrayList<>();
        while (index < stringPoolHeader.stringCount) {
            assertTrue(stringContentIndex == pool.stringIndexArray[index] + stringStartOffset);
            int stringSize = 0;
            if (index + 1 < stringPoolHeader.stringCount) {
                int nextOffset = pool.stringIndexArray[index + 1];
                int curOffset = pool.stringIndexArray[index];
                int metaSize = (encodeUtf16 ? 2 : 1) + 2;
                stringSize = (nextOffset - curOffset - metaSize);
                //assertTrue(stringSize == (debug_string_size));
            } else {
                //这个应该也有问题。可能会对齐。。。
                int nextOffset = pool.header.stylesStart > 0 ? pool.header.stylesStart - pool.header.stringsStart : (pool.header.header.size + pool.header.header.debug_offset - stringStartOffset);
                int curOffset = pool.stringIndexArray[index];
                int metaSize = (encodeUtf16 ? 2 : 1) + 2;
                stringSize = (nextOffset - curOffset - metaSize);
                byte[] stringSizeByte = ByteUtils.copyByte(src, stringContentIndex, 2);
                //实测正确。。应该有问题。。Orz
                int debug_string_size = encodeUtf16 ? (stringSizeByte[0] | stringSizeByte[1] << 8) * 2 : ((stringSizeByte[1] & 0x7F));
                //assertTrue(stringSize == (debug_string_size));
                stringSize = debug_string_size;
            }
            if (stringSize != 0) {
                String val = "";
                try {
                    String charset = encodeUtf16 ? "UTF-16LE" : "UTF-8";
                    val = new String(ByteUtils.copyByte(src, stringContentIndex + 2, stringSize), charset);
                } catch (Exception e) {

                }
                strings.add(val);
            } else {
                strings.add("");
            }
            //2 字节的 stringSize, utf16 以 0x00 00 结尾， utf8 以 0x00 结尾
            stringContentIndex += (stringSize + 2 + (encodeUtf16 ? 2 : 1));
            index++;
        }
        pool.strings = strings;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (strings != null) {
            for (String item : strings) {
                if (!item.isEmpty())
                    stringBuilder.append(item).append(" ");
            }
        }
        return "ResStringPool{" +
                "header=" + header +
                ", stringIndexArray=" + Arrays.toString(stringIndexArray) +
                ", styleIndexArray=" + Arrays.toString(styleIndexArray) +
                ", strings=" + stringBuilder +
                ", styles=" + styles +
                '}';
    }

    @Override
    public byte[] toBytes() {
        byte[] bytesHeader = header.toBytes();
        byte[] byteStringIndexArray = Object2ByteUtil.intArrayByteArray(stringIndexArray);
        byte[] byteStyleIndexArray = Object2ByteUtil.intArrayByteArray(styleIndexArray);
        byte[] byteStrings = getByteString();
        byte[] byteStyle = getByteStyle();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytesHeader.length + byteStringIndexArray.length + byteStyleIndexArray.length + byteStrings.length + byteStyle.length);
        byteBuffer.put(bytesHeader);
        byteBuffer.put(byteStringIndexArray);
        byteBuffer.put(byteStyleIndexArray);
        byteBuffer.put(byteStrings);
        byteBuffer.put(byteStyle);
        byteBuffer.flip();
        return byteBuffer.array();
    }

    private byte[] getByteString() {
        byte[] byteStrings;
        int lenByteStrings = 0;
        if (ResStringPoolHeader.UTF8_FLAG == header.flags) {
            for (int i = 0; i < strings.size(); i++) {
                int itemLen = strings.get(i).getBytes(StandardCharsets.UTF_8).length;
                lenByteStrings = lenByteStrings + itemLen + 3;
            }
            byteStrings = new byte[lenByteStrings];
            int index = 0;
            for (int i = 0; i < strings.size(); i++) {
                String item = strings.get(i);
                int len = item.length();
                int lenByte = item.getBytes(StandardCharsets.UTF_8).length;
                byteStrings[index++] = (byte) len;
                byteStrings[index++] = (byte) lenByte;
                if (item.length() > 0) {
                    byte[] byteStr = item.getBytes(StandardCharsets.UTF_8);
                    System.arraycopy(byteStr, 0, byteStrings, index, byteStr.length);
                    index = index + byteStr.length;
                }
                byteStrings[index++] = 0;
            }
        } else {
            for (int i = 0; i < strings.size(); i++) {
                int itemLen = strings.get(i).getBytes(StandardCharsets.UTF_8).length * 2;
                lenByteStrings = lenByteStrings + itemLen + 4;
            }
            byteStrings = new byte[lenByteStrings];
            int offset = 0;
            for (int i = 0; i < strings.size(); i++) {
                String item = strings.get(i);
                short lenByte = (short) (item.getBytes(StandardCharsets.UTF_8).length * 2);
                byte[] byteLen = Object2ByteUtil.short2ByteArray_Little_Endian(lenByte);
                System.arraycopy(byteLen, 0, byteStrings, offset, byteLen.length);
                offset = offset + 2;
                if (item.length() > 0) {
                    byte[] byteStr = item.getBytes(StandardCharsets.UTF_8);
                    System.arraycopy(byteStr, 0, byteStrings, offset, byteStr.length);
                    offset = offset + byteStr.length;
                }
                byteStrings[offset++] = 0;
                byteStrings[offset++] = 0;
            }
        }
        return byteStrings;
    }

    private byte[] getByteStyle() {
        byte[] byteStyles;
        int lenByteStyle = 0;
        for (int i = 0; i < styles.size(); i++) {
            lenByteStyle = lenByteStyle + styles.get(i).toBytes().length;
        }
        byteStyles = new byte[lenByteStyle];
        int offset = 0;
        for (int i = 0; i < styles.size(); i++) {
            byte[] byteStyle = styles.get(i).toBytes();
            System.arraycopy(byteStyle, 0, byteStyle, offset, byteStyle.length);
            offset = offset + byteStyle.length;
        }
        return byteStyles;
    }
}
