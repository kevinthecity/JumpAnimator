package com.tumblr.motion;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.tumblr.motion.jumpAnim.JumpAnimator;

public class MainActivity extends Activity {

	private JumpAnimator mJumpAnimator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize the JumpAnimator
		mJumpAnimator = new JumpAnimator(this, getWindow().getDecorView(), new HomeIconJump(this));
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mAnimationReceiver, new IntentFilter(JumpAnimator.INTENT_JUMP_END));
	}

	/**
	 * OnClick from XML.
	 */
	public void onJumpClick(View view) {
		if (mJumpAnimator != null) {
			mJumpAnimator.animate();
		}
	}

	/**
	 * Receive Jump animation broadcasts.
	 */
	private BroadcastReceiver mAnimationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			final ImageView homeIcon = HomeIconJump.getHomeIcon(MainActivity.this);
			if (homeIcon != null) {

				ScaleAnimation scaleAnim = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
						homeIcon.getWidth() / 2, homeIcon.getHeight() / 2);
				scaleAnim.setDuration(300);
				scaleAnim.setRepeatMode(Animation.REVERSE);
				scaleAnim.setRepeatCount(1);
				homeIcon.startAnimation(scaleAnim);
			}
		}
	};
}
