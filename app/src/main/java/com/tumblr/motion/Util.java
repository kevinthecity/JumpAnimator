package com.tumblr.motion;

import android.app.Activity;

public class Util {

	public static int getScreenWidth(final Activity activity) {
		if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null) {
			return -1;
		}
		return activity.getWindow().getDecorView().getMeasuredWidth();
	}

	public static int getScreenHeight(final Activity activity) {
		if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null) {
			return -1;
		}
		return activity.getWindow().getDecorView().getMeasuredHeight();
	}
}
