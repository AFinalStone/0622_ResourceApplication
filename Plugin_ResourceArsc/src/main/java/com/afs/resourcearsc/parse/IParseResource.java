package com.afs.resourcearsc.parse;

import com.afs.resourcearsc.bean.ResTableHeader;
import com.afs.resourcearsc.bean.ResTablePackageHeader;
import com.afs.resourcearsc.bean.ResTableStringPool;
import com.afs.resourcearsc.bean.ResTableTypeSpec;
import com.afs.resourcearsc.bean.ResTableTypeType;

public interface IParseResource {

//    ResTable parseResTable(byte[] arscArray);

    /**
     * 解析表头
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    ResTableHeader parseResTableHeaderChunk(byte[] arscArray, int offSet);

    /**
     * 解析字符串池
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    ResTableStringPool parseResTableStringPool(byte[] arscArray, int offSet);

    /**
     * 解析表ResTablePackageHeader
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    ResTablePackageHeader parseResTablePackage(byte[] arscArray, int offSet);

//    /**
//     * 解析ResTablePackage中的字符串池
//     *
//     * @param arscArray
//     * @param offSet
//     * @return
//     */
//    ResTablePackageStringPool parseResTablePackageStringPool(byte[] arscArray, int offSet);

    /**
     * 解析ResTableTypeSpec
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    ResTableTypeSpec parseResTableTypeSpec(byte[] arscArray, int offSet);

    /**
     * 解析ResTableTypeType
     *
     * @param arscArray
     * @param offSet
     * @return
     */
    ResTableTypeType parseResTableTypeType(byte[] arscArray, int offSet);
}
