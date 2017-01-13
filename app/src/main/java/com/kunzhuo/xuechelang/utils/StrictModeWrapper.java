package com.kunzhuo.xuechelang.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;

/**
 * 严苛模式
 * 
 * @author SB
 * 
 */
public class StrictModeWrapper {

	public static void init(Context context) {
		// check if android:debuggable is set to true
		int appFlags = context.getApplicationInfo().flags;
		if ((appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());

			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog() // .penaltyDeath()
					.build());
		}
	}
}
