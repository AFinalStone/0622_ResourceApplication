package com.afs.resourcearsc.parse;

import com.afs.resourcearsc.bean.ResTableHeader;
import com.afs.resourcearsc.bean.ResTablePackageHeader;
import com.afs.resourcearsc.bean.ResTableStringPool;
import com.afs.resourcearsc.bean.ResTableTypeSpec;
import com.afs.resourcearsc.bean.ResTableTypeType;
import com.afs.resourcearsc.impl.ParseResourceImpl;

public class ResourceParseManager implements IParseResource {

    private static class ResourcePackageHolder {
        public static ResourceParseManager INSTANCE = new ResourceParseManager();
    }

    private IParseResource impl;

    private ResourceParseManager() {
        impl = new ParseResourceImpl();
    }

    public static ResourceParseManager get() {
        return ResourcePackageHolder.INSTANCE;
    }

    public ResTableHeader parseResTableHeaderChunk(byte[] arscArray, int offSet) {
        return impl.parseResTableHeaderChunk(arscArray, offSet);
    }

    public ResTableStringPool parseResTableStringPool(byte[] arscArray, int offSet) {
        return impl.parseResTableStringPool(arscArray, offSet);
    }

    public ResTablePackageHeader parseResTablePackage(byte[] arscArray, int offSet) {
        return impl.parseResTablePackage(arscArray, offSet);
    }

    @Override
    public ResTableTypeSpec parseResTableTypeSpec(byte[] arscArray, int offSet) {
        return impl.parseResTableTypeSpec(arscArray, offSet);
    }

    @Override
    public ResTableTypeType parseResTableTypeType(byte[] arscArray, int offSet) {
        return impl.parseResTableTypeType(arscArray, offSet);
    }
}
