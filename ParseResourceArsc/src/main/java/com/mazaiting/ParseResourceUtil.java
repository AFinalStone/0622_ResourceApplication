package com.mazaiting;

import com.mazaiting.type.Res00ChunkHeader;
import com.mazaiting.type.Res01TableHeader;
import com.mazaiting.type.Res02StringPoolHeader;
import com.mazaiting.type.ResStringPoolRef;
import com.mazaiting.type.ResTableConfig;
import com.mazaiting.type.ResTableEntry;
import com.mazaiting.type.ResTableMap;
import com.mazaiting.type.ResTableMapEntry;
import com.mazaiting.type.ResTablePackage;
import com.mazaiting.type.ResTableRef;
import com.mazaiting.type.ResTableType;
import com.mazaiting.type.ResTableTypeSpec;
import com.mazaiting.type.ResValue;

import java.util.ArrayList;

public class ParseResourceUtil {
    /**
     * 字符串的偏移值
     */
    private static int resStringPoolChunkOffset;
    /**
     * 包内容的偏移值
     */
    private static int packageChunkOffset;
    /**
     * key字符串池的偏移值
     */
    private static int keyStringPoolChunkOffset;
    /**
     * 类型字符串池的偏移值
     */
    private static int typeStringPoolChunkOffset;
    /**
     * 解析资源的类型的偏移值
     */
    private static int resTypeOffset;
    /**
     * 资源包的ID和类型ID
     */
    private static int packId;
    private static int resTypeId;

    /**
     * 所有的字符串池
     */
    private static ArrayList<String> resStringList = new ArrayList<>();
    /**
     * 所有的资源key的值的池
     */
    private static ArrayList<String> keyStringList = new ArrayList<>();
    /**
     * 所有的类型的值的池
     */
    private static ArrayList<String> typeStringList = new ArrayList<>();

    /**
     * 解析头部信息
     *
     * @param arscArray arsc二进制数据
     */
    public static Res01TableHeader parseResTableHeaderChunk(byte[] arscArray) {
        Res01TableHeader resTableHeader = new Res01TableHeader();
        resTableHeader.header = parseResChunkHeader(arscArray, 0);
        resStringPoolChunkOffset = resTableHeader.header.headerSize;
        // 解析PackageCount个数（一个apk可能包含多个Package资源）
        byte[] packageCountByte = Util.copyByte(arscArray, resTableHeader.header.getHeaderSize(), 4);
        resTableHeader.packageCount = Util.byte2int(packageCountByte);
        return resTableHeader;
    }


    /**
     * 解析Resource.arsc文件中所有字符串内容
     *
     * @param arscArray 二进制数据
     */
    public static Res02StringPoolHeader parseResStringPoolChunk(byte[] arscArray) {
        Res02StringPoolHeader stringPoolHeader = parseStringPoolChunk(arscArray, resStringList, resStringPoolChunkOffset);
        packageChunkOffset = resStringPoolChunkOffset + stringPoolHeader.header.size;
        return stringPoolHeader;
    }

    /**
     * 解析包信息
     *
     * @param arscArray arsc二进制数据
     */
    public static void parsePackage(byte[] arscArray) {
        ResTablePackage resTablePackage = new ResTablePackage();

        // 解析头部信息
        resTablePackage.header = parseResChunkHeader(arscArray, packageChunkOffset);

        int offset = packageChunkOffset + resTablePackage.header.getHeaderSize();

        // 解析package ID
        byte[] idByte = Util.copyByte(arscArray, offset, 4);
        resTablePackage.id = Util.byte2int(idByte);
        packId = resTablePackage.id;

        // 解析包名
        // 这里的128是这个字段的大小，可以查看类型说明，是char类型的，所以要乘以2
        byte[] nameByte = Util.copyByte(arscArray, offset + 4, 128 * 2);
        String packageName = new String(nameByte);
        packageName = Util.filterStringNull(packageName);
//		System.out.println("packageName: " + packageName);

        // 解析类型字符串的偏移值
        byte[] typeStringByte = Util.copyByte(arscArray, offset + 4 + 128 * 2, 4);
        resTablePackage.typeStrings = Util.byte2int(typeStringByte);

        // 解析lastPublicType字段
        byte[] lastPublicType = Util.copyByte(arscArray, offset + 8 + 128 * 2, 4);
        resTablePackage.lastPublicType = Util.byte2int(lastPublicType);

        // 解析keyString字符串的偏移值
        byte[] keyStrings = Util.copyByte(arscArray, offset + 12 + 128 * 2, 4);
        resTablePackage.keyStrings = Util.byte2int(keyStrings);

        // 解析lastPublicKey
        byte[] lastPublicKey = Util.copyByte(arscArray, offset + 16 + 128 * 2, 4);
        resTablePackage.lastPublicKey = Util.byte2int(lastPublicKey);

        // 这里获取类型字符串的偏移值和类型字符串的偏移值
        keyStringPoolChunkOffset = packageChunkOffset + resTablePackage.keyStrings;
        typeStringPoolChunkOffset = packageChunkOffset + resTablePackage.typeStrings;

//		System.out.println(resTablePackage.toString());
    }

