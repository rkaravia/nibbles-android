package nibbles.ui;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class Notification {
	private static final int DURATION = 1000;

	public static void show(final View view) {
		Animation anim = new Animation() {
		};
		anim.setDuration(DURATION);
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.INVISIBLE);
			}
		});
		view.setVisibility(View.VISIBLE);
		view.startAnimation(anim);
	}
}
