package com.afs.resourcearsc;

import com.afs.resourcearsc.bean.ResTableHeader;
import com.afs.resourcearsc.bean.ResTableStringPool;
import com.afs.resourcearsc.bean.ResTableStringPoolHeader;
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


    public static void main(String[] args) throws IOException {
        test02();
    }

    private static void test01() throws IOException {
        //资源表头
        int offSet = 0;
        byte[] arscArray = getArscFromFile(new File(ARSC_FILE_IN_PATH));
        ResTableHeader resTableHeader = ParseResourceUtil.parseResTableHeaderChunk(arscArray, offSet);
        byte[] byteResTableHeader = resTableHeader.toBytes();
        System.out.println();
        System.out.println(resTableHeader);
        System.out.println();

        //字符创常量池头
        offSet = resTableHeader.header.headerSize;
        ResTableStringPoolHeader resStringPoolHeader = ParseResourceUtil.parseResStringPoolHeader(arscArray, offSet);

        //字符串常量池
        offSet = offSet + resStringPoolHeader.getHeaderSize() + resStringPoolHeader.stringCount * 4 + resStringPoolHeader.styleCount * 4;
        ResTableStringPool resStringPool = ParseResourceUtil.parseResStringPool(arscArray, offSet, resStringPoolHeader.stringCount, resStringPoolHeader.styleCount);
//        resStringPool.stringList.set(4, "插件话123");

        byte[] byteResStringPool = resStringPool.toBytes();
        resStringPoolHeader.header.size = resStringPoolHeader.getHeaderSize() + byteResStringPool.length + resStringPoolHeader.stringCount * 4;
        byte[] byteResStringPoolHeader = resStringPoolHeader.toBytes();
        System.out.println();
        System.out.println(resStringPool);
        System.out.println();

//        //资源包Package块起点
//        offSet = resTableHeader.header.headerSize + resStringPoolHeader.header.size;
//        ResTablePackage resTablePackage = ParseResourceUtil.parseResTablePackage(arscArray, offSet);
//        byte[] byteResTablePackage = resTablePackage.toBytes();
//        System.out.println();
//        System.out.println(resTablePackage);
//        System.out.println();
//       //Package_Type字符创常量池头
//        offSet = resTableHeader.header.headerSize + resStringPoolHeader.header.size + resTablePackage.typeStrings;
//        ResTableStringPool packageTypeStringPool = ParseResourceUtil.parseResStringPool(arscArray, offSet, resStringPoolHeader.stringCount, resStringPoolHeader.styleCount);
//        byte[] bytePackageTypeStringPool = packageTypeStringPool.toBytes();
//        System.out.println();
//        System.out.println(packageTypeStringPool);
//        System.out.println();
//        //Package_Key字符串常量池
//        offSet = resTableHeader.header.headerSize + resStringPoolHeader.header.size + resTablePackage.keyStrings;
//        ResTableStringPool packageKeyStringPool = ParseResourceUtil.parseResStringPool(arscArray, offSet, resStringPoolHeader.stringCount, resStringPoolHeader.styleCount);
//        byte[] bytePackageKeyStringPool = packageKeyStringPool.toBytes();
//        System.out.println();
//        System.out.println(packageKeyStringPool);
//        System.out.println();
        //Package的额外配置
//        offSet = resTableHeader.header.headerSize + resStringPoolHeader.header.size + resTablePackage.header.size;
//        ResTableTypeSpec resTableTypeSpec =


        //添加到缓存
        int length = offSet + byteResStringPool.length;
        byte[] resultByteBuffer = new byte[length];
        offSet = 0;
        System.arraycopy(byteResTableHeader, 0, resultByteBuffer, offSet, byteResTableHeader.length);
        offSet = resTableHeader.header.headerSize;
        System.arraycopy(byteResStringPoolHeader, 0, resultByteBuffer, offSet, byteResStringPoolHeader.length);
        offSet = offSet + resStringPoolHeader.getHeaderSize() + resStringPoolHeader.stringCount * 4 + resStringPoolHeader.styleCount * 4;
        System.arraycopy(byteResStringPool, 0, resultByteBuffer, offSet, byteResStringPool.length);


        writeFile(resultByteBuffer, new File(ARSC_FILE_OUT_PATH));
    }

    private static void test02() throws IOException {
        //资源表头
        int offSet = 0;
        byte[] arscArray = getArscFromFile(new File(ARSC_FILE_IN_PATH));
        ResTableHeader resTableHeader = ParseResourceUtil.parseResTableHeaderChunk(arscArray, offSet);
        System.out.println();
        System.out.println(resTableHeader);
        System.out.println();

        //字符创常量池头
        offSet = resTableHeader.header.headerSize;
        ResTableStringPoolHeader resStringPoolHeader = ParseResourceUtil.parseResStringPoolHeader(arscArray, offSet);

        //字符串常量池
        offSet = offSet + resStringPoolHeader.getHeaderSize() + resStringPoolHeader.stringCount * 4 + resStringPoolHeader.styleCount * 4;
        ResTableStringPool resStringPool = ParseResourceUtil.parseResStringPool(arscArray, offSet, resStringPoolHeader.stringCount, resStringPoolHeader.styleCount);

        //修改字符串长度
        resStringPool.stringList.set(4, "插件话1234567");
        byte[] byteResStringPool = resStringPool.toBytes();

        int resTableHeaderSize = resTableHeader.header.headerSize;
        int oldResTableSize = resTableHeader.header.size;
        int oldStringPoolHeaderSize = resStringPoolHeader.header.headerSize;
        int oldStringPoolSize = resStringPoolHeader.header.size;

        int newStringPoolSize = oldStringPoolHeaderSize + byteResStringPool.length + resStringPoolHeader.stringCount * 4;
        int newResTableSize = oldResTableSize + newStringPoolSize - oldStringPoolSize;
        resStringPoolHeader.header.size = newStringPoolSize;//新的字符串常量池长度
        byte[] byteResStringPoolHeader = resStringPoolHeader.toBytes();
        resTableHeader.header.size = newResTableSize;//新的文件长度
        byte[] byteResTableHeader = resTableHeader.toBytes();


        System.out.println();
        System.out.println(resStringPool);
        System.out.println();

        //新的资源的后半部分
        byte[] realEndBytes = new byte[oldResTableSize - resTableHeaderSize - oldStringPoolSize];
        System.arraycopy(arscArray, resTableHeaderSize + oldStringPoolSize, realEndBytes, 0, realEndBytes.length);

        //新的资源字节文件
        byte[] realBytes = new byte[newResTableSize];
        offSet = 0;
        System.arraycopy(byteResTableHeader, 0, realBytes, offSet, byteResTableHeader.length);
        offSet = resTableHeaderSize;
        System.arraycopy(byteResStringPoolHeader, 0, realBytes, offSet, byteResStringPoolHeader.length);
        offSet = offSet + resStringPoolHeader.getHeaderSize() + resStringPoolHeader.stringCount * 4 + resStringPoolHeader.styleCount * 4;
        System.arraycopy(byteResStringPool, 0, realBytes, offSet, byteResStringPool.length);
        offSet = 1;
        System.arraycopy(realEndBytes, 0, realBytes, offSet, realEndBytes.length);

        writeFile(realBytes, new File(ARSC_FILE_OUT_PATH));
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