    /**
     * 解析资源类型的字符串内容
     *
     * @param arscArray 二进制数据
     */
    public static void parseTypeStringPoolChunk(byte[] arscArray) {
        Res02StringPoolHeader resStringPoolHeader = parseStringPoolChunk(arscArray, typeStringList, typeStringPoolChunkOffset);
//		System.out.println(resStringPoolHeader.toString());
    }

    /**
     * 解析资源字字符串内容
     *
     * @param arscArray 二进制数据
     */
    public static void parseKeyStringPoolChunk(byte[] arscArray) {
        Res02StringPoolHeader stringPoolHeader = parseStringPoolChunk(arscArray, keyStringList, keyStringPoolChunkOffset);
        // 解析key字符串之后，需要赋值给resType的偏移值，后续还需继续解析
        resTypeOffset = keyStringPoolChunkOffset + stringPoolHeader.header.size;
    }

    /**
     * 解析ResTypeSpec类型描述内容
     *
     * @param arscArray 二进制数据
     */
    public static void parseResTypeSpec(byte[] arscArray) {
        ResTableTypeSpec typeSpec = new ResTableTypeSpec();
        // 解析头部信息
        typeSpec.header = parseResChunkHeader(arscArray, resTypeOffset);

        int offset = resTypeOffset + typeSpec.header.getHeaderSize();

        // 解析id类型
        byte[] idByte = Util.copyByte(arscArray, offset, 1);
        typeSpec.id = (byte) (idByte[0] & 0xFF);
        resTypeId = typeSpec.id;

        // 解析res0字段，这个字段是备用的，始终是0
        byte[] res0Byte = Util.copyByte(arscArray, offset + 1, 1);
        typeSpec.res0 = (byte) (res0Byte[0] & 0xFF);

        // 解析res1字段，这个字段是备用的，始终是0
        byte[] res1Byte = Util.copyByte(arscArray, offset + 2, 2);
        typeSpec.res1 = Util.byte2Short(res1Byte);

        byte[] entryCountByte = Util.copyByte(arscArray, offset + 4, 4);
        typeSpec.entryCount = Util.byte2int(entryCountByte);

        // 获取EntryCount个int数组
        int[] intAry = new int[typeSpec.entryCount];
        int intAryOffset = resTypeOffset + typeSpec.header.headerSize;
        for (int i = 0; i < typeSpec.entryCount; i++) {
            int element = Util.byte2int(Util.copyByte(arscArray, intAryOffset + i * 4, 4));
            intAry[i] = element;
        }

        resTypeOffset += typeSpec.header.size;
    }

