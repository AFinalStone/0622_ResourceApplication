package com.afs.resourcearsc;

import com.afs.resourcearsc.bean.Res01TableHeader;
import com.afs.resourcearsc.bean.Res02StringPool;
import com.afs.resourcearsc.bean.Res02StringPoolHeader;
import com.afs.resourcearsc.bean.Res03TablePackage;
import com.afs.resourcearsc.utils.ParseResourceUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestMain {
    private final static String ARSC_FILE_IN_PATH = "Plugin_ResourceArsc/res/resources.arsc";
    private final static String ARSC_FILE_OUT_PATH = "Plugin_ResourceArsc/res/resources_01.arsc";

    private static class StartIndexInfo {
        private static int ResStringPoolChunkOffset;//字符串常量池块起点
        private static int ResTablePackageOffset;//资源包块起点

    }

    public static void main(String[] args) throws IOException {
        //资源表头
        byte[] arscArray = getArscFromFile(new File(ARSC_FILE_IN_PATH));
        Res01TableHeader resTableHeader = ParseResourceUtil.parseResTableHeaderChunk(arscArray);
        byte[] byteResTableHeader = resTableHeader.toBytes();
        System.out.println();
        System.out.println(resTableHeader);
        System.out.println();

        //字符创常量池头
        StartIndexInfo.ResStringPoolChunkOffset = resTableHeader.getHeaderSize();
        Res02StringPoolHeader resStringPoolHeader = ParseResourceUtil.parseResStringPoolHeader(arscArray, StartIndexInfo.ResStringPoolChunkOffset);
        byte[] byteResStringPoolHeader = resStringPoolHeader.toBytes();
        System.out.println();
        System.out.println(resStringPoolHeader);
        System.out.println();

        //字符串常量池
        int stringStart = StartIndexInfo.ResStringPoolChunkOffset + resStringPoolHeader.getHeaderSize() + resStringPoolHeader.stringCount * 4 + resStringPoolHeader.styleCount * 4;
        Res02StringPool res02StringPool = ParseResourceUtil.parseResStringPool(arscArray, stringStart, resStringPoolHeader.stringCount, resStringPoolHeader.styleCount);
        byte[] byteResStringPool = res02StringPool.toBytes();
        System.out.println();
        System.out.println(res02StringPool);
        System.out.println();

        //资源包块起点
        StartIndexInfo.ResTablePackageOffset = StartIndexInfo.ResStringPoolChunkOffset + resStringPoolHeader.getHeaderSize();
        Res03TablePackage res03TablePackage = ParseResourceUtil.parseResTablePackage(arscArray, StartIndexInfo.ResTablePackageOffset);
        byte[] byteResTablePackage = res03TablePackage.toBytes();
        System.out.println();
        System.out.println(res03TablePackage);
        System.out.println();

//        ParseResourceUtil.parseTypeStringPoolChunk(arscArray);


    }

    /**
     * 从文件中获取resouces.arsc
     *
     * @param file 文件
     * @return
     */
    private static byte[] getArscFromFile(File file) {
        byte[] srcByte = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            srcByte = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return srcByte;
    }

    public static void writeFile(byte[] data, File out) throws IOException {
        FileOutputStream fos = new FileOutputStream(out);
        fos.write(data);
        fos.close();
    }
}
