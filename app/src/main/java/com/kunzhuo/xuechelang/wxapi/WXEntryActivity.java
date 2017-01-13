package com.kunzhuo.xuechelang.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.kunzhuo.xuechelang.AndroidApplication;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2015/12/9.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private BaseResp resp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ContantsUtils.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "";

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (resp instanceof SendAuth.Resp) {
                    SendAuth.Resp newResp = (SendAuth.Resp) resp;
                    // 获取微信传回的code
                    if (newResp != null) {
                        AndroidApplication.setResp(newResp);
                    }
                }
                result = "操作成功";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:

                // 获取微信传回的code
                AndroidApplication.setResp(null);

                result = "操作取消";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();

                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "操作拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "操作返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }
}
