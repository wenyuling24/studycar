package com.kunzhuo.xuechelang;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.Emo_Bean;
import com.kunzhuo.xuechelang.model.PlayVideo_Bean;
import com.kunzhuo.xuechelang.network.ImageLoaderConfig;
import com.kunzhuo.xuechelang.network.SystemBase;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseResp;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by waaa on 2016/8/30.
 */
public class AndroidApplication extends Application {
    final static String TAG = "BaseApplication";

    public static AndroidApplication instance;
    // 程序是否需要检查更新
    public boolean FLAG_CHECK_UPDATE = true;
    // 程序是否需要定位
    public boolean FLAG_LOCATION = true;

    public static Map<String, Long> map2;// 用来存放倒计时的时间

    public static List<Activity> activityList = new ArrayList<Activity>(); // 临时存放activity数据
    public static List<Activity> activityList2 = new ArrayList<Activity>();
    public static List<Activity> activityList3 = new ArrayList<Activity>();

    /**
     * mEmoticons 表情
     **/
    public static Map<String, Integer> mEmoticonsId;
    // public static List<String> mEmoticons;
    public static List<Emo_Bean> mEmoticons;
    public static List<String> mEmoticons_Zem;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

 /*imageloader工具初始化*/
        ImageLoaderConfig
                .initImageLoader(this, SystemBase.IMAGE_CACHE_PATH);

        InputStream inputStream = getResources().openRawResource(R.raw.expressionjson);

        String emoJson = getStringByRaw(inputStream);

        mEmoticonsId = new HashMap<>();
        mEmoticons = new ArrayList<>();
        mEmoticons_Zem = new ArrayList<>();

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(emoJson); // 将json字符串转换成JsonElement
        JsonArray jsonArray = jsonElement.getAsJsonArray(); // 将JsonElement转换成JsonArray
        Iterator it = jsonArray.iterator(); // Iterator处理
        while (it.hasNext()) { // 循环
            JsonElement e = (JsonElement) it.next(); // 提取JsonElement
            String json = e.toString(); // JsonElement转换成String
            Emo_Bean bean = gson.fromJson(json, Emo_Bean.class); // String转化成JavaBean

            int emoticonsId = getResources().getIdentifier("p" + bean.getID(),
                    "drawable", getPackageName());

            mEmoticons.add(bean);
            mEmoticons_Zem.add(bean.getData());
            mEmoticonsId.put(bean.getData(), emoticonsId);
        }

        try {
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static AndroidApplication getInstance() {
        return instance;
    }

    private static BaseResp resp;

    public static BaseResp getResp() {
        return resp;
    }

    public static void setResp(BaseResp resp) {
        AndroidApplication.resp = resp;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    /**
     * 流转String
     *
     * @param inputStream
     * @return
     */
    public static String getStringByRaw(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}