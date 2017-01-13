package com.kunzhuo.xuechelang.utils.bitmap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.utils.DateUtils;
import com.kunzhuo.xuechelang.utils.FileUtils;

/**
 * 图片管理工具
 *
 * @author SB
 */
public class BitmapUtil {

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 根据path缩放图片的方法
     *
     * @param path
     * @return
     */
    public static String scal(String path) {
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 700 * 700;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);

            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            outputFile = new File(BitmapUtil.createImageFile(path).getPath());

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,
                        (int) (100 / (scale + 0.5)), fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Log.d(, sss ok + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(BitmapUtil.createImageFile(path)
                        .getPath());
                BitmapUtil.copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile.getPath();

    }

    public static Uri createImageFile(String path) {

        // Create an image file name
        String imageFileName = FileUtils.getNameByPath(path) + "_"
                + DateUtils.getNowTime();

        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg");// imageFileName,
            // .jpg,
            // storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(image);
    }

    /**
     * 图片压缩1
     *
     * @param source
     * @param dest
     */
    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel
                        .transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }


    /**
     * bitmap下载方法
     *
     * @param imgUrl
     * @return
     */
    public static Bitmap loadRmoteImage(String imgUrl) {

        URL fileURL = null;
        Bitmap bitmap = null;

        InputStream input = null;

        try {
            fileURL = new URL(imgUrl);
        } catch (MalformedURLException err) {
            err.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) fileURL
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            int length = (int) conn.getContentLength();
            if (length != -1) {
                byte[] imgData = new byte[length];
                byte[] buffer = new byte[1024];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(buffer)) > 0) {
                    System.arraycopy(buffer, 0, imgData, destPos, readLen);
                    destPos += readLen;
                }
                // bitmap = BitmapFactory.decodeByteArray(imgData, 0,
                // imgData.length);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                input = new ByteArrayInputStream(imgData);
                SoftReference softRef = new SoftReference(
                        BitmapFactory.decodeStream(input, null, options));
                bitmap = (Bitmap) softRef.get();

                if (input != null) {
                    input.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /***
     * 等比例压缩图片
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
                                   int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // Log.e("jj", "图片宽度" + w + ",screenWidth=" + screenWidth);
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;

        // scale = scale < scale2 ? scale : scale2;

        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /**
     * View转Bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }


    /**
     * 获取图片的宽高
     *
     * @param path
     * @return
     */
    public static int[] getBitmapSize(String path) {
        int[] size = {0, 0};
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不加载bitmap到内存中
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        size[0] = outWidth;
        size[1] = outHeight;
        return size;
    }
}
