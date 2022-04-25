package com.mazaiting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestMain {

    private final static String ARSC_FILE_IN_PATH = "ParseResourceArsc/res/resources.arsc";
    private final static String ARSC_FILE_OUT_PATH = "ParseResourceArsc/res/source_01.arsc";

    public static void main(String[] args) throws IOException {
        //读取文件
        File fileInput = new File(ARSC_FILE_IN_PATH);
        byte[] arscArray = getArscFromFile(fileInput);
        ParseResourceUtil.parseResTableHeaderChunk(arscArray);
    }

    public static void writeFile(byte[] data, File out) throws IOException {
        FileOutputStream fos = new FileOutputStream(out);
        fos.write(data);
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
