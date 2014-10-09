package com.tumblr.motion;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.tumblr.motion.jumpAnim.JumpAnimator;

import java.util.Random;

/**
 * Example of how to use the JumpAnimator to animate the Home icon.
 */
public class HomeIconJump implements JumpAnimator.Jump {

	private MainActivity mActivity;

	public HomeIconJump(MainActivity context) {
		mActivity = context;
	}

	@Override
	public Drawable getJumpDrawable() {
		if (mActivity == null) {
			return null;
		}
		return mActivity.getResources().getDrawable(R.drawable.ic_launcher);
	}

	@Override
	public int[] getStartLoc() {
		final int[] startLoc = new int[2];
		if (mActivity != null) {
			Random random = new Random();
			startLoc[0] = random.nextInt(Util.getScreenWidth(mActivity));
			startLoc[1] = random.nextInt(Util.getScreenHeight(mActivity));
		}
		return startLoc;
	}

	@Override
	public int[] getEndLoc() {
		final int[] endLoc = new int[2];
		if (mActivity != null) {
			getHomeIcon(mActivity).getLocationOnScreen(endLoc);
		}
		return endLoc;
	}

	/**
	 * Get the home icon from an Activity.
	 *
	 * @param activity
	 * @return
	 */
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
}
