package com.afs.resourcearsc;

import com.afs.resourcearsc.bean.ResTableHeader;
import com.afs.resourcearsc.bean.ResTableStringPool;
import com.afs.resourcearsc.parse.ResourceParseManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestMain {
    private final static String ARSC_FILE_IN_PATH = "ParseResourceArsc/res/resources_01.arsc";
    private final static String ARSC_FILE_OUT_PATH = "ParseResourceArsc/res/resources.arsc";
    //80F9 82F9

    public static void main(String[] args) throws IOException {
        //资源表头
        int offSet = 0;
        byte[] arscArray = getArscFromFile(new File(ARSC_FILE_IN_PATH));
        ResTableHeader resTableHeader = ResourceParseManager.get().parseResTableHeaderChunk(arscArray, offSet);
        System.out.println("resTableHeader----------------------------------");
        System.out.println(resTableHeader);
        System.out.println();

        //字符串常量池
        offSet = resTableHeader.header.headerSize;
        ResTableStringPool resStringPool = ResourceParseManager.get().parseResTableStringPool(arscArray, offSet);
        System.out.println("resStringPool----------------------------------");
        System.out.println(resStringPool);
        System.out.println();
//        for (int i = 0; i < resStringPool.stringList.size(); i++) {
//            resStringPool.stringList.set(i, " ");
//        }
        int resTableHeaderSize = resTableHeader.header.headerSize;//资源表头长度不会发生变化
        int oldResTableSize = resTableHeader.header.size;
        int oldStringPoolSize = resStringPool.stringPoolHeader.header.size;
        int oldResEndOffset = resTableHeaderSize + oldStringPoolSize;
        int oldResEndLen = arscArray.length - oldResEndOffset;

        //新的字符串常量池总长度
        int newStringPoolSize = resStringPool.toBytes().length;
        //新的资源索引表总长度
        int newResTableSize = oldResTableSize + newStringPoolSize - oldStringPoolSize;

        resStringPool.stringPoolHeader.header.size = newStringPoolSize;//新的字符串常量池长度
//        resStringPool.stringPoolHeader.stylesStart = newStringPoolSize;//新的字符串常量池长度
        resTableHeader.header.size = newResTableSize;//新的文件长度
        byte[] byteResTableHeader = resTableHeader.toBytes();
        byte[] byteResStringPool = resStringPool.toBytes();

        //创建一个新的资源字节文件
        byte[] realBytes = new byte[newResTableSize];
        //拷贝资源表头部数据
        offSet = 0;
        System.arraycopy(byteResTableHeader, 0, realBytes, offSet, byteResTableHeader.length);
        //拷贝字符常量池数据
        offSet = byteResTableHeader.length;
        System.arraycopy(byteResStringPool, 0, realBytes, offSet, byteResStringPool.length);
        //拷贝从字符串常量池之后的所有原始数据
        offSet = offSet + byteResStringPool.length;
        System.arraycopy(arscArray, oldResEndOffset, realBytes, offSet, oldResEndLen);
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
        out.setLastModified(0);
        FileOutputStream fos = new FileOutputStream(out);
        fos.write(data);
        fos.close();
    }
}
