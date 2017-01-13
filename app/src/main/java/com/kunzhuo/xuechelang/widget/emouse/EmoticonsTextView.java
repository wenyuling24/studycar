package com.kunzhuo.xuechelang.widget.emouse;


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

import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.model.Emo_Bean;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;

public class EmoticonsTextView extends HandyTextView {

	public EmoticonsTextView(Context context) {
		super(context);
	}

	public EmoticonsTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EmoticonsTextView(Context context, AttributeSet attrs) {
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
}
