package com.tenone.gamebox.view.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;

@TargetApi(Build.VERSION_CODES.M)
public class MaterialAimUtils {
	private int animDuration;
	private int slideGrivaty;
	private Visibility materialVisibility;

	private MaterialAimUtils(Builder builder) {
		this.animDuration = builder.animDuration;
		MaterialAimType materialAimType = builder.materialAimType;
		this.slideGrivaty = builder.slideGrivaty;
		switch (materialAimType) {
		case SLIDE:
			materialVisibility = buildMySlideInstance();
			break;
		case FADE:
			materialVisibility = buildMyFadeInstance();
			break;
		case EXPLODE:
			materialVisibility = buildMyExplodeInstance();
			break;
		}
	}

	public static class Builder {
		private int animDuration;
		private MaterialAimType materialAimType;
		private int slideGrivaty;

		public Builder animDuration(int animDuration) {
			this.animDuration = animDuration;
			return this;
		}

		public Builder materialAimType(MaterialAimType materialAimType) {
			this.materialAimType = materialAimType;
			return this;
		}

		public Builder slideGrivaty(int slideGrivaty) {
			this.slideGrivaty = slideGrivaty;
			return this;
		}

		public MaterialAimUtils build() {
			return new MaterialAimUtils(this);
		}
	}

	public enum MaterialAimType {
		SLIDE(), FADE(), EXPLODE();
		MaterialAimType() {
		}
	}

	private Slide buildMySlideInstance() {
		Slide slide = new Slide();
		slide.setDuration(animDuration);
		slide.setSlideEdge(slideGrivaty);
		return slide;
	}

	private Fade buildMyFadeInstance() {
		Fade fade = new Fade();
		fade.setDuration(animDuration);
		return fade;
	}

	private Explode buildMyExplodeInstance() {
		Explode explode = new Explode();
		explode.setDuration(animDuration);
		return explode;
	}

	public static void startActivityWithMaterialAim(Activity currentActivity,
			Class<?> targetActivity) {
		Intent intent = new Intent(currentActivity, targetActivity);
		currentActivity.startActivity(intent, ActivityOptions
				.makeSceneTransitionAnimation(currentActivity).toBundle());
	}

	public void setEixtMaterial(Activity activity) {
		activity.getWindow().setExitTransition(materialVisibility);

	}

	public void setEnterMaterial(Activity activity) {
		activity.getWindow().setEnterTransition(materialVisibility);
	}
}
