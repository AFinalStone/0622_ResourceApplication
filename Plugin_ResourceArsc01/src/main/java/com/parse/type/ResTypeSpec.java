package com.parse.type;

import com.parse.util.ByteUtils;
import com.parse.util.DebugUtils;
import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

import java.util.Arrays;

/**
 * @author syl
 * @time 2022/4/28 10:40
 */
public class ResTypeSpec implements IObjToBytes {

    /**
     * ResTypeSpec chunk header
     */
    public ResTableTypeSpecHeader header;
    /**
     * 同名资源在哪些维度上具有差异
     * flag = array[index] //index 为某个名字的资源的索引位置
     * flag & XX_FLAG, flag & YY_FLAG 表示在哪些类型的配置有差异（语言，地区，屏幕分辨率等
     */
    public int array[];

    public static ResTypeSpec parseResTypeSpec(ResTablePackage pkg, byte[] src, int resTypeOffset) {

        ResTypeSpec typeSpec = new ResTypeSpec();

        ResTableTypeSpecHeader typeSpecHeader = new ResTableTypeSpecHeader();

        //解析chunk头部信息
        typeSpecHeader.header = ResChunkHeader.parseResChunkHeader(src, resTypeOffset);

        DebugUtils.assertTrue(typeSpecHeader.header.type == ResChunkHeader.ChunkType.RES_TABLE_TYPE_SPEC_TYPE);
        typeSpec.header = typeSpecHeader;
        //typeSpec header 信息
        int offset = (resTypeOffset + typeSpecHeader.header.getResChunkHeaderSize());

        //解析id类型
        byte[] idByte = ByteUtils.copyByte(src, offset, 1);
        typeSpecHeader.id = (byte) (idByte[0] & 0xFF);

        //解析res0字段,这个字段是备用的，始终是0
        byte[] res0Byte = ByteUtils.copyByte(src, offset + 1, 1);
        typeSpecHeader.res0 = (byte) (res0Byte[0] & 0xFF);

        //解析res1字段，这个字段是备用的，始终是0
        byte[] res1Byte = ByteUtils.copyByte(src, offset + 2, 2);
        typeSpecHeader.res1 = ByteUtils.byte2Short(res1Byte);

        //entry的总个数
        byte[] entryCountByte = ByteUtils.copyByte(src, offset + 4, 4);
        typeSpecHeader.entryCount = ByteUtils.byte2int(entryCountByte);

        //获取entryCount个int数组
        int[] intAry = new int[typeSpecHeader.entryCount];
        int intAryOffset = resTypeOffset + typeSpecHeader.header.headerSize;
        for (int i = 0; i < typeSpecHeader.entryCount; i++) {
            int element = ByteUtils.byte2int(ByteUtils.copyByte(src, intAryOffset + i * 4, 4));
            intAry[i] = element;
        }

        typeSpec.array = intAry;

        return typeSpec;
    }

    @Override
    public String toString() {
        return "ResTypeSpec{" +
                "header=" + header +
                ", array=" + Arrays.toString(array) +
                '}';
    }

    @Override
    public byte[] toBytes() {
        return Object2ByteUtil.object2ByteArray_Little_Endian(new Object[]{header, array});
    }
}
