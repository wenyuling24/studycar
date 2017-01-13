package com.kunzhuo.xuechelang.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.kunzhuo.xuechelang.network.SystemBase;

public class FileUtils {

	public final static String TAG = "FileUtils";

	/**
	 * 判断SD
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 创建根目录
	 * 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文件
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		myFilePath.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路径
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		int mLength = tempList.length;
		for (int i = 0; i < mLength; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * 获取文件的Uri
	 * 
	 * @param path
	 *            文件的路径
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 通过路径获得文件名字
	 * 
	 * @param path
	 * @return
	 */
	public static String getPathByFullPath(String fullpath) {
		return fullpath.substring(0, fullpath.lastIndexOf(File.separator));
	}

	/**
	 * 通过路径获得文件名字
	 * 
	 * @param path
	 * @return
	 */
	public static String getNameByPath(String path) {
		return path.substring(path.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 通过判断文件是否存在
	 * 
	 * @param path
	 * @return
	 */

	public static boolean isFileExists(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 获得SD卡路径
	 * 
	 * @param
	 * @return String
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString();
		}
		return null;
	}

	/**
	 * encodeBase64File:(将文件转成base64 字符串). <br/>
	 * 
	 * @author guhaizhou@126.com
	 * @param path
	 *            文件路径
	 * @return
	 * @throws Exception
	 * @since JDK 1.6
	 */
	// public static String encodeBase64File(String path) throws Exception {
	// File file = new File(path);
	// FileInputStream inputFile = new FileInputStream(file);
	// byte[] buffer = new byte[(int) file.length()];
	// inputFile.read(buffer);
	// inputFile.close();
	// return Base64.encodeToString(buffer, Base64.DEFAULT);
	// }


	/**
	 * 
	 * @param fileName
	 * @param data
	 */
	public static void saveFile(String fileName, String data) {
		if (!isSdcardExist()) {
			Log.i(TAG, "没有SD卡！");
			return;
		}
		if (data == null) {
			Log.i(TAG, "data==null");
			return;
		}
		File file = new File(SystemBase.DATA_CACHE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, fileName);
		FileOutputStream outputStream = null;
		PrintStream ps = null;
		try {
			outputStream = new FileOutputStream(file);
			ps = new PrintStream(outputStream);
			ps.println(data);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFile(String fileName) {
		String result = null;
		File file = new File(SystemBase.DATA_CACHE_PATH + fileName);
		FileInputStream fis = null;
		InputStreamReader reader = null;
		try {
			fis = new FileInputStream(file);
			reader = new InputStreamReader(fis);
			StringBuffer sb = new StringBuffer();
			char[] buffer = new char[1024];
			int len;
			while ((len = reader.read(buffer)) >= 0) {
				sb.append(buffer, 0, len);
			}
			result = sb.toString().trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static String SDPATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + SystemBase.CACHE_DIR + "/photo/thumb/";

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	/**
	 * 保存图片
	 * 
	 * @param context
	 * @param bitmap
	 */
	public static void saveImageToGallery(Context context, Bitmap bitmap, int i) {

		String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";

		// 首先保存图片
		// File appDir = new File(Environment.getExternalStorageDirectory(),
		// "jiafans");
		File appDir = new File(SystemBase.DOWN_LOAD_PATH);

		if (!appDir.exists()) {
			appDir.mkdir();
		}

		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			ToastUtil.show(context, "成功保存" + i + "张图片到你的相册");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
		// .parse("file://" + file.getAbsolutePath())));

		MediaScannerConnection.scanFile(context, new String[] { Environment
				.getExternalStorageDirectory().toString() }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * android.media.MediaScannerConnection.OnScanCompletedListener
					 * #onScanCompleted(java.lang.String, android.net.Uri)
					 */
					public void onScanCompleted(String path, Uri uri) {
						Log.i("ExternalStorage", "Scanned " + path + ":");
						Log.i("ExternalStorage", "-> uri=" + uri);
					}
				});
	}
}