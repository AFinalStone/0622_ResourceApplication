package com.mazaiting;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ParseResourceMain {
	private final static String FILE_PATH = "ParseResourceArsc/res/app-release-unsigned.apk";
//	private final static String FILE_PATH = "ParseResourceArsc/res/resources.arsc";
	public static void main(String[] args) {
		
		byte[] arscArray = getArscFromApk(FILE_PATH);
//		byte[] arscArray = getArscFromFile(FILE_PATH);
		
		System.out.println("parse restable header ...");
		ParseResourceUtil.parseResTableHeaderChunk(arscArray);
		System.out.println("===================================");
		System.out.println();

		System.out.println("parse resstring pool chunk  ...");
		ParseResourceUtil.parseResStringPoolChunk(arscArray);
		System.out.println("===================================");
		System.out.println();
		
		System.out.println("parse package chunk ...");
		ParseResourceUtil.parsePackage(arscArray);
		System.out.println("===================================");
		System.out.println();
		
		System.out.println("parse typestring pool chunk ...");
		ParseResourceUtil.parseTypeStringPoolChunk(arscArray);
		System.out.println("===================================");
		System.out.println();
		
		System.out.println("parse keystring pool chunk ...");
		ParseResourceUtil.parseKeyStringPoolChunk(arscArray);
		System.out.println("===================================");
		System.out.println();
		
		/**
		 * 解析正文内容
		 * 正文内容就是ResValue值，也就是开始构建public.xml中的条目信息，和类型的分离不同的xml文件
		 */
		int resCount = 0;
		while (!ParseResourceUtil.isEnd(arscArray.length)) {
			resCount++;
			boolean isSpec = ParseResourceUtil.isTypeSpec(arscArray);
			if (isSpec) {
				System.out.println("parse restype spec chunk ...");
				ParseResourceUtil.parseResTypeSpec(arscArray);
				System.out.println("===================================");
				System.out.println();
			} else {
				System.out.println("parse restype info chunk ...");
				ParseResourceUtil.parseResTypeInfo(arscArray);
				System.out.println("===================================");
				System.out.println();
			}
		}
		System.out.println("res count: " + resCount);
		
	}
	
	/**
	 * 从文件中获取resouces.arsc
	 * @param filePath 文件路径
	 * @return
	 */
	private static byte[] getArscFromFile(String filePath) {
		byte[] srcByte = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(filePath);
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

	/**
	 * 从APK中获取resources.arsc文件
	 * @param filePath 文件路径
	 * @return resources.arsc文件二进制数据
	 */
	private static byte[] getArscFromApk(String filePath) {
		byte[] srcByte = null;
		ZipFile zipFile = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			zipFile = new ZipFile(filePath);
			ZipEntry entry = zipFile.getEntry("resources.arsc");
			is = zipFile.getInputStream(entry);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);				
			}
			srcByte = baos.toByteArray();
			zipFile.close();
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