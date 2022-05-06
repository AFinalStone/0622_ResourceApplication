package com.parse.type;

import java.util.List;

/**
 * @author syl
 * @time 2022/4/28 10:39
 */
public class ResTableMapEntryKv extends ResTableEntryKv {

    /**
     * 复杂类型的资源实体，对应多个
     *
     * @see ResTableMap
     */
    public List<ResTableMap> listMap;

    public ResTableMapEntryKv(ResTableEntry entry, List<ResTableMap> listMap) {
        super(entry);
        this.listMap = listMap;
    }

    @Override
    public String toString() {
        return "ResTableMapEntryKv{" +
                "listMap=" + listMap +
                '}';
    }
}
