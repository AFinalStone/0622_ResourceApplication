package com.afs.resourcearsc.utils;

import com.afs.resourcearsc.bean.ResChunkHeader;
import com.afs.resourcearsc.bean.ResTable;
import com.afs.resourcearsc.bean.ResTableHeader;
import com.afs.resourcearsc.bean.ResTablePackage;
import com.afs.resourcearsc.bean.ResTableStringPool;
import com.afs.resourcearsc.bean.ResTableStringPoolHeader;

import java.util.ArrayList;

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

    private static ResTable parseResTable(byte[] arscArray) {
        int offset = 0;
        ResChunkHeader resChunkHeader = parseResChunkHeader(arscArray, offset);
        //header
        ResTableHeader resTableHeader = new ResTableHeader();
        resTableHeader.header = resChunkHeader;


        ResTable resTable = new ResTable();
        return resTable;
    }

    /**
     * 解析资源头部信息
     * 所有的Chunk公共的头部信息
     *
     * @param arscArray 数组
     * @param offSet    开始位置
     * @return
     */
    public static ResChunkHeader parseResChunkHeader(byte[] arscArray, int offSet) {
        ResChunkHeader header = new ResChunkHeader();
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
    public static ResTableHeader parseResTableHeaderChunk(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        ResChunkHeader resChunkHeader = parseResChunkHeader(arscArray, offSet);
        //解析PackageCount个数（一个apk可能包含多个Package资源）
        byte[] packageCountByte = copyByte(arscArray, 0 + resChunkHeader.getHeaderSize(), 4);
        int packageCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(packageCountByte);
        //创建ResTableHeader
        ResTableHeader resTableHeader = new ResTableHeader();
        resTableHeader.header = resChunkHeader;
        resTableHeader.packageCount = packageCount;
        return resTableHeader;
    }

    /**
     * 解析字符串常量池
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    public static ResTableStringPoolHeader parseResStringPoolHeader(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        int startIndex = offSet;
        ResTableStringPoolHeader resStringPoolHeader = new ResTableStringPoolHeader();
        resStringPoolHeader.header = parseResChunkHeader(arscArray, offSet);

        //stringCount
        startIndex = startIndex + resStringPoolHeader.header.getHeaderSize();
        byte[] stringCountByte = copyByte(arscArray, startIndex, 4);
        resStringPoolHeader.stringCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(stringCountByte);

        //styleCount
        startIndex = startIndex + stringCountByte.length;
        byte[] styleCountByte = copyByte(arscArray, startIndex, 4);
        resStringPoolHeader.styleCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(styleCountByte);

        //flags
        startIndex = startIndex + styleCountByte.length;
        byte[] flagsByte = copyByte(arscArray, startIndex, 4);
        resStringPoolHeader.flags = Byte2ObjectUtil.byteArray2Int_Little_Endian(flagsByte);

        //stringsStart
        startIndex = startIndex + flagsByte.length;
        byte[] stringsStartByte = copyByte(arscArray, startIndex, 4);
        resStringPoolHeader.stringsStart = Byte2ObjectUtil.byteArray2Int_Little_Endian(stringsStartByte);

        //stylesStart
        startIndex = startIndex + stringsStartByte.length;
        byte[] stylesStartByte = copyByte(arscArray, startIndex, 4);
        resStringPoolHeader.stylesStart = Byte2ObjectUtil.byteArray2Int_Little_Endian(stylesStartByte);

        return resStringPoolHeader;
    }


    /**
     * 解析字符串常量池
     *
     * @param arscArray
     * @param offSet
     * @param stringCount
     * @param styleCount
     * @return
     */
    public static ResTableStringPool parseResStringPool(byte[] arscArray, int offSet, int stringCount, int styleCount) {
        //解析ChunkHeader
        ResTableStringPool resTableStringPool = new ResTableStringPool();
        int startIndex = offSet;
        //字符串
        ArrayList<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < stringCount) {
            byte[] stringSizeByte = copyByte(arscArray, startIndex, 2);
            int stringSize = (stringSizeByte[1] & 0x7F);
            if (0 != stringSize) {
                String val = "";
                try {
                    val = new String(copyByte(arscArray, startIndex + 2, stringSize), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                strings.add(val);
            } else {
                strings.add("");
            }
            startIndex += (stringSize + 3);
            index++;
        }
        resTableStringPool.stringList = strings;
        //样式串
        startIndex = offSet + stringCount * 4;
        ArrayList<String> styles = new ArrayList<String>();
        index = 0;
        while (index < styleCount) {
            byte[] styleSizeByte = copyByte(arscArray, startIndex, 2);
            //20 = 0x14 = 1_0100
            //127 = 0x7F = 0111_1111
            //
            int styleSize = (styleSizeByte[1] & 0x7F);
            if (0 != styleSize) {
                String val = "";
                try {
                    val = new String(copyByte(arscArray, startIndex + 2, styleSize), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                styles.add(val);
            } else {
                styles.add("");
            }
            startIndex += (styleSize + 3);
            index++;
        }
        resTableStringPool.styleList = styles;
        return resTableStringPool;
    }


    /**
     * 解析资源Package块
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    public static ResTablePackage parseResTablePackage(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        int startIndex = offSet;
        ResTablePackage resTablePackage = new ResTablePackage();
        resTablePackage.header = parseResChunkHeader(arscArray, offSet);

        //id
        startIndex = startIndex + resTablePackage.header.getHeaderSize();
        byte[] idByte = copyByte(arscArray, startIndex, 4);
        resTablePackage.id = Byte2ObjectUtil.byteArray2Int_Little_Endian(idByte);

        //name
        startIndex = startIndex + idByte.length;
        byte[] nameByte = copyByte(arscArray, startIndex, 128 * 2);
        resTablePackage.name = new String(nameByte);

        //typeStrings
        startIndex = startIndex + nameByte.length;
        byte[] typeStringsByte = copyByte(arscArray, startIndex, 4);
        resTablePackage.typeStrings = Byte2ObjectUtil.byteArray2Int_Little_Endian(typeStringsByte);

        //lastPublicType
        startIndex = startIndex + typeStringsByte.length;
        byte[] lastPublicTypeByte = copyByte(arscArray, startIndex, 4);
        resTablePackage.lastPublicType = Byte2ObjectUtil.byteArray2Int_Little_Endian(lastPublicTypeByte);

        //keyStrings
        startIndex = startIndex + lastPublicTypeByte.length;
        byte[] keyStringsByte = copyByte(arscArray, startIndex, 4);
        resTablePackage.keyStrings = Byte2ObjectUtil.byteArray2Int_Little_Endian(keyStringsByte);

        //lastPublicKey
        startIndex = startIndex + keyStringsByte.length;
        byte[] lastPublicKeyByte = copyByte(arscArray, startIndex, 4);
        resTablePackage.lastPublicKey = Byte2ObjectUtil.byteArray2Int_Little_Endian(lastPublicKeyByte);

        return resTablePackage;
    }
}

























