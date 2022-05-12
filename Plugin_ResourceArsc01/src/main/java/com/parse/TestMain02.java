//package com.parse;
//
//import com.parse.type.ResFile;
//import com.parse.type.ResStringPool;
//import com.parse.type.ResTableEntryKv;
//import com.parse.type.ResTableHeader;
//import com.parse.type.ResTablePackage;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.List;
//
//public class TestMain02 {
//
//    private final static String ARSC_FILE_IN_PATH = "Plugin_ResourceArsc01/res/resources_01.arsc";
//    private final static String ARSC_FILE_OUT_PATH = "Plugin_ResourceArsc01/res/resources.arsc";
//
//    public static void main(String[] args) {
//        ResFile resFile = ResourceArscFileParser.parseFile(ARSC_FILE_IN_PATH);
//        int byteTotalSize = 0;
//        //表头
//        ResTableHeader resTableHeader = resFile.header;
//        byte[] byteResTableHeader = resTableHeader.toBytes();
//        byteTotalSize = byteTotalSize + byteResTableHeader.length;
//        System.out.println("resTableHeader----------------------------------");
//        System.out.println(resTableHeader);
//        System.out.println();
//
//        //全局字符串池
//        ResStringPool resStringPool = resFile.globalStringPool;
//        byte[] byteResStringPool = resStringPool.toBytes();
//        byteTotalSize = byteTotalSize + byteResStringPool.length;
//        System.out.println("resStringPool----------------------------------");
//        System.out.println(resStringPool);
//        System.out.println();
//
//        //Package包中的字符串池
//        List<ResTablePackage> packageList = resFile.pkgs;
//        for (int i = 0; i < packageList.size(); i++) {
//            ResTablePackage packageItem = packageList.get(i);
//            byte[] bytePackageItem = packageItem.toBytes();
//            byteTotalSize = byteTotalSize + bytePackageItem.length;
//            System.out.println(String.format("packageItem_%S----------------------------------", i));
//            System.out.println(packageItem);
//            System.out.println();
//        }
//        //把对象转化为字节数据
//        ByteBuffer byteBuffer = ByteBuffer.allocate(byteTotalSize);
//        byteBuffer.put(resTableHeader.toBytes());
//        byteBuffer.put(resStringPool.toBytes());
//        for (int i = 0; i < packageList.size(); i++) {
//            ResTablePackage packageItem = packageList.get(i);
//            byte[] bytePackageItem = packageItem.toBytes();
//            byteBuffer.put(bytePackageItem);
//        }
//        byteBuffer.flip();
//        writeFile(byteBuffer.array(), new File(ARSC_FILE_OUT_PATH));
//
//        List<ResTableEntryKv> list = resFile.getEntry(0x7F030002);
//        for (int i = 0; i < list.size(); i++) {
//            ResTableEntryKv resTableEntryKv = list.get(i);
//            System.out.println(String.format("0x7F030002ResTableEntryKv_%S----------------------------------", i));
//            System.out.println(resTableEntryKv);
//        }
//
//        list = resFile.getEntry(0x7F020000);
//        for (int i = 0; i < list.size(); i++) {
//            ResTableEntryKv resTableEntryKv = list.get(i);
//            System.out.println(String.format("0x7F020000ResTableEntryKv_%S----------------------------------", i));
//            System.out.println(resTableEntryKv);
//        }
//    }
//
//    public static void writeFile(byte[] data, File out) {
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(out);
//            fos.write(data);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//}
