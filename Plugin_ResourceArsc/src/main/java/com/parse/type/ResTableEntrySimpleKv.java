package com.parse.type;


/**
 * @author syl
 * @time 2022/4/28 10:39
 */
public class ResTableEntrySimpleKv extends ResTableEntryKv {

    /**
     * 简单类型的资源实体的 value
     */
    public ResValue value;

    public ResTableEntrySimpleKv(ResTableEntry entry, ResValue value) {
        super(entry);
        this.value = value;
    }

    @Override
    public String toString() {
        return "ResTableEntrySimpleKv{" +
                "value=" + value +
                '}';
    }
}
