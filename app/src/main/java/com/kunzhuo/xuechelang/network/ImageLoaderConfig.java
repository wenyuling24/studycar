package com.kunzhuo.xuechelang.network;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

import com.kunzhuo.xuechelang.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ImageLoaderConfig {

    /**
     * 返回默认的参数配置
     * 返回默认的参数配置
     * true：显示默认的加载图片 false：不显示默认的加载图片
     *
     * @return
     */
    public static DisplayImageOptions initDisplayOptions(boolean isShowDefault) {
        DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
        // 设置图片缩放方式
        // EXACTLY: 图像将完全按比例缩小的目标大小
        // EXACTLY_STRETCHED: 图片会缩放到目标大小
        // IN_SAMPLE_INT: 图像将被二次采样的整数倍
        // IN_SAMPLE_POWER_OF_2: 图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
        // NONE: 图片不会调整
        displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
//        displayImageOptionsBuilder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
        if (isShowDefault) {
            // 默认显示的图片
            displayImageOptionsBuilder
                    .showImageOnLoading(R.drawable.animation_loading);
            // 地址为空的默认显示图片
            displayImageOptionsBuilder
                    .showImageForEmptyUri(R.drawable.animation_loading);
            // 加载失败的显示图片
            displayImageOptionsBuilder
                    .showImageOnFail(R.drawable.animation_loading);
        }
        // 开启内存缓存
        displayImageOptionsBuilder.cacheInMemory(true);
        displayImageOptionsBuilder.cacheOnDisk(true);
//		displayImageOptionsBuilder.delayBeforeLoading(50);
        // 设置图片的编码格式为RGB_565，此格式比ARGB_8888快
        displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
        return displayImageOptionsBuilder.build();
    }

    /**
     * 返回默认的参数配置
     *
     * @param isShowDefault true：显示默认的加载图片 false：不显示默认的加载图片
     * @param picId         设置默认图片
     * @return
     */
    public static DisplayImageOptions initDisplayOptions(boolean isShowDefault,
                                                         int picId) {
        DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
        // 设置图片缩放方式
        // EXACTLY: 图像将完全按比例缩小的目标大小
        // EXACTLY_STRETCHED: 图片会缩放到目标大小
        // IN_SAMPLE_INT: 图像将被二次采样的整数倍
        // IN_SAMPLE_POWER_OF_2: 图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
        // NONE: 图片不会调整
        displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
        if (isShowDefault) {
            // 默认显示的图片
            displayImageOptionsBuilder.showImageOnLoading(picId);
            // 地址为空的默认显示图片
            displayImageOptionsBuilder.showImageForEmptyUri(picId);
            // 加载失败的显示图片
            displayImageOptionsBuilder.showImageOnFail(picId);
        }
        // 开启内存缓存
        displayImageOptionsBuilder.cacheInMemory(true);
        // 开启SDCard缓存
        displayImageOptionsBuilder.cacheOnDisk(true);
        // 设置图片的编码格式为RGB_565，此格式比ARGB_8888快
        displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);

        return displayImageOptionsBuilder.build();
    }

    /**
     * 返回修改图片大小的加载参数配置
     *
     * @return
     */
    public static DisplayImageOptions initDisplayOptions(int targetWidth,
                                                         boolean isShowDefault) {
        DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
        // 设置图片缩放方式
        // EXACTLY: 图像将完全按比例缩小的目标大小
        // EXACTLY_STRETCHED: 图片会缩放到目标大小
        // IN_SAMPLE_INT: 图像将被二次采样的整数倍
        // IN_SAMPLE_POWER_OF_2: 图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
        // NONE: 图片不会调整
        displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
        if (isShowDefault) {
            // 默认显示的图片
            displayImageOptionsBuilder
                    .showImageOnLoading(R.drawable.animation_loading);
            // 地址为空的默认显示图片
            displayImageOptionsBuilder
                    .showImageForEmptyUri(R.drawable.animation_loading);
            // 加载失败的显示图片
            displayImageOptionsBuilder
                    .showImageOnFail(R.drawable.animation_loading);
        }
        // 开启内存缓存
        displayImageOptionsBuilder.cacheInMemory(true);
        // 开启SDCard缓存
        displayImageOptionsBuilder.cacheOnDisk(true);
        // 设置图片的编码格式为RGB_565，此格式比ARGB_8888快
        displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
        // 设置图片显示方式
        displayImageOptionsBuilder.displayer(new SimpleImageDisplayer(
                targetWidth));

        return displayImageOptionsBuilder.build();
    }

    /**
     * 异步图片加载ImageLoader的初始化操作，在Application中调用此方法
     *
     * @param context   上下文对象
     * @param cacheDisc 绝对路径
     */
    public static void initImageLoader(Context context, String cacheDisc) {
        File file = new File(cacheDisc);
        // 配置ImageLoader
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                context);
        // 设置线程数量
        builder.threadPoolSize(3);
        // 设定线程等级比普通低一点
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        // 设定内存缓存为弱缓存
        builder.memoryCache(new WeakMemoryCache());
        // 设定内存图片缓存大小限制，不设置默认为屏幕的宽高
        builder.memoryCacheExtraOptions(480, 800);
        // 设定只保存同一尺寸的图片在内存
        builder.denyCacheImageMultipleSizesInMemory();
        // 设定缓存的SDcard目录，UnlimitDiscCache速度最快
        builder.diskCache(new UnlimitedDiskCache(file));
        // builder.diskCacheFileCount(500);
        builder.diskCacheSize(200 * 1024 * 1024);
        builder.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
        builder.memoryCacheSize(2 * 1024 * 1024);
        // 设定缓存到SDCard目录的文件命名
        // builder.discCacheFileNameGenerator(new HashCodeFileNameGenerator());
        // 设定网络连接超时 timeout: 10s
        // 读取网络连接超时read timeout: 60s
        builder.imageDownloader(new BaseImageDownloader(context, 5 * 1000,
                30 * 1000));
        // 设置ImageLoader的配置参数
        builder.defaultDisplayImageOptions(initDisplayOptions(false));
//		builder.writeDebugLogs();

        // 初始化ImageLoader
        ImageLoader.getInstance().init(builder.build());
    }

}
