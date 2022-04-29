package com.afs.resourcearsc.impl;

import com.afs.resourcearsc.bean.ResChunkHeader;
import com.afs.resourcearsc.bean.ResTableHeader;
import com.afs.resourcearsc.bean.ResTablePackageHeader;
import com.afs.resourcearsc.bean.ResTableStringPool;
import com.afs.resourcearsc.bean.ResTableStringPoolHeader;
import com.afs.resourcearsc.bean.ResTableTypeSpec;
import com.afs.resourcearsc.bean.ResTableTypeType;
import com.afs.resourcearsc.parse.IParseResource;
import com.afs.resourcearsc.utils.Byte2ObjectUtil;

import java.util.ArrayList;

public class ParseResourceImpl implements IParseResource {

    /**
     * 解析头部信息
     *
     * @param arscArray arsc二进制数据
     */
    @Override
    public ResTableHeader parseResTableHeaderChunk(byte[] arscArray, int offSet) {
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
    public ResTableStringPool parseResTableStringPool(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        int startIndex = offSet;
        ResTableStringPoolHeader resTableStringPoolHeader = new ResTableStringPoolHeader();
        resTableStringPoolHeader.header = parseResChunkHeader(arscArray, offSet);

        //stringCount
        startIndex = startIndex + resTableStringPoolHeader.header.getHeaderSize();
        byte[] stringCountByte = copyByte(arscArray, startIndex, 4);
        resTableStringPoolHeader.stringCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(stringCountByte);

        //styleCount
        startIndex = startIndex + stringCountByte.length;
        byte[] styleCountByte = copyByte(arscArray, startIndex, 4);
        resTableStringPoolHeader.styleCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(styleCountByte);

        //flags
        startIndex = startIndex + styleCountByte.length;
        byte[] flagsByte = copyByte(arscArray, startIndex, 4);
        resTableStringPoolHeader.flags = Byte2ObjectUtil.byteArray2Int_Little_Endian(flagsByte);

        //stringsStart
        startIndex = startIndex + flagsByte.length;
        byte[] stringsStartByte = copyByte(arscArray, startIndex, 4);
        resTableStringPoolHeader.stringsStart = Byte2ObjectUtil.byteArray2Int_Little_Endian(stringsStartByte);

        //stylesStart
        startIndex = startIndex + stringsStartByte.length;
        byte[] stylesStartByte = copyByte(arscArray, startIndex, 4);
        resTableStringPoolHeader.stylesStart = Byte2ObjectUtil.byteArray2Int_Little_Endian(stylesStartByte);

        //byteStringOffSet偏移数组字节
        startIndex = startIndex + stylesStartByte.length;
        int len = resTableStringPoolHeader.stringsStart - resTableStringPoolHeader.header.headerSize;
        resTableStringPoolHeader.byteStringOffSet = copyByte(arscArray, startIndex, len);

        ResTableStringPool resTableStringPool = null;
        if (ResTableStringPoolHeader.UTF8_FLAG == resTableStringPoolHeader.flags) {
            resTableStringPool = parseResTableStringPoolStrListByUTF_8(arscArray, offSet, resTableStringPoolHeader.stringsStart, resTableStringPoolHeader.stringCount);
        } else {
            resTableStringPool = parseResTableStringPoolStrListByUTF_16(arscArray, offSet, resTableStringPoolHeader.stringsStart, resTableStringPoolHeader.stringCount);
        }
        if (resTableStringPoolHeader.stylesStart != 0) {
            startIndex = offSet + resTableStringPoolHeader.stylesStart;
            int styLen = resTableStringPoolHeader.header.size - resTableStringPoolHeader.stylesStart;
            resTableStringPoolHeader.byteStringOffSet = copyByte(arscArray, startIndex, styLen);
        }
        resTableStringPool.stringPoolHeader = resTableStringPoolHeader;
        return resTableStringPool;
    }


    /**
     * 解析资源Package块
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    public ResTablePackageHeader parseResTablePackage(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        int startIndex = offSet;
        ResTablePackageHeader resTablePackage = new ResTablePackageHeader();
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


    @Override
    public ResTableTypeSpec parseResTableTypeSpec(byte[] arscArray, int offSet) {
        //解析ChunkHeader
        int startIndex = offSet;
        ResTableTypeSpec resTableTypeSpec = new ResTableTypeSpec();
        resTableTypeSpec.header = parseResChunkHeader(arscArray, offSet);

        //id和res0
        startIndex = startIndex + resTableTypeSpec.header.getHeaderSize();
        byte[] byteIds = copyByte(arscArray, startIndex, 2);
        resTableTypeSpec.id = (byte) (byteIds[0] & 0xFF);
        resTableTypeSpec.res0 = (byte) (byteIds[1] & 0xFF);

        //res1
        startIndex = startIndex + byteIds.length;
        byte[] byteRes1 = copyByte(arscArray, startIndex, 2);
        resTableTypeSpec.res1 = Byte2ObjectUtil.byteArray2Short_Little_Endian(byteRes1);

        //entryCount
        startIndex = startIndex + byteRes1.length;
        byte[] byteEntryCount = copyByte(arscArray, startIndex, 4);
        resTableTypeSpec.entryCount = Byte2ObjectUtil.byteArray2Int_Little_Endian(byteEntryCount);

        // 获取EntryCount个int数组
        int[] intAry = new int[resTableTypeSpec.entryCount];
        int intAryOffset = offSet + resTableTypeSpec.header.headerSize;
        for (int i = 0; i < resTableTypeSpec.entryCount; i++) {
            int element = Byte2ObjectUtil.byteArray2Int_Little_Endian(copyByte(arscArray, intAryOffset + i * 4, 4));
            intAry[i] = element;
        }
        resTableTypeSpec.entryArray = intAry;
        return resTableTypeSpec;
    }

    @Override
    public ResTableTypeType parseResTableTypeType(byte[] arscArray, int offSet) {
        return null;
    }


    /**
     * 把目标字节数组中的特定字节拷贝出来
     *
     * @param sourceBytes
     * @param start
     * @param length
     */
    private byte[] copyByte(byte[] sourceBytes, int start, int length) {
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
    private ResChunkHeader parseResChunkHeader(byte[] arscArray, int offSet) {
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
     * 解析字符串常量池中的字符串
     *
     * @param arscArray
     * @param stringsStart
     * @param stringCount
     * @return
     */
    private ResTableStringPool parseResTableStringPoolStrListByUTF_16(byte[] arscArray, int offSet, int stringsStart, int stringCount) {
        //解析ChunkHeader
        ResTableStringPool resTableStringPool = new ResTableStringPool();
        int startIndex = offSet + stringsStart;
        //字符串
        ArrayList<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < stringCount) {
            byte[] byteStringSize = copyByte(arscArray, startIndex, 2);
            int stringStringLen = Byte2ObjectUtil.byteArray2Int_Little_Endian(byteStringSize) * 2;
            if (0 != stringStringLen) {
                String val = "";
                try {
                    val = new String(copyByte(arscArray, startIndex + 2, stringStringLen), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                strings.add(val);
            } else {
                strings.add("");
            }
            startIndex += (stringStringLen + 4);
            index++;
        }
        return resTableStringPool;
    }

    /**
     * 解析字符串常量池中的字符串
     *
     * @param arscArray
     * @param stringsStart
     * @param stringCount
     * @return
     */
    private ResTableStringPool parseResTableStringPoolStrListByUTF_8(byte[] arscArray, int offSet, int stringsStart, int stringCount) {
        //解析ChunkHeader
        ResTableStringPool resTableStringPool = new ResTableStringPool();
        int startIndex = offSet + stringsStart;
        //字符串
        ArrayList<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < stringCount) {
            byte[] byteStringSize = copyByte(arscArray, startIndex, 2);
            int stringByteFlag = (byteStringSize[0] & 0xFF);
            int stringByteLen = (byteStringSize[1] & 0xFF);
            int tempLen = 0;
            if (stringByteFlag >= 128) {
                tempLen = 2;
                byteStringSize = copyByte(arscArray, startIndex, 2 + tempLen);
                stringByteLen = (byteStringSize[3] & 0xFF) | (byteStringSize[2] & 0x0F) << 8;
            }
            if (0 != stringByteLen) {
                String val = "";
                try {
                    byte[] byteTemp = copyByte(arscArray, startIndex + 2 + tempLen, stringByteLen);
                    val = new String(byteTemp, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                strings.add(val);
            } else {
                strings.add("");
            }
            startIndex += (stringByteLen + 3 + tempLen);
            index++;
        }
        resTableStringPool.stringList = strings;
        return resTableStringPool;
    }

}

























