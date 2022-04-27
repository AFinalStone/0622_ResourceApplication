//package com.afs.resourcearsc.bean;
//
//import com.afs.resourcearsc.utils.Byte2ObjectUtil;
//import com.afs.resourcearsc.utils.IObjToBytes;
//
//public class ResTablePackageStringPoolHeader implements IObjToBytes {
//    /**
//     * 排序标记
//     */
//    public final static int SORTED_FLAG = 1;
//    /**
//     * UTF-8编码标识
//     */
//    public final static int UTF8_FLAG = (1 << 8);
//    /**
//     * 标准的Chunk头部信息结构
//     */
//    public ResChunkHeader header;
//    /**
//     * 字符串的个数
//     */
//    public int stringCount;
//    /**
//     * 字符串样式的个数
//     */
//    public int styleCount;
//    /**
//     * 字符串的属性，可取值包括0x000(UTF-16)，0x001(字符串经过排序)，0x100(UTF-8)和他们的组合值
//     */
//    public int flags;
//    /**
//     * 字符串内容块相对于其头部的距离
//     */
//    public int stringsStart;
//    /**
//     * 字符串样式块相对于其头部的距离
//     */
//    public int stylesStart;
//    /**
//     * 字符串偏移数组和Style偏移数组的总长度
//     */
//    public byte[] byteStringOffSet;
//
//    public ResTablePackageStringPoolHeader() {
//        header = new ResChunkHeader();
//    }
//
//    /**
//     * 获取当前String Pool Header所占字节数
//     *
//     * @return
//     */
//    public int getHeaderSize() {
//        return header.getHeaderSize() + 4 + 4 + 4 + 4 + 4;
//    }
//
//    @Override
//    public String toString() {
//        return "header: " + "\n" + header.toString() + "\n" + "stringCount: " + stringCount + "\n" + "styleCount: " + styleCount
//                + "\n" + "flags: " + flags + "\n" + "stringStart: " + stringsStart + "\n" + "stylesStart: " + stylesStart;
//    }
//
//    @Override
//    public byte[] toBytes() {
//        Object[] objects = {header, stringCount, styleCount, flags, stringsStart, stylesStart};
//        byte[] byteStringPoolHeader = Byte2ObjectUtil.object2ByteArray_Little_Endian(objects);
//        byte[] byteStringPool = new byte[byteStringOffSet.length + byteStringPoolHeader.length];
//        System.arraycopy(byteStringPoolHeader, 0, byteStringPool, 0, byteStringPoolHeader.length);
//        System.arraycopy(byteStringOffSet, 0, byteStringPool, byteStringPoolHeader.length, byteStringOffSet.length);
//        return byteStringPool;
//    }
//}