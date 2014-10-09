package com.tumblr.motion;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

public class Util {

	public static ImageView getHomeIcon(Activity activity) {
		// Defensive
		if (activity == null || activity.getActionBar() == null) {
			return null;
		}

		ImageView homeIcon = null;
		final Resources res = Resources.getSystem();

		// Hack #1: search for the "android.id.home" field. (This works for <= 4.4)
		if (res != null) {
			int actionBarUpId = res.getIdentifier("home", "id", "android");
			View upView = activity.findViewById(actionBarUpId);
			if (upView instanceof ImageView) {
				homeIcon = (ImageView) upView;
			}
		}

		return homeIcon;
	}

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
