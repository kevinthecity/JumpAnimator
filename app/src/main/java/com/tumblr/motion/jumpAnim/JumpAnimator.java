package com.tumblr.motion.jumpAnim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.tumblr.motion.jumpAnim.path.AnimatorPath;
import com.tumblr.motion.jumpAnim.path.PathEvaluator;
import com.tumblr.motion.jumpAnim.path.PathPoint;

import java.lang.ref.WeakReference;

/**
 * Helper class that takes a subclass of {@link JumpAnimator.Jump}
 * and animates an avatar from and to a passed location on the screen.
 */
public class JumpAnimator {

	public static final String INTENT_JUMP_END = "animate_home";

	private WeakReference<Context> mContextRef;
	private WeakReference<Jump> mJump;

	private ViewGroup mDecorView;
	private View mViewAnimProxy;

	/**
	 * Creates a new JumpAnimator.
	 *
	 * @param context
	 * 		The context, used for sending local broadcasts.
	 * @param decorView
	 * 		The decorview to add and remove views to.
	 * @param jump
	 * 		The jump to animate with.
	 */
	public JumpAnimator(Context context, View decorView, Jump jump) {
		mContextRef = new WeakReference<Context>(context);
		mJump = new WeakReference<Jump>(jump);

		if (decorView instanceof ViewGroup) {
			mDecorView = (ViewGroup) decorView;
		}
	}

	/**
	 * Starts the jump animation that this animator was initialized with.
	 * <p/>
	 * NOTE: The arcing mechanic is determined within this method and cannot be manipulated via API.
	 */
	public void animate() {

		final Context context = getOwnerContext();
		final Jump jump = getJump();

		if (context != null && jump != null && mDecorView != null) {

			final ImageView temporalImageView = new ImageView(context);

			if (jump.getJumpDrawable() != null) {
				temporalImageView.setImageDrawable(jump.getJumpDrawable());
			}

			final int size = (int) (context.getResources().getDisplayMetrics().density * 33f);
			final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size, size);

			// Add the view for drawing
			mDecorView.addView(temporalImageView, params);

			// Wrap the animatedAvatar so the proxy can perform animations on it
			mViewAnimProxy = temporalImageView;

			final int[] startLoc = jump.getStartLoc();
			final int[] endLoc = jump.getEndLoc();

			final AnimatorPath path = new AnimatorPath();

			// Must set the path start location
			path.moveTo(startLoc[0], startLoc[1]);

			// start x/y, midpoint x/y, end x/y
			path.curveTo(startLoc[0], startLoc[1], 100, 800, endLoc[0],
					endLoc[1] - temporalImageView.getHeight());

			final AnimatorSet set = new AnimatorSet();
			ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(temporalImageView, "alpha", 1f, 0f);
			fadeAnimator.setStartDelay(350);
			fadeAnimator.setDuration(150);
			set.playTogether(fadeAnimator);

			// Set up the animation
			final ObjectAnimator pathAnimator = ObjectAnimator.ofObject(this, "viewLoc",
					new PathEvaluator(), path.getPoints().toArray());
			pathAnimator.setDuration(500);
			set.playTogether(pathAnimator);

			set.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					onAnimationCancel(animation);

					final Intent intent = new Intent(INTENT_JUMP_END);
					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// If the animation is canceled for some reason, ensure we remove the image
					if (mDecorView != null) {
						mDecorView.removeView(temporalImageView);
					}
				}
			});

			set.start();
		}
	}

	/**
	 * @return The context held in the weak reference, or null
	 */
	private Context getOwnerContext() {
		if (mContextRef != null) {
			return mContextRef.get();
		}
		return null;
	}

	/**
	 * @return The jump held in the weak reference, or null.
	 */
	private Jump getJump() {
		if (mJump != null) {
			return mJump.get();
		}
		return null;
	}

	/**
	 * We need this setter to translate between the information the animator
	 * produces (a new "PathPoint" describing the current animated location)
	 * and the information that the button requires (an xy location). The
	 * setter will be called by the ObjectAnimator given the 'viewLoc'
	 * property string.
	 */
	public void setViewLoc(PathPoint newLoc) {
		mViewAnimProxy.setTranslationX(newLoc.mX);
		mViewAnimProxy.setTranslationY(newLoc.mY);
	}

	/**
	 * Interface for providing methods necessary to allow the {@link JumpAnimator} to animate.
	 */
	public interface Jump {

		/**
		 * @return The drawable you want to animate with.
		 */
		public Drawable getJumpDrawable();

		/**
		 * @return The start location of where you want the avatar to animate from.
		 */
		public int[] getStartLoc();

		/**
		 * @return The end location of where you want the avatar to animate to
		 */
		public int[] getEndLoc();
	}
}
