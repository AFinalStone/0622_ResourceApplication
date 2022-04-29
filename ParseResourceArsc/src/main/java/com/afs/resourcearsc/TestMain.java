//package com.afs.resourcearsc;
//
//import com.afs.resourcearsc.bean.ResTableHeader;
//import com.afs.resourcearsc.bean.ResTableStringPool;
//import com.afs.resourcearsc.parse.ResourceParseManager;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//public class TestMain {
//    private final static String ARSC_FILE_IN_PATH = "Plugin_ResourceArsc/res/resources_01.arsc";
//    private final static String ARSC_FILE_OUT_PATH = "Plugin_ResourceArsc/res/resources.arsc";
//
//
//    public static void main(String[] args) throws IOException {
//        //资源表头
//        int offSet = 0;
//        byte[] arscArray = getArscFromFile(new File(ARSC_FILE_IN_PATH));
//        ResTableHeader resTableHeader = ResourceParseManager.get().parseResTableHeaderChunk(arscArray, offSet);
//        byte[] byteResTableHeader = resTableHeader.toBytes();
//        System.out.println("resTableHeader----------------------------------");
//        System.out.println(resTableHeader);
//        System.out.println();
//
//        //字符串常量池
//        offSet = byteResTableHeader.length;
//        ResTableStringPool resStringPool = ResourceParseManager.get().parseResTableStringPool(arscArray, offSet);
//        byte[] byteResStringPool = resStringPool.toBytes();
//        System.out.println("resStringPool----------------------------------");
//        System.out.println(resStringPool);
//        System.out.println();
//        for (int i = 0; i < resStringPool.stringList.size(); i++) {
//            resStringPool.stringList.set(i, " ");
//        }
//        int resTableHeaderSize = resTableHeader.header.headerSize;
//        int oldResTableSize = resTableHeader.header.size;
//        int oldStringPoolSize = resStringPool.stringPoolHeader.header.size;
//        int stringPoolHeaderSize = resStringPool.stringPoolHeader.header.headerSize;
//
//        int newStringPoolSize = stringPoolHeaderSize + resStringPoolHeader.byteStringOffSet.length + byteResStringPool.length;
//        int newResTableSize = oldResTableSize + newStringPoolSize - oldStringPoolSize;
//
//        resStringPoolHeader.header.size = newStringPoolSize;//新的字符串常量池长度
//        byte[] byteResStringPoolHeader = resStringPoolHeader.toBytes();
//        resTableHeader.header.size = newResTableSize;//新的文件长度
//        byte[] byteResTableHeader = resTableHeader.toBytes();
//
//        System.out.println();
//        System.out.println(resStringPool);
//        System.out.println();
//
//        //创建一个新的资源字节文件
//        byte[] realBytes = new byte[newResTableSize];
//        //拷贝原始数据
//        offSet = 0;
//        System.arraycopy(byteResTableHeader, 0, realBytes, offSet, byteResTableHeader.length);
//        offSet = resTableHeaderSize;
//        System.arraycopy(byteResStringPoolHeader, 0, realBytes, offSet, byteResStringPoolHeader.length);
//        //拷贝原始数据，字符串偏移数组和Style偏移数组
//        offSet = resTableHeaderSize + stringPoolHeaderSize;
//        System.arraycopy(resStringPoolHeader.byteStringOffSet, 0, realBytes, offSet, resStringPoolHeader.byteStringOffSet.length);
//        //拷贝新的字符串常量池
//        offSet = resTableHeaderSize + stringPoolHeaderSize + resStringPoolHeader.byteStringOffSet.length;
//        System.arraycopy(byteResStringPool, 0, realBytes, offSet, byteResStringPool.length);
//        //拷贝原始数据，从字符串常量池之后的所有内容
//        offSet = resTableHeaderSize + stringPoolHeaderSize + resStringPoolHeader.byteStringOffSet.length + byteResStringPool.length;
//        int len = newResTableSize - offSet;
//        System.arraycopy(arscArray, resTableHeaderSize + oldStringPoolSize, realBytes, offSet, len);
//
//        writeFile(realBytes, new File(ARSC_FILE_OUT_PATH));
//    }
//
//    /**
//     * 从文件中获取resouces.arsc
//     *
//     * @param file 文件
//     * @return
//     */
//    private static byte[] getArscFromFile(File file) {
//        byte[] srcByte = null;
//        InputStream is = null;
//        ByteArrayOutputStream baos = null;
//        try {
//            is = new FileInputStream(file);
//            baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = is.read(buffer)) != -1) {
//                baos.write(buffer, 0, len);
//            }
//            srcByte = baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//                baos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return srcByte;
//    }
//
//    public static void writeFile(byte[] data, File out) throws IOException {
//        out.setLastModified(0);
//        FileOutputStream fos = new FileOutputStream(out);
//        fos.write(data);
//        fos.close();
//    }
//}
