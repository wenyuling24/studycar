package com.kunzhuo.xuechelang.widget.emouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.Emo_Bean;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 编辑显示表情控件
 */
public class EmoticonsEditText extends EditText {

    JSONArray jsonArray;

    public EmoticonsEditText(Context context) {
        super(context);
    }

    public EmoticonsEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EmoticonsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            super.setText(replace(text), type);
        } else {
            super.setText(text, type);
        }
    }

    private Pattern buildPattern() {
        // List<String> cachemEmoticons = BaseApplication.mEmoticons;
        List<Emo_Bean> cachemEmoticons = AndroidApplication.mEmoticons;
        int mLength = cachemEmoticons.size();
        StringBuilder patternString = new StringBuilder(mLength * 3);
        patternString.append('(');
        for (int i = 0; i < mLength; i++) {
            String s = cachemEmoticons.get(i).getData();
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1,
                patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }

    /**
     * 从获取的title匹配\[BQ/[a-zA-Z0-9\u4e00-\u9fa5]+\]正则
     *
     * @return
     */

    private CharSequence replace(CharSequence text) {
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            Pattern pattern = buildPattern();
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {

                if (AndroidApplication.mEmoticonsId.containsKey(matcher.group())) {
                    int id = AndroidApplication.mEmoticonsId.get(matcher.group());
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            getResources(), id);
                    if (bitmap != null) {
                        Bitmap bitmap2 = null;
                        if (Build.MANUFACTURER.equalsIgnoreCase("bbk")
                                || Build.MANUFACTURER
                                .equalsIgnoreCase("huawei")) {
                            bitmap2 = BitmapUtil.getBitmap(bitmap, 50, 50);
                        } else if (Build.MANUFACTURER
                                .equalsIgnoreCase("samsung")) {
                            bitmap2 = BitmapUtil.getBitmap(bitmap, 80, 80);
                        } else
                            bitmap2 = BitmapUtil.getBitmap(bitmap, 70, 70);
                        ImageSpan span = new ImageSpan(getContext(), bitmap2);
                        builder.setSpan(span, matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            return builder;
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * 流转String
     *
     * @param inputStream
     * @return
     */
    public String getStringByRaw(InputStream inputStream) {
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

    /**
     * 把名称转换成url
     *
     * @param
     * @return
     */
    public String changeTitleToUrl(String url) {

        InputStream inputStream = getResources().openRawResource(R.raw.expressionjson);
        String emoJson = getStringByRaw(inputStream);

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(emoJson);

            jsonArray = jsonObject.getJSONArray("List");

            for (int i = 0; i < jsonArray.length(); i++) {

                Emo_Bean bean = new Emo_Bean();
                JSONObject object = jsonArray.optJSONObject(i);

                bean.setID(object.getInt("ID"));
                bean.setImgSrc(object.getString("imgSrc"));
                bean.setData(object.getString("data"));
                bean.setHttpImg(object.getString("httpImg"));

                if (url.equals(bean.getHttpImg() + bean.getImgSrc())) {
                    return bean.getData();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}