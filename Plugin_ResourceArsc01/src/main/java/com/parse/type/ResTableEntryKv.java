package com.parse.type;

import com.parse.util.IObjToBytes;

/**
 * @author syl
 * @time 2022/4/28 10:39
 */
public abstract class ResTableEntryKv implements IObjToBytes {

    /**
     * 资源型的key
     */
    public ResTableEntry entry;

    public ResTableEntryKv(ResTableEntry entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "ResTableEntryKv{" +
                "entry=" + entry +
                '}';
    }


    @Override
    public byte[] toBytes() {
        return entry.toBytes();
    }
}
