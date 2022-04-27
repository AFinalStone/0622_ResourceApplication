package com.parse;

import com.parse.type.ResFile;
import com.parse.type.ResStringPool;
import com.parse.type.ResTableHeader;
import com.parse.type.ResTablePackage;

import java.util.List;

public class TestMain02 {

    private final static String ARSC_FILE_IN_PATH = "Plugin_ResourceArsc/res/resources_01.arsc";
    private final static String ARSC_FILE_OUT_PATH = "Plugin_ResourceArsc/res/resources.arsc";

    public static void main(String[] args) {
        ResFile resFile = ResourceArscFileParser.parseFile(ARSC_FILE_IN_PATH);

        //表头
        ResTableHeader resTableHeader = resFile.header;
        System.out.println("resTableHeader----------------------------------");
        System.out.println(resTableHeader);
        System.out.println();

        //全局字符串池
        ResStringPool resStringPool = resFile.globalStringPool;
        System.out.println("resStringPool----------------------------------");
        System.out.println(resStringPool);
        System.out.println();
//
//        //全局字符串池
//        List<ResTablePackage> packageList = resFile.pkgs;
//        for (int i = 0; i < packageList.size(); i++) {
//            ResTablePackage packageItem = packageList.get(i);
//            System.out.println(String.format("packageItem%S----------------------------------", i));
//            System.out.println(packageItem);
//            System.out.println();
//        }

        System.out.println(resFile.toString());
    }
}