    /**
     * 解析ResType
     *
     * @param arscArray 二进制数据
     */
    public static void parseResTypeInfo(byte[] arscArray) {
        ResTableType type = new ResTableType();
        // 解析头部信息
        type.header = parseResChunkHeader(arscArray, resTypeOffset);

        int offset = resTypeOffset + type.header.getHeaderSize();

        // 解析type的id值
        byte[] idByte = Util.copyByte(arscArray, offset, 1);
        type.id = (byte) (idByte[0] & 0xFF);

        // 解析res0字段的值，备用字段，始终是0
        byte[] res0 = Util.copyByte(arscArray, offset + 1, 1);
        type.res0 = (byte) (res0[0] & 0xFF);

        // 解析res1字段的值，备用字段，始终是0
        byte[] res1 = Util.copyByte(arscArray, offset + 2, 2);
        type.res1 = Util.byte2Short(res1);

        byte[] entryCountByte = Util.copyByte(arscArray, offset + 4, 4);
        type.entryCount = Util.byte2int(entryCountByte);

        byte[] entriesStartByte = Util.copyByte(arscArray, offset + 8, 4);
        type.entriesStart = Util.byte2int(entriesStartByte);

        ResTableConfig resConfig = new ResTableConfig();
        resConfig = parseResTableConfig(Util.copyByte(arscArray, offset + 12, resConfig.getSize()));

//		System.out.println("config:"+resConfig);		 
//		System.out.println("res type info:"+type);	 
//		System.out.println("type_name:"+typeStringList.get(type.id-1));

        // 获取entryCount个int数组
//		System.out.print("type int elements:");
        int[] intAry = new int[type.entryCount];
        for (int i = 0; i < type.entryCount; i++) {
            int element = Util.byte2int(Util.copyByte(arscArray, resTypeOffset + type.header.headerSize + i * 4, 4));
            intAry[i] = element;
//			System.out.print(element+",");
        }
//		System.out.println();

        // 这里开始解析后面对应的ResEntry和ResValue
        int entryAryOffset = resTypeOffset + type.entriesStart;
        ResTableEntry[] tableEntries = new ResTableEntry[type.entryCount];
        ResValue[] resValues = new ResValue[type.entryCount];
//		System.out.println("entry offset:"+Util.bytesToHexString(Util.int2Byte(entryAryOffset)));

        // 如果是ResMapEntry的话，偏移值不一样
        int bodySize = 0, valueOffset = entryAryOffset;
        for (int i = 0; i < type.entryCount; i++) {
            int resId = getResId(i);
//			System.out.println("resId:"+Util.bytesToHexString(Util.int2Byte(resId)));
            ResTableEntry entry = new ResTableEntry();
            ResValue value = new ResValue();
            valueOffset += bodySize;
//			System.out.println("valueOffset:"+Util.bytesToHexString(Util.int2Byte(valueOffset)));
            entry = parseResEntry(Util.copyByte(arscArray, valueOffset, entry.getSize()));

            // 判断entry的flag变量是否为1，如果为1的话，那就是ResTable_map_entry
            if (entry.flags == 1) {
                // 复杂类型的value
                ResTableMapEntry mapEntry = new ResTableMapEntry();
                mapEntry = parseResMapEntry(Util.copyByte(arscArray, valueOffset, mapEntry.getSize()));
//				System.out.println("map entry:"+mapEntry);
                ResTableMap resMap = new ResTableMap();
                for (int j = 0; j < mapEntry.count; j++) {
                    int mapOffset = valueOffset + mapEntry.getSize() + resMap.getSize() * j;
                    resMap = parseResTableMap(Util.copyByte(arscArray, mapOffset, resMap.getSize()));
//					System.out.println("map:" + resMap);
                }
                bodySize = mapEntry.getSize() + resMap.getSize() * mapEntry.count;
            } else {
//				System.out.println("entry: " + entry);
                // 简单的类型value
                value = parseResValue(Util.copyByte(arscArray, valueOffset + entry.getSize(), value.getSize()));
//				System.out.println("value: " + value);
                bodySize = entry.getSize() + value.getSize();
            }

            tableEntries[i] = entry;
            resValues[i] = value;
//			System.out.println("============================");
        }
        resTypeOffset += type.header.size;
    }

    /**
     * 解析ResTableMap内容
     *
     * @param srcByte 二进制数据
     * @return
     */
    private static ResTableMap parseResTableMap(byte[] srcByte) {
        ResTableMap tableMap = new ResTableMap();

        ResTableRef ref = new ResTableRef();
        byte[] identByte = Util.copyByte(srcByte, 0, ref.getSize());
        if (null != identByte) {
            ref.ident = Util.byte2int(identByte);
            tableMap.name = ref;
        }

        ResValue value = new ResValue();
        value = parseResValue(Util.copyByte(srcByte, ref.getSize(), value.getSize()));
        tableMap.value = value;

        return tableMap;
    }

