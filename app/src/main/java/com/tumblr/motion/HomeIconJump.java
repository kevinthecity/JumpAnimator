package com.tumblr.motion;

import android.graphics.drawable.Drawable;
import com.tumblr.motion.jumpAnim.JumpAnimator;

import java.util.Random;

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
			Util.getHomeIcon(mActivity).getLocationOnScreen(endLoc);
		}
		return endLoc;
	}
}
