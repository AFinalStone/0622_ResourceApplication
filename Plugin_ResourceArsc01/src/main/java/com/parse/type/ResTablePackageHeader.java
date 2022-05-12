package com.parse.type;

import com.parse.util.ByteUtils;
import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

import java.io.UnsupportedEncodingException;

/**
 * 资源包头
 *
 * @author syl
 * @time 2022/4/28 10:39
 */
public class ResTablePackageHeader implements IObjToBytes {

    public ResChunkHeader header;

    /**
     * 资源包id
     * If this is a base package, its ID.  Package IDs start
     * at 1 (corresponding to the value of the package bits in a
     * resource identifier).  0 means this is not a base package.
     */
    public int id;
    /**
     * package name
     * Actual name of this package, \0-terminated.
     */
//    public byte[] nameByte;
    public String name;

    /**
     * type 字符串池相对本chunk 的开始的偏移
     * Offset to a ResStringPool_header defining the resource
     * type symbol table.  If zero, this package is inheriting from
     * another base package (overriding specific values in it).
     */
    public int typeStrings;
    /**
     * Last index into typeStrings that is for public use by others.
     */
    public int lastPublicType;
    /**
     * key 字符串池相对本chunk 的开始的偏移
     * Offset to a ResStringPool_header defining the resource
     * key symbol table.  If zero, this package is inheriting from
     * another base package (overriding specific values in it).
     */
    public int keyStrings;
    /**
     * Last index into keyStrings that is for public use by others.
     */
    public int lastPublicKey;

    //????
    public int typeIdOffset;

    public ResTablePackageHeader() {

    }

    public static ResTablePackageHeader parsePackageHeader(byte[] src, int packageChunkOffset) {

        ResTablePackageHeader resTabPackageHeader = new ResTablePackageHeader();
        //解析头部信息
        resTabPackageHeader.header = ResChunkHeader.parseResChunkHeader(src, packageChunkOffset);

        //package 头部信息
        int offset = packageChunkOffset + resTabPackageHeader.header.getResChunkHeaderSize();

        //解析packId
        byte[] idByte = ByteUtils.copyByte(src, offset, 4);
        resTabPackageHeader.id = ByteUtils.byte2int(idByte);

        //解析包名
        resTabPackageHeader.name = new String(ByteUtils.copyByte(src, offset + 4, 128 * 2));//这里的128是这个字段的大小，可以查看类型说明，是char类型的，所以要乘以2

        //解析类型字符串的偏移值
        byte[] typeStringsByte = ByteUtils.copyByte(src, offset + 4 + 128 * 2, 4);
        resTabPackageHeader.typeStrings = ByteUtils.byte2int(typeStringsByte);

        //解析lastPublicType字段
        byte[] lastPublicType = ByteUtils.copyByte(src, offset + 8 + 128 * 2, 4);
        resTabPackageHeader.lastPublicType = ByteUtils.byte2int(lastPublicType);

        //解析keyString字符串的偏移值
        byte[] keyStrings = ByteUtils.copyByte(src, offset + 12 + 128 * 2, 4);
        resTabPackageHeader.keyStrings = ByteUtils.byte2int(keyStrings);

        //解析lastPublicKey
        byte[] lastPublicKey = ByteUtils.copyByte(src, offset + 16 + 128 * 2, 4);
        resTabPackageHeader.lastPublicKey = ByteUtils.byte2int(lastPublicKey);

        return resTabPackageHeader;
    }

    public String __debugPkgName() {
        String packageName = new String(name);
        return ByteUtils.filterStringNull(packageName);
    }

    @Override
    public String toString() {
        return "ResTablePackageHeader{" +
                "header=" + header +
                ", id=" + id +
                ", nameByte=" + name +
                ", typeStrings=" + typeStrings +
                ", lastPublicType=" + lastPublicType +
                ", keyStrings=" + keyStrings +
                ", lastPublicKey=" + lastPublicKey +
                ", typeIdOffset=" + typeIdOffset +
                '}';
    }

    @Override
    public byte[] toBytes() {
        byte[] byteStrName = new byte[256];
        try {
            byteStrName = ((String) name).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Object2ByteUtil.object2ByteArray_Little_Endian(new Object[]{
                header, id, byteStrName, typeStrings, lastPublicType, keyStrings, lastPublicKey, typeIdOffset
        });
    }
}
