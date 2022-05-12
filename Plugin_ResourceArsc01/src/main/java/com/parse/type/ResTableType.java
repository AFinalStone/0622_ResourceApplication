package com.parse.type;

import com.parse.util.ByteUtils;
import com.parse.util.DebugUtils;
import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 一种资源类型下具有相同资源配置的资源集合
 * 例如 string-hdpi.xml, string-zh.xml,string-xxhdpi-cn.xml 对应不同的  ResTableType
 *
 * @author syl
 * @time 2022/4/28 10:40
 */
public class ResTableType implements IObjToBytes {

    /**
     * chunk header
     */
    public ResTableTypeHeader header;
    /**
     * 每个资源实体所在的偏移或者 0xFFFFFFFF （不存在该资源实体，占位）
     */
    public int[] entryArray;

    /**
     * 对应的所有资源实体
     */
    public List<ResTableEntryKv> resKvList;

    /**
     * debug
     */
    public ResTablePackage __pkg;

    public static ResTableType parseResTypeInfo(ResTablePackage pkg, byte[] src, int resTypeOffset) {

        ResTableType type = new ResTableType();
        ResTableTypeHeader typeHeader = new ResTableTypeHeader();
        type.__pkg = pkg;

        //解析头部信息
        typeHeader.header = ResChunkHeader.parseResChunkHeader(src, resTypeOffset);
        DebugUtils.assertTrue(typeHeader.header.type == ResChunkHeader.ChunkType.RES_TABLE_TYPE_TYPE);
        type.header = typeHeader;
        //resType 头部信息
        int offset = (resTypeOffset + typeHeader.header.getResChunkHeaderSize());

        //解析type的id值
        byte[] idByte = ByteUtils.copyByte(src, offset, 1);
        typeHeader.id = (byte) (idByte[0] & 0xFF);

        //解析res0字段的值，备用字段，始终是0
        byte[] res0 = ByteUtils.copyByte(src, offset + 1, 1);
        typeHeader.res0 = (byte) (res0[0] & 0xFF);

        //解析res1字段的值，备用字段，始终是0
        byte[] res1 = ByteUtils.copyByte(src, offset + 2, 2);
        typeHeader.res1 = ByteUtils.byte2Short(res1);

        byte[] entryCountByte = ByteUtils.copyByte(src, offset + 4, 4);
        typeHeader.entryCount = ByteUtils.byte2int(entryCountByte);

        byte[] entriesStartByte = ByteUtils.copyByte(src, offset + 8, 4);
        typeHeader.entriesStart = ByteUtils.byte2int(entriesStartByte);

        ResTableConfig resConfig = new ResTableConfig();
        resConfig = ResTableConfig.parseResTableConfig(ByteUtils.copyByte(src, offset + 12, resConfig.getSize()));

        typeHeader.resConfig = resConfig;

        //索引数组
        int[] entryIndexArray = new int[typeHeader.entryCount];
        for (int i = 0; i < typeHeader.entryCount; i++) {
            int element = ByteUtils.byte2int(ByteUtils.copyByte(src, resTypeOffset + typeHeader.header.headerSize + i * 4, 4));
            entryIndexArray[i] = element;
        }
        type.entryArray = entryIndexArray;

        List<ResTableEntryKv> resKvs = new ArrayList<>();

        int entryStart = type.header.header.debug_offset + type.header.entriesStart;

        for (int i = 0; i < typeHeader.entryCount; i++) {
            if (entryIndexArray[i] == 0xFFFFFFFF) {
                ResTableEntry entry = new ResTableEntry(type);
                entry.__index = i;
                entry.__empty = true;
                continue;
            }
            int valueOffset = entryIndexArray[i] + entryStart;
            ResTableEntry entry = ResTableEntry.parseResEntry(type, ByteUtils.copyByte(src, valueOffset, ResTableEntry.getSize()));
            entry.__index = i;
            ResValue value = new ResValue();

            //这里需要注意的是，先判断entry的flag变量是否为1,如果为1的话，那就ResTable_map_entry
            if (entry.flags == ResTableMapEntry.FLAG_COMPLEX) {
                //这里是复杂类型的value
                ResTableMapEntry mapEntry = ResTableMapEntry.parseResMapEntry(type, ByteUtils.copyByte(src, valueOffset, ResTableMapEntry.getSize()));
                List<ResTableMap> list = new ArrayList<>();
                ResTableMap resMap = new ResTableMap();
                for (int j = 0; j < mapEntry.count; j++) {
                    int mapOffset = valueOffset + ResTableMapEntry.getSize() + resMap.getSize() * j;
                    resMap = ResTableMap.parseResTableMap(mapEntry, ByteUtils.copyByte(src, mapOffset, resMap.getSize()));
                    list.add(resMap);
                }
                ResTableMapEntryKv kv = new ResTableMapEntryKv(entry, list);
                resKvs.add(kv);
            } else {
                //这里是简单的类型的value
                value = ResValue.parseResValue(ByteUtils.copyByte(src, valueOffset + ResTableEntry.getSize(), value.getSize()));
                value.__entry = entry;
                ResTableEntrySimpleKv kv = new ResTableEntrySimpleKv(entry, value);
                resKvs.add(kv);
            }
        }
        type.resKvList = resKvs;
        return type;
    }

    @Override
    public String toString() {
        return "ResTableType{" +
                "header=" + header +
                ", entryArray=" + Arrays.toString(entryArray) +
                ", resKvList=" + resKvList +
                ", __pkg=" + __pkg +
                '}';
    }

    @Override
    public byte[] toBytes() {
        byte[] byteTop = Object2ByteUtil.object2ByteArray_Little_Endian(new Object[]{header, entryArray});
        byte[] byteResKvList = getResKvList();
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteTop.length + byteResKvList.length);
        byteBuffer.put(byteTop);
        byteBuffer.put(byteResKvList);
        byteBuffer.flip();
        return byteBuffer.array();
    }

    private byte[] getResKvList() {
        byte[] byteResTypes;
        int lenByteResTypes = 0;
        for (int i = 0; i < resKvList.size(); i++) {
            lenByteResTypes = lenByteResTypes + resKvList.get(i).toBytes().length;
        }
        byteResTypes = new byte[lenByteResTypes];
        int offset = 0;
        for (int i = 0; i < resKvList.size(); i++) {
            byte[] byteStyle = resKvList.get(i).toBytes();
            System.arraycopy(byteStyle, 0, byteResTypes, offset, byteStyle.length);
            offset = offset + byteStyle.length;
        }
        return byteResTypes;
    }
}
