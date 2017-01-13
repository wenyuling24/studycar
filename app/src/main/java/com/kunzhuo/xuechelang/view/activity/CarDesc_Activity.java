package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CarDec_Bean;
import com.kunzhuo.xuechelang.model.UsedCarPic_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.network.SystemBase;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.CarPic_Adapter;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.kunzhuo.xuechelang.widget.MyScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class CarDesc_Activity extends BaseActivity {

    @BindView(R.id.id_peiBtn)
    TextView idPeiBtn;
    @BindView(R.id.id_callBtn)
    TextView idCallBtn;
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
    @BindView(R.id.id_carBrand)
    TextView idCarBrand;
    @BindView(R.id.id_carType)
    TextView idCarType;
    @BindView(R.id.id_carYear)
    TextView idCarYear;
    @BindView(R.id.id_carLong)
    TextView idCarLong;
    @BindView(R.id.id_carPai)
    TextView idCarPai;
    @BindView(R.id.id_carColor)
    TextView idCarColor;
    @BindView(R.id.id_carBian)
    TextView idCarBian;
    @BindView(R.id.id_carZuowei)
    TextView idCarZuowei;
    @BindView(R.id.id_carTime)
    TextView idCarTime;
    @BindView(R.id.id_carDesDescible)
    WebView idPeiDesDescible;
    @BindView(R.id.second_carDesgridMore)
    MyGridView secondCarDesgridMore;
    @BindView(R.id.scrollview)
    MyScrollView scrollview;


    private Context context;
    private ProgressDialog dialog;
    private CarPic_Adapter adapter;
    private String Car_UsedID = "";
    private InputMethodManager inputMethodManager;

    @Override
    protected int setLayoutId() {
        return R.layout.cardec_layout;
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
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载,请稍后...");

        itemTitlemsg.setText("车辆详情");
        Car_UsedID = getIntent().getStringExtra("Car_UsedID");


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


        adapter = new CarPic_Adapter(context, null);
        secondCarDesgridMore.setAdapter(adapter);

        dialog.show();

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Resquest.getUsedCarTab_I(handler, Car_UsedID);
        Resquest.getUsedCarPicList(handler2, Car_UsedID);
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


    @OnClick({R.id.id_peiBtn, R.id.item_back, R.id.id_callBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_peiBtn:
                showYuyuePop(Car_UsedID);
                break;
            case R.id.item_back:
                finish();
                break;
            case R.id.id_callBtn:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + "4001141855"));
                startActivity(intent);
                break;
        }
    }


    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            Type type = new TypeToken<List<CarDec_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<CarDec_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            setDesc(mList.get(0));
                        }

                        dialog.cancel();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Resquest.FAILED_MSG:

                    dialog.cancel();
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });

    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            Type type = new TypeToken<List<UsedCarPic_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<UsedCarPic_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();
                        }
                        if (Code.equals("8")) {

                        }

                        dialog.cancel();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Resquest.FAILED_MSG:

                    dialog.cancel();
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });


    Handler handler3 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            ToastUtil.show(context, "预约成功");

                            popupWindow.dismiss();
                            hideSoftInput();

                            finish();
                        }

                        dialog.cancel();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Resquest.FAILED_MSG:

                    dialog.cancel();
                    ToastUtil.show(context, "网络异常");

                    break;
            }

            return true;
        }
    });

    private void hideSoftInput() {
        if (inputMethodManager != null) {
            View v = CarDesc_Activity.this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void setDesc(CarDec_Bean bean) {

        if (!bean.getCar_cover().equals("")) {

            ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getCar_cover(), idPeiDesImg);

        }

        idPeiDesName.setText(bean.getCar_title());
        idPeiDesPrice.setText("￥ " + bean.getCar_PriceX() + " 万");
        idCarBrand.setText("品牌: " + bean.getPname());
        idCarType.setText("车型: " + bean.getCar_models());
        idCarYear.setText("车龄: " + bean.getCar_theage());
        idCarLong.setText("里程: " + bean.getCar_mileage());
        idCarPai.setText("排量 " + bean.getCar_displacement());
        idCarColor.setText("颜色 " + bean.getCar_colour());
        idCarBian.setText("变速箱: " + bean.getCar_transmission());
        idCarZuowei.setText("座位数: " + bean.getCar_seatsNumber());
        idCarTime.setText("上牌时间: " + bean.getCar_ontime());

        idPeiDesDescible.loadDataWithBaseURL(
                null,
                "<meta name='viewport' content='width=device-width,initial-scale=1.0, maximum-scale=1' /><script type='text/javascript'>window.onload=function(){var img=document.getElementsByTagName('img');for(var i=0;i<img.length;i++){img[i].setAttribute('width','100%')}}</script>"
                        + bean.getCar_Icontent(), "text/html",

                "utf-8", null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    PopupWindow popupWindow;

    private void showYuyuePop(final String car_UsedID) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.cardec_pop, null);

        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setTouchable(true);// 可点击
        popupWindow.setOutsideTouchable(true);// 设置外部可点击,点击取消
        popupWindow.setFocusable(true);// 设置可聚焦


        final EditText editName = (EditText) view.findViewById(R.id.id_EditName);
        final EditText editPhone = (EditText) view.findViewById(R.id.id_EditPhone);
        final EditText editPrice = (EditText) view.findViewById(R.id.id_EditPrice);

        editPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        editPrice.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        Button id_YuyueBtn = (Button) view.findViewById(R.id.id_YuyueBtn);


        id_YuyueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String price = editPrice.getText().toString();


                Resquest.kanE_UsedCarBargainAdd(handler3, name, phone, price, car_UsedID);

            }
        });


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // popupWindow.dismiss();
                return true;
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 返回false,事件才能下发
                return false;
            }
        });

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        ColorDrawable dw = new ColorDrawable(0xb0000000);

        //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setBackgroundDrawable(dw);

        popupWindow.showAtLocation(itemBack, Gravity.BOTTOM, 0, 0);

    }
}
