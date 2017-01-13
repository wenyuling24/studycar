package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.PeilianDes_Bean;
import com.kunzhuo.xuechelang.model.Peilian_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.network.SystemBase;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;
import com.kunzhuo.xuechelang.view.adapter.PeiDes_Adapter;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class PeiDesc_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_peiDesImg)
    ImageView idPeiDesImg;
    @BindView(R.id.id_peiDesName)
    TextView idPeiDesName;
    @BindView(R.id.id_peiDesPrice)
    TextView idPeiDesPrice;
    @BindView(R.id.id_peiDesStartTime)
    TextView idPeiDesStartTime;
    @BindView(R.id.id_peiDesStoptTime)
    TextView idPeiDesStoptTime;
    @BindView(R.id.id_peiDesIsJia)
    TextView idPeiDesIsJia;
    @BindView(R.id.second_peiDesgridMore)
    MyGridView secondPeiDesgridMore;
    @BindView(R.id.id_peiBtn)
    TextView idPeiBtn;
    @BindView(R.id.id_peiDesDescible)
    WebView idPeiDesDescible;

    private Context context;
    private String Id;
    private ProgressDialog dialog;
    private PeiDes_Adapter adapter;
    private PeilianDes_Bean pei_Bean;

    @Override
    protected int setLayoutId() {
        return R.layout.peiliandes_layout;
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

        itemTitlemsg.setText("学车郎—陪练详情");
        Id = getIntent().getStringExtra("Id");
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        adapter = new PeiDes_Adapter(context, null);
        secondPeiDesgridMore.setAdapter(adapter);

        secondPeiDesgridMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Peilian_Bean bean = (Peilian_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(context, PeiDesc_Activity.class);
                intent.putExtra("Id", bean.getID());
                startActivity(intent);

                finish();
            }
        });

        Resquest.getSparringSingle(handler, Id);

        Resquest.getSparringList(handlerList, 1, 7);

        idPeiDesDescible.getSettings().setLoadWithOverviewMode(true);
        idPeiDesDescible.getSettings().setUseWideViewPort(true);

        idPeiDesDescible.getSettings().setRenderPriority(
                WebSettings.RenderPriority.HIGH);
        idPeiDesDescible.getSettings().setCacheMode(
                WebSettings.LOAD_DEFAULT); // 设置 缓存模式

        idPeiDesDescible.getSettings().setBuiltInZoomControls(false);

        idPeiDesDescible.getSettings().setSupportZoom(true);

        idPeiDesDescible.getSettings().setDefaultFontSize(14);

        idPeiDesDescible.getSettings().setJavaScriptEnabled(true); // 设置支持Javascript

        idPeiDesDescible.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        idPeiDesDescible.setVerticalScrollBarEnabled(false);
        idPeiDesDescible.setVerticalScrollbarOverlay(false);
        idPeiDesDescible.setHorizontalScrollBarEnabled(false);
        idPeiDesDescible.setHorizontalScrollbarOverlay(false);

        idPeiDesDescible.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 开启 DOM storage API 功能
        idPeiDesDescible.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        idPeiDesDescible.getSettings().setDatabaseEnabled(true);

        String cacheDirPath = SystemBase.APP_CACAHE_DIRNAME;
        // String cacheDirPath =
        // getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        // 设置数据库缓存路径
        idPeiDesDescible.getSettings().setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        idPeiDesDescible.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        idPeiDesDescible.getSettings().setAppCacheEnabled(true);
        idPeiDesDescible.setWebChromeClient(new WebChromeClient());

        idPeiDesDescible.setWebViewClient(new WebViewClient() {
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

        idPeiDesDescible.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                idPeiDesDescible.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });

        idPeiDesDescible.addJavascriptInterface(this, "App");
    }

    @JavascriptInterface
    public void resize(final float height) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                idPeiDesDescible.setLayoutParams(new LinearLayout.LayoutParams(
                        getResources().getDisplayMetrics().widthPixels,
                        (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<PeilianDes_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<PeilianDes_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        setPeiDesMsg(mList.get(0));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    dialog.cancel();
                    break;
            }

            return true;
        }
    });

    Handler handlerList = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            Type type = new TypeToken<List<Peilian_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<Peilian_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();
                        }
                        if (Code.equals("8")) {


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    dialog.cancel();
                    break;
            }

            return true;
        }
    });

    @OnClick({R.id.item_back, R.id.id_peiBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_peiBtn:

                if (pei_Bean.getProStart().equals("") && pei_Bean.getProEnd().equals("")) {

                    Intent intent = new Intent(context, WannaPeiNoTime_Activity.class);
                    intent.putExtra("ID", pei_Bean.getID());
                    intent.putExtra("PriceType", pei_Bean.getUnitPrice());
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(context, WannaPei_Activity.class);
                    intent.putExtra("ID", pei_Bean.getID());
                    intent.putExtra("PriceType", pei_Bean.getUnitPrice());
                    startActivity(intent);
                }


                break;
        }
    }

    private void setPeiDesMsg(PeilianDes_Bean bean) {

        pei_Bean = bean;

        if (!bean.getProCover().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getProCover(), idPeiDesImg);
        }

        idPeiDesName.setText(bean.getProName());
        idPeiDesPrice.setText("￥ " + bean.getProMoney());

        if (bean.getProStart().equals("")) {
            idPeiDesStartTime.setVisibility(View.GONE);
        } else {
            idPeiDesStartTime.setVisibility(View.VISIBLE);
            idPeiDesStartTime.setText("开始时间: " + bean.getProStart());
        }

        if (bean.getProEnd().equals("")) {
            idPeiDesStoptTime.setVisibility(View.GONE);

        } else {
            idPeiDesStoptTime.setVisibility(View.VISIBLE);
            idPeiDesStoptTime.setText("结束时间: " + bean.getProEnd());
        }

        idPeiDesIsJia.setText("是否需要驾照: " + bean.getProType());

        idPeiDesDescible.loadDataWithBaseURL(
                null,
                "<meta name='viewport' content='width=device-width,initial-scale=1.0, maximum-scale=1' /><script type='text/javascript'>window.onload=function(){var img=document.getElementsByTagName('img');for(var i=0;i<img.length;i++){img[i].setAttribute('width','100%')}}</script>"
                        + bean.getProDesc(), "text/html",

                "utf-8", null);


    }

}
