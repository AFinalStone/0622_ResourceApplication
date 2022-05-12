package com.parse.type;

import com.parse.util.IObjToBytes;
import com.parse.util.Object2ByteUtil;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author syl
 * @time 2022/4/28 10:38
 */
public class ResStringPoolSpanList implements IObjToBytes {
    /**
     * 一个 style 对应的 string span
     */
    public List<ResStringPoolSpan> spans;

    @Override
    public byte[] toBytes() {
        int len = 0;
        for (int i = 0; i < spans.size(); i++) {
            len = len + spans.get(i).toBytes().length;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        for (int i = 0; i < spans.size(); i++) {
            byteBuffer.put(spans.get(i).toBytes());
        }
        byteBuffer.flip();
        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return "ResStringPoolSpanList{" +
                "spans=" + spans +
                '}';
    }
}
