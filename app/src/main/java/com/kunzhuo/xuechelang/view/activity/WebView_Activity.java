package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.Novice_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.network.SystemBase;
import com.kunzhuo.xuechelang.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class WebView_Activity extends BaseActivity {


    private static final String TAG = WebView_Activity.class.getSimpleName();

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_webView)
    WebView idWebView;

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private Context context;
    private String Title = "";
    private String url = "http://www.xuechelang.com/"; //"https://www.taobao.com/";//
    private ProgressDialog dialog;

    @Override
    protected int setLayoutId() {
        return R.layout.webview_layout;
    }

    @Override
    protected void setDefaultViews() {
        initViews();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void updateViews() {

    }

    private void initViews() {
        context = this;
        Title = getIntent().getStringExtra("Title");
        url = getIntent().getStringExtra("Url");

        System.out.println(url + " 地址");

        itemTitlemsg.setText(Title);

        idWebView.getSettings().setLoadWithOverviewMode(true);
        idWebView.getSettings().setUseWideViewPort(true);

        idWebView.getSettings().setRenderPriority(
                WebSettings.RenderPriority.HIGH);
        idWebView.getSettings().setCacheMode(
                WebSettings.LOAD_DEFAULT); // 设置 缓存模式

        idWebView.getSettings().setBuiltInZoomControls(false);

        idWebView.getSettings().setSupportZoom(true);

        idWebView.getSettings().setDefaultFontSize(14);

        idWebView.getSettings().setJavaScriptEnabled(true); // 设置支持Javascript

        idWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        idWebView.setVerticalScrollBarEnabled(false);
        idWebView.setVerticalScrollbarOverlay(false);
        idWebView.setHorizontalScrollBarEnabled(false);
        idWebView.setHorizontalScrollbarOverlay(false);

        idWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 开启 DOM storage API 功能
        idWebView.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        idWebView.getSettings().setDatabaseEnabled(true);

        String cacheDirPath = SystemBase.APP_CACAHE_DIRNAME;
//         String cacheDirPath =
//         getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        // 设置数据库缓存路径
        idWebView.getSettings().setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        idWebView.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        idWebView.getSettings().setAppCacheEnabled(true);
        idWebView.setWebChromeClient(new WebChromeClient());

        idWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        idWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                idWebView.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });

        idWebView.addJavascriptInterface(this, "App");

        if (getIntent().hasExtra("IID")) {

            String IID = getIntent().getStringExtra("IID");

            dialog = new ProgressDialog(this);
            dialog.setMessage("正在加载,请稍后...");
            dialog.show();
            Resquest.getNoviceListID(handler, IID);

        } else {

            idWebView.loadUrl(url);

        }


    }

    @JavascriptInterface
    public void resize(final float height) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                idWebView.setLayoutParams(new LinearLayout.LayoutParams(
                        getResources().getDisplayMetrics().widthPixels,
                        (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<Novice_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Novice_Bean> mList = mapper.transformCollection(jsonObject, "List");

                        idWebView.loadDataWithBaseURL(
                                null,
                                "<meta name='viewport' content='width=device-width,initial-scale=1.0, maximum-scale=1' /><script type='text/javascript'>window.onload=function(){var img=document.getElementsByTagName('img');for(var i=0;i<img.length;i++){img[i].setAttribute('width','100%')}}</script>"
                                        + mList.get(0).getIcontent(), "text/html",
                                "utf-8", null);

                        dialog.cancel();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    dialog.cancel();
                    break;
            }

            return true;
        }
    });


    @OnClick(R.id.item_back)
    public void onClick() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (idWebView.canGoBack()) {
                idWebView.goBack();// 网页回退
            }

            if (!idWebView.canGoBack()) {
//                clearWebViewCache();
                finish();
            }
            return true;// 不结束当前activity
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        // 清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WebView 缓存文件
        File appCacheDir = new File(SystemBase.APP_CACAHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()
                + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir.toString());
        }
        // 删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir.toString());
        }
    }


}
