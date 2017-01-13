package com.kunzhuo.xuechelang.utils.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.kunzhuo.xuechelang.utils.ToastUtil;

/**
 * @author zhangQ
 * @date 2016-1-11
 * @description 支付宝支付工具
 */
public class AliPayUtils {

    // 商户PID
    public static final String PARTNER = "2088121028763594";
    // 商户收款账号
    public static final String SELLER = "xueche555@126.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANd54lHaJSZpiMjOV2oFil2GJb9WenL9/d0e/gNzV6kP/Ot5k9Dou6+wNr4eSj6SpGy6bF+Co5hg9naA7aYuQUS6MDeu1djdtdANK779YUOtVLqX8SOzMMcROIGchTXDwE98Hh7RCOQMRs8NQN/KWaDFcff8SUf7TPResb/GgDc7AgMBAAECgYEAx+82SpDJa8z8uKaNgbjGXGU+3T1WvSBj0CsTswvxSNOm8K+MsolgrXIqOVpNtaXHn4OTT0QenCNMTUTwPgQpVfI3J+7uWqe4UMNpodjDKKW9Zf8e3L7Q9PCQk/ygt0wtyCpOw32lLyqxZ0RJZkA4dv/qKTrPj8ALHpMp7yhlA/ECQQDsZf46AgcSueIqnHkBzGxtWfP1hqvWTTik+k//+dw4nJgCkqXN927OCSdyb6qAGxc//xNEfBuRUF1U3lmVhSdPAkEA6VfGd/VU3rc5A8yBGp7YivK4KtG+kpQCIua4NActmx2RouxvN9tOxAkOpV1AD3SE2u3GDtC9tsDopibPvSi2VQJBAOBReAe7XNLcKEye5gRa4phxPxnAjNZxuEp0a+1OPzZJAcWPOl5TaIWCEjh41aVyD4HenX3i9pXRWH7r7r3+9+kCQQCGd+3InVl+SyoRFV3lAFsbn4ogKuTexWqBwGGwod5XQG/36rahAsHeWLXW9+j9vxD8tP9o9EQPuBWaAk2+3WJNAkArx+4HhlD4TwXYY8PTE8jbrxMcV9tHJ9pTl64HqISZRnlKNfwx5pZRRiTnYvn6J8LBHgISatuNKxbI3+dt583U";

    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    private static final int SDK_PAY_FLAG = 1;


    private static final int SDK_CHECK_FLAG = 2;

    private Context mContext;

    public AliPayUtils(Context mContext) {
        this.mContext = mContext;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT)
                                    .show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(mContext, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
                            .show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     * @param subject   商品名称 名称
     * @param body      商品描述 guid
     * @param price     价格
     * @param outTradNo 订单号 6位随机数 时间格式 带三位毫秒
     */
    public void pay(final Handler payHandelr, String subject, String body, String price, String outTradNo) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(mContext)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    dialoginterface.dismiss();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo(subject, body, price, outTradNo);

        System.out.println(orderInfo + "订单信息1");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);

        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
//				mHandler.sendMessage(msg);
                payHandelr.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask((Activity) mContext);
        String version = payTask.getVersion();
        Toast.makeText(mContext, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price,
                               String outTradNo) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\""
                + "http://www.xuechelang.com/Index/Other/Uservice/notify_url.aspx" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public void onDestory() {
        mHandler.removeCallbacksAndMessages(null);
        mContext = null;
    }
}
