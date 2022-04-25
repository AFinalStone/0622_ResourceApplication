package com.afs.resourcearsc.utils;

import com.afs.resourcearsc.bean.Res00ChunkHeader;
import com.afs.resourcearsc.bean.Res01TableHeader;
import com.afs.resourcearsc.bean.Res02StringPool;
import com.afs.resourcearsc.bean.Res02StringPoolHeader;
import com.afs.resourcearsc.bean.Res03TablePackage;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
     * @param offSet
     * @param stringCount
     * @param styleCount
     * @return
     */
    public static Res02StringPool parseResStringPool(byte[] arscArray, int offSet, int stringCount, int styleCount) {
        //解析ChunkHeader
        Res02StringPool res02StringPool = new Res02StringPool();
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
        res02StringPool.string = strings;
        //样式串
        startIndex = offSet + stringCount * 4;
        ArrayList<String> styles = new ArrayList<String>();
        index = 0;
        while (index < styleCount) {
            byte[] styleSizeByte = copyByte(arscArray, startIndex, 2);
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
        res02StringPool.style = styles;
        return res02StringPool;
    }


    /**
     * 解析资源Package块
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    public static Res03TablePackage parseResTablePackage(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        int startIndex = offSet;
        Res03TablePackage res03TablePackage = new Res03TablePackage();
        res03TablePackage.header = parseResChunkHeader(arscArray, offSet);

        //id
        startIndex = startIndex + res03TablePackage.header.getHeaderSize();
        byte[] idByte = copyByte(arscArray, startIndex, 4);
        res03TablePackage.id = Byte2ObjectUtil.byteArray2Int_Little_Endian(idByte);

        //name
        startIndex = startIndex + idByte.length;
        byte[] nameByte = copyByte(arscArray, startIndex, 128 * 2);
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(nameByte.length);
        bb.put(nameByte);
        bb.flip();
        res03TablePackage.name = cs.decode(bb).array();

        //typeStrings
        startIndex = startIndex + nameByte.length;
        byte[] typeStringsByte = copyByte(arscArray, startIndex, 4);
        res03TablePackage.typeStrings = Byte2ObjectUtil.byteArray2Int_Little_Endian(typeStringsByte);

        //lastPublicType
        startIndex = startIndex + typeStringsByte.length;
        byte[] lastPublicTypeByte = copyByte(arscArray, startIndex, 4);
        res03TablePackage.lastPublicType = Byte2ObjectUtil.byteArray2Int_Little_Endian(lastPublicTypeByte);

        //keyStrings
        startIndex = startIndex + lastPublicTypeByte.length;
        byte[] keyStringsByte = copyByte(arscArray, startIndex, 4);
        res03TablePackage.keyStrings = Byte2ObjectUtil.byteArray2Int_Little_Endian(keyStringsByte);

        //lastPublicKey
        startIndex = startIndex + keyStringsByte.length;
        byte[] lastPublicKeyByte = copyByte(arscArray, startIndex, 4);
        res03TablePackage.lastPublicKey = Byte2ObjectUtil.byteArray2Int_Little_Endian(lastPublicKeyByte);

        return res03TablePackage;
    }
}

























