package com.parse;

import com.parse.type.ResFile;
import com.parse.type.ResStringPool;
import com.parse.type.ResTableHeader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class ParseResourceMain {

    public static void main(String[] args) throws Exception {
        String resourceName = args[0];
        String oldStringName = args[1];
        String newStringName = args[2];
        //开始解析
        byte[] arscArr = getArscFromFile(new File(resourceName));
        ResFile resFile = ResFile.parseResFile(arscArr);
        int byteTotalSize = 0;
        //表头
        ResTableHeader resTableHeader = resFile.header;
        //全局字符串池
        ResStringPool resStringPool = resFile.globalStringPool;
        int oldByteResStringPoolLen = resStringPool.toBytes().length;
        int resEndOffSet = resTableHeader.toBytes().length + resStringPool.toBytes().length;
        int resEndLen = arscArr.length - resEndOffSet;
        byte[] resTableEndData = new byte[resEndLen];
        System.arraycopy(arscArr, resEndOffSet, resTableEndData, 0, resEndLen);
        List<String> strings = resStringPool.strings;
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).equals(oldStringName)) {
                strings.set(i, newStringName);
            }
        }
        byte[] byteResStringPool = resStringPool.toBytes();
        resStringPool.header.header.size = byteResStringPool.length;
        byteResStringPool = resStringPool.toBytes();
        resTableHeader.header.size = resTableHeader.header.size + byteResStringPool.length - oldByteResStringPoolLen;
        //把对象转化为字节数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(resTableHeader.header.size);
        byteBuffer.put(resTableHeader.toBytes());
        byteBuffer.put(resStringPool.toBytes());
        byteBuffer.put(resTableEndData);
        byteBuffer.flip();
        //重新写回文件
        FileOutputStream fos = new FileOutputStream(resourceName);
        fos.write(byteBuffer.array());
        fos.close();
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
}