    /**
     * 解析ResValue内容
     *
     * @param srcByte 二进制数据
     * @return
     */
    private static ResValue parseResValue(byte[] srcByte) {
        ResValue value = new ResValue();
        byte[] sizeByte = Util.copyByte(srcByte, 0, 2);
        if (null != sizeByte) {
            value.size = Util.byte2Short(sizeByte);
        }

        byte[] res0Byte = Util.copyByte(srcByte, 2, 1);
        if (null != res0Byte) {
            value.res0 = (byte) (res0Byte[0] & 0xFF);
        }

        byte[] dataType = Util.copyByte(srcByte, 3, 1);
        if (null != dataType) {
            value.dataType = (byte) (dataType[0] & 0xFF);
        }

        byte[] data = Util.copyByte(srcByte, 4, 4);
        if (null != data) {
            value.data = Util.byte2int(data);
        }

        return value;
    }

    /**
     * 解析ResMapEntry内容
     *
     * @param srcByte 二进制数据
     * @return
     */
    private static ResTableMapEntry parseResMapEntry(byte[] srcByte) {
        ResTableMapEntry entry = new ResTableMapEntry();
        byte[] sizeByte = Util.copyByte(srcByte, 0, 2);
        entry.size = Util.byte2Short(sizeByte);

        byte[] flagByte = Util.copyByte(srcByte, 2, 2);
        entry.flags = Util.byte2Short(flagByte);

        ResStringPoolRef key = new ResStringPoolRef();
        byte[] keyByte = Util.copyByte(srcByte, 4, 4);
        key.index = Util.byte2int(keyByte);
        entry.key = key;

        ResTableRef ref = new ResTableRef();
        byte[] identByte = Util.copyByte(srcByte, 8, 4);
        ref.ident = Util.byte2int(identByte);
        entry.parent = ref;
        byte[] countByte = Util.copyByte(srcByte, 12, 4);
        entry.count = Util.byte2int(countByte);

        return entry;
    }

    /**
     * 解析ResEntry内容
     *
     * @param srcByte 二进制数据
     * @return
     */
    private static ResTableEntry parseResEntry(byte[] srcByte) {
        ResTableEntry entry = new ResTableEntry();
        byte[] sizeByte = Util.copyByte(srcByte, 0, 2);
        if (null != sizeByte) {
            entry.size = Util.byte2Short(sizeByte);
        }

        byte[] flagByte = Util.copyByte(srcByte, 2, 2);
        if (null != flagByte) {
            entry.flags = Util.byte2Short(flagByte);
        }

        ResStringPoolRef key = new ResStringPoolRef();
        byte[] keyByte = Util.copyByte(srcByte, 4, 4);
        if (null != keyByte) {
            key.index = Util.byte2int(keyByte);
        }
        entry.key = key;

        return entry;
    }


    /**
     * 获取资源id
     * 这里高位是packid，中位是restypeid，地位是entryid
     *
     * @param entryid
     * @return
     */
    public static int getResId(int entryid) {
        return (((packId) << 24) | (((resTypeId) & 0xFF) << 16) | (entryid & 0xFFFF));
    }

