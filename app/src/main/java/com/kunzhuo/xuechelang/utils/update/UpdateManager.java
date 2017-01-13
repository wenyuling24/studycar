package com.kunzhuo.xuechelang.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.widget.dialog.CustomDialog;
import com.kunzhuo.xuechelang.widget.dialog.DownLoadDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class UpdateManager {
    private final static String TAG = "UpdateManager";
    /* 需要更新 */
    private static final int NEED = 1;
    /* 不需要更新 */
    private static final int UNNEED = 2;
    /* 下载中 */
    private static final int DOWNLOAD = 101;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 102;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;

    private final static String XML_URL = "http://www.xueche555.com/Upload/AppExe/stu_xuechelang_version.xml";
    // UpdataInfo updataInfo;

    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    String apkurl;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEED:
                    showNoticeDialog();
                    break;
                case UNNEED:
                    // Toast.makeText(mContext, R.string.soft_update_no,
                    // Toast.LENGTH_LONG).show();
                    break;
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        new Thread() {
            @Override
            public void run() {
                isUpdate();
            }
        }.start();

    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private void isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);
        URL url;// 定义网络中version.xml的连接
        try {
            url = new URL(XML_URL);// 创建version.xml的连接地址。
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            InputStream inStream = connection.getInputStream();// 从输入流获取数据
            ParseXmlService service = new ParseXmlService();
            // updataInfo = service.getUpdataInfo(inStream);
            mHashMap = service.parseXml(inStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mHashMap != null) {
            int serviceCode = Integer.valueOf(mHashMap.get("version"));

            System.out.println(versionCode + " 本地版本号");
            System.out.println(serviceCode + " 服务器版本号");

            apkurl = mHashMap.get("url");

            System.out.println(apkurl + " 服务器url");

            // 版本判断
            if (serviceCode > versionCode) {
                mHandler.sendEmptyMessage(NEED);
            } else {
                mHandler.sendEmptyMessage(UNNEED);
            }
        }
    }

    /**
     * 获取当前软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    "com.kunzhuo.xuechelang", 0).versionCode;


        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle(R.string.soft_update_title).setMessage(
                mHashMap.get("info"));
        builder.setOnPositiveListener(R.string.soft_update_updatebtn,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
        builder.setOnNeagtiveListener(R.string.soft_update_later,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        DownLoadDialog.Builder builder = new DownLoadDialog.Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.addView(v);
        // 取消更新
        builder.setOnNeagtiveListener(R.string.soft_update_cancel,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                    }
                });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory()
                            + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(mHashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mHashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];

                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, mHashMap.get("name"));
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
