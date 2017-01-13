package com.kunzhuo.xuechelang.network;

import android.os.Environment;

/**
 * 图片缓存参数设置
 * 
 */
public class SystemBase {
	/**
	 * android系统SD卡路径
	 */
	public static String SDCARDPATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	/**
	 * APP缓存数据根目录
	 */
	public static String APP_CACHE_PATH = SDCARDPATH
			+ "/Android/data/com.kunzhuo.xuechelang";
	/**
	 * 存放数据缓存文件，如JSON数据等
	 */
	public static String DATA_CACHE_PATH = APP_CACHE_PATH + "/cache/";
	/**
	 * 存放图片文件缓存文件
	 */
	public static String IMAGE_CACHE_PATH = APP_CACHE_PATH + "/image/";
	/**
	 * 下载文件存放路径
	 */
	public static String DOWN_LOAD_PATH = APP_CACHE_PATH + "/downloads/";

	/**
	 * 下载文件存放路径
	 */
	public static String APP_CACAHE_DIRNAME = APP_CACHE_PATH + "/dirname/";


	/**
	 * 存放地址
	 */
	public static String CACHE_DIR = "/xuechelang/";

}