    /**
     * 解析ResTablConfig配置信息
     *
     * @param srcByte 二进制数据
     * @return
     */
    private static ResTableConfig parseResTableConfig(byte[] srcByte) {
        ResTableConfig config = new ResTableConfig();

        byte[] sizeByte = Util.copyByte(srcByte, 0, 4);
        config.size = Util.byte2int(sizeByte);

        // 以下结构是Union
        byte[] mccByte = Util.copyByte(srcByte, 4, 2);
        config.mcc = Util.byte2Short(mccByte);
        byte[] mncByte = Util.copyByte(srcByte, 6, 4);
        config.mnc = Util.byte2Short(mncByte);
        byte[] imsiByte = Util.copyByte(srcByte, 4, 4);
        config.imsi = Util.byte2int(imsiByte);

        // 以下结构是Union
        byte[] languageByte = Util.copyByte(srcByte, 8, 2);
        config.language = languageByte;
        byte[] countryByte = Util.copyByte(srcByte, 10, 2);
        config.country = countryByte;
        byte[] localeByte = Util.copyByte(srcByte, 8, 4);
        config.locale = Util.byte2int(localeByte);

        // 以下结构是Union
        byte[] orientationByte = Util.copyByte(srcByte, 12, 1);
        config.orientation = orientationByte[0];
        byte[] touchscreenByte = Util.copyByte(srcByte, 13, 1);
        config.touchscreen = touchscreenByte[0];
        byte[] densityByte = Util.copyByte(srcByte, 14, 2);
        config.density = Util.byte2Short(densityByte);
        byte[] screenTypeByte = Util.copyByte(srcByte, 12, 4);
        config.screenType = Util.byte2int(screenTypeByte);

        // 以下结构是Union
        byte[] keyboardByte = Util.copyByte(srcByte, 16, 1);
        config.keyboard = keyboardByte[0];
        byte[] navigationByte = Util.copyByte(srcByte, 17, 1);
        config.navigation = navigationByte[0];
        byte[] inputFlagsByte = Util.copyByte(srcByte, 18, 1);
        config.inputFlags = inputFlagsByte[0];
        byte[] inputPad0Byte = Util.copyByte(srcByte, 19, 1);
        config.inputPad0 = inputPad0Byte[0];
        byte[] inputByte = Util.copyByte(srcByte, 16, 4);
        config.input = Util.byte2int(inputByte);

        // 以下结构是Union
        byte[] screenWidthByte = Util.copyByte(srcByte, 20, 2);
        config.screenWidth = Util.byte2Short(screenWidthByte);
        byte[] screenHeightByte = Util.copyByte(srcByte, 22, 2);
        config.screenHeight = Util.byte2Short(screenHeightByte);
        byte[] screenSizeByte = Util.copyByte(srcByte, 20, 4);
        config.screenSize = Util.byte2int(screenSizeByte);

        // 以下结构是Union
        byte[] sdVersionByte = Util.copyByte(srcByte, 24, 2);
        config.sdkVersion = Util.byte2Short(sdVersionByte);
        byte[] minorVersionByte = Util.copyByte(srcByte, 26, 2);
        config.minorVersion = Util.byte2Short(minorVersionByte);
        byte[] versionByte = Util.copyByte(srcByte, 24, 4);
        config.version = Util.byte2int(versionByte);

        // 以下结构是Union
        byte[] screenLayoutByte = Util.copyByte(srcByte, 28, 1);
        config.screenLayout = screenLayoutByte[0];
        byte[] uiModeByte = Util.copyByte(srcByte, 29, 1);
        config.uiMode = uiModeByte[0];
        byte[] smallestScreenWidthDpByte = Util.copyByte(srcByte, 30, 2);
        config.smallestScreenWidthDp = Util.byte2Short(smallestScreenWidthDpByte);
        byte[] screenConfigByte = Util.copyByte(srcByte, 28, 4);
        config.screenConfig = Util.byte2int(screenConfigByte);

        // 以下结构是Union
        byte[] screenWidthDpByte = Util.copyByte(srcByte, 32, 2);
        config.screenWidthDp = Util.byte2Short(screenWidthDpByte);
        byte[] screenHeightDpByte = Util.copyByte(srcByte, 34, 2);
        config.screenHeightDp = Util.byte2Short(screenHeightDpByte);
        byte[] screenSizeDpByte = Util.copyByte(srcByte, 32, 4);
        config.screenSizeDp = Util.byte2int(screenSizeDpByte);

        byte[] localeScriptByte = Util.copyByte(srcByte, 36, 4);
        config.localeScript = localeScriptByte;

        byte[] localeVariantByte = Util.copyByte(srcByte, 40, 8);
        config.localeVariant = localeVariantByte;

        return config;
    }

