/**
 * 
 */
package com.kunzhuo.xuechelang.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kunzhuo.xuechelang.R;

public class ToastUtil {

	// public static void show(Context context, String info) {
	// Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	// }
	//
	// public static void show(Context context, int info) {
	// Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	// }

	public static void show(final Context context, final String toast) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();
	}

	public static void show(final Context context, final int toast) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();
	}

	/**
	 * 显示自定Toast
	 * @param context
	 * @param msg
	 */
	public static void showToastMsgShort(Context context, String msg) {
		Toast toast = new Toast(context);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.toast_view, null);
		TextView tv = (TextView) view.findViewById(R.id.toast_msg);
		tv.setText(msg);
		TextPaint tp = tv.getPaint();
		tp.setFakeBoldText(true);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
}
