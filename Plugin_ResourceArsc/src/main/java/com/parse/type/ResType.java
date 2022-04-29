package com.parse.type;

import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 一种资源类型对应的 spec 配置表和所有资源项
 *
 * @author syl
 * @time 2022/4/28 10:40
 */
public class ResType implements IObjToBytes {

    /**
     * 资源配置
     */
    public ResTypeSpec spec;
    /**
     * 所有配置下的资源项
     */
    public List<ResTableType> typeList;

    @Override
    public byte[] toBytes() {
        byte[] bytesResTypeSpec = spec.toBytes();
        byte[] byteStyleList = getByteStyleList();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytesResTypeSpec.length + byteStyleList.length);
        byteBuffer.put(bytesResTypeSpec);
        byteBuffer.put(byteStyleList);
        byteBuffer.flip();
        return byteBuffer.array();
    }

    private byte[] getByteStyleList() {
        byte[] byteStyles;
        int lenByteStyle = 0;
        for (int i = 0; i < typeList.size(); i++) {
            lenByteStyle = lenByteStyle + typeList.get(i).toBytes().length;
        }
        byteStyles = new byte[lenByteStyle];
        int offset = 0;
        for (int i = 0; i < typeList.size(); i++) {
            byte[] byteStyle = typeList.get(i).toBytes();
            System.arraycopy(byteStyle, 0, byteStyle, offset, byteStyle.length);
            offset = offset + byteStyle.length;
        }
        return byteStyles;
    }
}
