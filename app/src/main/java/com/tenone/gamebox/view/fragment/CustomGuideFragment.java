package com.tenone.gamebox.view.fragment;

import android.graphics.Color;
import android.view.View;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.custom.RhombusRenderer;
import com.tenone.gamebox.view.custom.slidingtutorial.IndicatorOptions;
import com.tenone.gamebox.view.custom.slidingtutorial.TutorialOptions;
import com.tenone.gamebox.view.custom.slidingtutorial.TutorialSupportFragment;

public class CustomGuideFragment extends TutorialSupportFragment implements View.OnClickListener {
	private static final int TOTAL_PAGES = 3;

	@Override
	protected TutorialOptions provideTutorialOptions() {
		return  newTutorialOptionsBuilder(getContext())
				.setUseInfiniteScroll(true)
				.setPagesCount(TOTAL_PAGES)
				.setIndicatorOptions( IndicatorOptions.newBuilder(getContext())
						.setElementSizeRes(R.dimen.indicator_size)
						.setElementSpacingRes(R.dimen.indicator_spacing)
						.setElementColorRes(android.R.color.darker_gray)
						.setSelectedElementColor( Color.LTGRAY)
						.setRenderer( RhombusRenderer.create())
						.build())
				.setOnSkipClickListener(this)
				.build();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.st_fragment_presentation;
	}

	/**
	 * Called when a view has been clicked.
	 *
	 * @param v The view that was clicked.
	 */
	@Override
	public void onClick(View v) {
			
	}
}
