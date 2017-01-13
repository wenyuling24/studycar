package com.kunzhuo.xuechelang.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DateUtils;
import com.kunzhuo.xuechelang.utils.StringUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.utils.alipay.AliPayUtils;
import com.kunzhuo.xuechelang.utils.alipay.PayResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/3 0003.
 */
public class OrderPaySelectActivity extends BaseActivity {
    final static String TAG = OrderPaySelectActivity.class.getSimpleName();

    public final static String INTENT_ORDERCODE = "orderCode";
    public final static String INTENT_PAYCODE = "payCode";
    public final static String INTENT_SHOPNAME = "orderName";
    public final static String INTENT_ORDER_MONEY = "orderMoney";
    public final static String GUID = "guid";
    public final static String INTENT_INDEX = "index";
    public final static int INDEX_INSURE = 1001;

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.tv_pay_money)
    TextView tv_pay_money;
    @BindView(R.id.tv_delivery_money)
    TextView tv_delivery_money;
    @BindView(R.id.btn_topay)
    Button btn_topay;
    @BindView(R.id.img_weixin)
    ImageView imgWeixin;
    @BindView(R.id.btn_weixin)
    RelativeLayout btnWeixin;
    @BindView(R.id.img_ali)
    ImageView imgAli;
    @BindView(R.id.btn_ali)
    RelativeLayout btnAli;

    private Context context;
    private String orderCode;
    private String orderMoney;
    private String shopName;
    private String payCode;
    private String guid;
    private int paytype = 1; // 0 微信支付 1支付宝支付

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pay_select;
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
        itemTitlemsg.setText("确认支付");

        AndroidApplication.activityList2.add(OrderPaySelectActivity.this);
        Bundle bundle = getIntent().getExtras();
        orderCode = bundle.getString(INTENT_ORDERCODE, "");
        shopName = bundle.getString(INTENT_SHOPNAME, "");
        guid = bundle.getString(GUID, "");
        orderMoney = bundle.getString(INTENT_ORDER_MONEY, "0.00");

        tv_pay_money.setText("￥" + orderMoney);

    }

    @OnClick({R.id.btn_topay, R.id.item_back, R.id.btn_weixin, R.id.btn_ali})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.btn_topay:
                if (paytype == 0) {

                    ToastUtil.show(context, "微信支付正在接入");
                }
                if (paytype == 1) {

                    payCode = ToolsUtils.getSixRandom(6) + DateUtils.getNowtimeX() + ToolsUtils.getSixRandom(3);

                    System.out.println(payCode + "订单编号");

                    addPayList();

                }

                break;
            case R.id.btn_weixin:

                imgWeixin.setImageResource(R.drawable.check_01);
                imgAli.setImageResource(R.drawable.check_02);

                paytype = 0;

                break;
            case R.id.btn_ali:

                imgWeixin.setImageResource(R.drawable.check_02);
                imgAli.setImageResource(R.drawable.check_01);

                paytype = 1;

                break;

            default:
                break;
        }
    }

    private void addPayList() {

        Resquest.addCouponTabAdd(handler2, "", "", "", guid, orderMoney, payCode, 2 + "");
    }

    private void payAli() {

        AliPayUtils aliPay = new AliPayUtils(context);

        aliPay.pay(aliHandler, shopName + "-学车郎陪练商品购买", guid, orderMoney, payCode);
    }

    private Handler aliHandler = new Handler() {
        public void handleMessage(Message msg) {

            PayResult payResult = new PayResult((String) msg.obj);

            String resultStatus = payResult.getResultStatus();

            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                showToast("支付成功");
                setResult(Activity.RESULT_OK);
                OrderPaySelectActivity.this.finish();

                for (int i = 0; i < AndroidApplication.activityList2.size(); i++) {

                    AndroidApplication.activityList2.get(i).finish();
                }

                AndroidApplication.activityList2.clear();


            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    showToast("支付结果确认中");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    showToast("支付失败");
                }
            }
        }
    };

    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            payAli();

                        }
                        if (Code.equals("8")) {


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");

                    break;
            }

            return true;
        }
    });

}
