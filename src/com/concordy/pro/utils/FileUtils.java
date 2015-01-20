package com.concordy.pro.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringWriter;

import android.os.Environment;

/**
 * @author Scleo
 */
public class FileUtils {

	public static final String ROOT_DIR = "concordya";
	public static final String DOWNLOAD_DIR = "download";
	public static final String CACHE_DIR = "cache";
	public static final String ICON_DIR = "icon";
	public static final String IMAGES_DIR = "images";

	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}
	/** 创建文件夹 */
	public static boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}
	/** 判断文件是否可写 */
	public static boolean isWriteable(String path) {
		try {
			if (StringUtils.isEmpty(path)) {
				return false;
			}
			File f = new File(path);
			return f.exists() && f.canWrite();
		} catch (Exception e) {
			LogUtils.e(e);
			return false;
		}
	}
	/**
	 * 把字节数组写入文件
	 * @param content 需要写入的字符串
	 * @param path    文件路径名称
	 * @param append  是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public static boolean writeFile(byte[] content, String path, boolean append) {
		boolean res = false;
		File f = new File(path);
		RandomAccessFile raf = null;
		try {
			if (f.exists()) {
				if (!append) {
					f.delete();
					f.createNewFile();
				}
			} else {
				f.createNewFile();
			}
			if (f.canWrite()) {
				raf = new RandomAccessFile(f, "rw");
				raf.seek(raf.length());
				raf.write(content);
				res = true;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(raf);
		}
		return res;
	}
	/**
	 * 把字符串数据写入文件
	 * @param content 需要写入的字符串
	 * @param path    文件路径名称
	 * @param append  是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public static boolean writeFile(String content, String path, boolean append) {
		return writeFile(content.getBytes(), path, append);
	}
	/***
	 * 得到读取文件流数据
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	public static InputStream getStreamFromFile(String path) throws Exception{
		
		FileInputStream fis = null;
		File f = new File(path);
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
		return fis;
	}
	/***
	 * 读取文件
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public static String readFile(String filePath){
		//得到文件读取流对象
		InputStreamReader isr = null;
		String result = "";
		BufferedReader br = null;
		StringWriter sw = null;
		try {
			isr = new InputStreamReader(getStreamFromFile(filePath));
			br = new BufferedReader(isr);
			// 内存的输出流
			sw = new StringWriter();
			String str;
			while((str=br.readLine())!=null){
				sw.write(str);
			}
			// 把内存流的对象 转换成字符串
			result = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{//关闭流资源
			if(isr!=null)
				IOUtils.close(isr);
			if(br!=null)
				IOUtils.close(br);
			if(sw!=null)
				IOUtils.close(sw);
		}
		return result;
	}
	/** 获取缓存目录 */
	public static String getCacheDir() {
		return getDir(CACHE_DIR);
	}
	
	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public static String getDir(String name) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath());
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}
	
	/** 获取SD下的应用目录 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}
	/** 获取SD下的图片目录 */
	public static String getImagesPath(){
		StringBuilder sb = new StringBuilder();
		sb.append(getExternalStoragePath());
		sb.append(File.separator);
		sb.append(IMAGES_DIR);
		sb.append(File.separator);
		return sb.toString();
	}
	/** 获取应用的cache目录 */
	public static String getCachePath() {
		File f = UIUtils.getContext().getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}
}