    /**
     * 解析资源头部信息
     * 所有的Chunk公共的头部信息
     *
     * @param arscArray 数组
     * @param start     开始位置
     * @return
     */
    private static Res00ChunkHeader parseResChunkHeader(byte[] arscArray, int start) {
        Res00ChunkHeader header = new Res00ChunkHeader();

        // 解析头部类型
        byte[] typeByte = Util.copyByte(arscArray, start, 2);
        header.type = Util.byte2Short(typeByte);

        // 解析头部大小
        byte[] headerSizeByte = Util.copyByte(arscArray, start + 2, 2);
        header.headerSize = Util.byte2Short(headerSizeByte);

        // 解析整个Chunk的大小
        byte[] tableSizeByte = Util.copyByte(arscArray, start + 4, 4);
        header.size = Util.byte2int(tableSizeByte);

        return header;
    }

    /**
     * 同一解析字符串内容
     *
     * @param arscArray    二进制数组
     * @param stringList   字符串列表
     * @param stringOffset 字符串偏移值
     * @return
     */
    private static Res02StringPoolHeader parseStringPoolChunk(byte[] arscArray, ArrayList<String> stringList, int stringOffset) {
        Res02StringPoolHeader stringPoolHeader = new Res02StringPoolHeader();
        // 解析头部信息
        stringPoolHeader.header = parseResChunkHeader(arscArray, stringOffset);

        int offset = stringOffset + stringPoolHeader.header.getHeaderSize();

        // 获取字符串的个数
        byte[] stringCountByte = Util.copyByte(arscArray, offset, 4);
        stringPoolHeader.stringCount = Util.byte2int(stringCountByte);

        // 解析样式的个数
        byte[] styleCountByte = Util.copyByte(arscArray, offset + 4, 4);
        stringPoolHeader.styleCount = Util.byte2int(styleCountByte);

        // 这里表示字符串的格式：UTF-8/UTF-16
        byte[] flagByte = Util.copyByte(arscArray, offset + 8, 4);
        stringPoolHeader.flags = Util.byte2int(flagByte);

        // 从字符串内容的开始位置
        byte[] stringStartByte = Util.copyByte(arscArray, offset + 12, 4);
        stringPoolHeader.stringsStart = Util.byte2int(stringStartByte);

        // 样式内容开始的位置
        byte[] styleStartByte = Util.copyByte(arscArray, offset + 16, 4);
        stringPoolHeader.stylesStart = Util.byte2int(styleStartByte);

        // 获取字符串内容的索引数组和样式内容的索引数组
        int[] stringIndexAry = new int[stringPoolHeader.stringCount];
        int[] styleIndexAry = new int[stringPoolHeader.styleCount];

        int stringIndex = offset + 20;
        for (int i = 0; i < stringIndexAry.length; i++) {
            stringIndexAry[i] = Util.byte2int(Util.copyByte(arscArray, stringIndex + i * 4, 4));
        }

        int styleIndex = stringIndex + 4 * styleIndexAry.length;
        for (int i = 0; i < styleIndexAry.length; i++) {
            styleIndexAry[i] = Util.byte2int(Util.copyByte(arscArray, styleIndex + i * 4, 4));
        }

        // 每个字符串的头两个字节的最后一个字节是字符串的长度
        int stringContentIndex = styleIndex + stringPoolHeader.styleCount * 4;
        int index = 0;
        while (index < stringPoolHeader.stringCount) {
            byte[] stringSizeByte = Util.copyByte(arscArray, stringContentIndex, 2);
            int stringSize = (stringSizeByte[1] & 0x7F);
            if (0 != stringSize) {
                String val = "";
                try {
                    val = new String(Util.copyByte(arscArray, stringContentIndex + 2, stringSize), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stringList.add(val);
            } else {
                stringList.add("");
            }
            stringContentIndex += (stringSize + 3);
            index++;
        }

        for (String str : stringList) {
            System.out.println("str: " + str);
        }

        return stringPoolHeader;
    }

    /**
     * 判断是否到文件结尾
     *
     * @param length 长度
     * @return
     */
    public static boolean isEnd(int length) {
        return resTypeOffset >= length ? true : false;
    }

    /**
     * 判断是不是类型描述符
     *
     * @param arscArray 二进制数据
     * @return
     */
    public static boolean isTypeSpec(byte[] arscArray) {
        Res00ChunkHeader header = parseResChunkHeader(arscArray, resTypeOffset);

        return header.type == 0x0202 ? true : false;
    }

    public static String getKeyString(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public static String getResString(int data) {
        // TODO Auto-generated method stub
        return null;
    }

}