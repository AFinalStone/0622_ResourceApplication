package com.afs.resourcearsc.utils;

import com.afs.resourcearsc.bean.Res00ChunkHeader;
import com.afs.resourcearsc.bean.Res01TableHeader;
import com.afs.resourcearsc.bean.Res02StringPool;
import com.afs.resourcearsc.bean.Res02StringPoolHeader;

public class ParseResourceUtil {

    private static int mResStringPoolChunkOffset;

    /**
     * 把目标字节数组中的特定字节拷贝出来
     *
     * @param sourceBytes
     * @param start
     * @param length
     */
    public static byte[] copyByte(byte[] sourceBytes, int start, int length) {
        byte[] byteType = new byte[length];
        System.arraycopy(sourceBytes, start, byteType, 0, length);
        return byteType;
    }

    /**
     * 解析资源头部信息
     * 所有的Chunk公共的头部信息
     *
     * @param arscArray 数组
     * @param offSet    开始位置
     * @return
     */
    private static Res00ChunkHeader parseResChunkHeader(byte[] arscArray, int offSet) {
        Res00ChunkHeader header = new Res00ChunkHeader();
        // 解析头部类型
        int startIndex = offSet;
        byte[] byteType = copyByte(arscArray, startIndex, 2);
        header.type = Byte2ObjectUtil.byteArray2Short_Little_Endian(byteType);

        // 解析头部大小
        startIndex = startIndex + byteType.length;
        byte[] byteHeaderSize = copyByte(arscArray, startIndex, 2);
        header.headerSize = Byte2ObjectUtil.byteArray2Short_Little_Endian(byteHeaderSize);

        // 解析整个Chunk的大小
        startIndex = startIndex + byteHeaderSize.length;
        byte[] byteSize = copyByte(arscArray, startIndex, 4);
        header.size = Byte2ObjectUtil.byteArray2Int_Little_Endian(byteSize);
        return header;
    }

    /**
     * 解析头部信息
     *
     * @param arscArray arsc二进制数据
     */
    public static Res01TableHeader parseResTableHeaderChunk(byte[] arscArray) {
        //解析ChunkHeader
        Res01TableHeader resTableHeader = new Res01TableHeader();
        resTableHeader.header = parseResChunkHeader(arscArray, 0);
        // 解析PackageCount个数（一个apk可能包含多个Package资源）
        byte[] packageCountByte = copyByte(arscArray, 0 + resTableHeader.header.getHeaderSize(), 4);
        resTableHeader.packageCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(packageCountByte);
        return resTableHeader;
    }

    /**
     * 解析字符串常量池
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    public static Res02StringPoolHeader parseResStringPoolHeader(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        int startIndex = offSet;
        Res02StringPoolHeader res02StringPoolHeader = new Res02StringPoolHeader();
        res02StringPoolHeader.header = parseResChunkHeader(arscArray, offSet);

        //stringCount
        startIndex = startIndex + res02StringPoolHeader.header.getHeaderSize();
        byte[] stringCountByte = copyByte(arscArray, startIndex, 4);
        res02StringPoolHeader.stringCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(stringCountByte);

        //styleCount
        startIndex = startIndex + stringCountByte.length;
        byte[] styleCountByte = copyByte(arscArray, startIndex, 4);
        res02StringPoolHeader.styleCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(styleCountByte);

        //flags
        startIndex = startIndex + styleCountByte.length;
        byte[] flagsByte = copyByte(arscArray, startIndex, 4);
        res02StringPoolHeader.flags = Byte2ObjectUtil.byteArray2Int_Little_Endian(flagsByte);

        //stringsStart
        startIndex = startIndex + flagsByte.length;
        byte[] stringsStartByte = copyByte(arscArray, startIndex, 4);
        res02StringPoolHeader.stringsStart = Byte2ObjectUtil.byteArray2Int_Little_Endian(flagsByte);

        //stylesStart
        startIndex = startIndex + stringsStartByte.length;
        byte[] stylesStartByte = copyByte(arscArray, startIndex, 4);
        res02StringPoolHeader.stylesStart = Byte2ObjectUtil.byteArray2Int_Little_Endian(stylesStartByte);

        return res02StringPoolHeader;
    }


    /**
     * 解析字符串常量池
     *
     * @param arscArray
     * @param stringStart
     * @param stringCount
     * @param styleStart
     * @param styleCount
     * @return
     */
    public static Res02StringPool parseResStringPool(byte[] arscArray, int stringStart, int stringCount, int styleStart, int styleCount) {
        //解析ChunkHeader
        Res02StringPool res02StringPool = new Res02StringPool();
        byte[] stringByte = copyByte(arscArray, stringStart, 4 * stringCount);
        res02StringPool.string = new String(stringByte);
        byte[] styleByte = copyByte(arscArray, styleStart, 4 * styleCount);
        res02StringPool.string = new String(styleByte);
        return res02StringPool;
    }

}
