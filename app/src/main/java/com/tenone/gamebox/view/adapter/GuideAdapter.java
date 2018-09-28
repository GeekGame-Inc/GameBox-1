package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class GuideAdapter extends PagerAdapter {
	private List<View> views;
	private int[] rIds;
	private Context context;

	public GuideAdapter(List<View> views, int[] rIds, Context context) {
		this.views = views;
		this.rIds = rIds;
		this.context = context;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		Log.i( "GuideAdapter", "isViewFromObject" );
		return view == object;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView( views.get( position ) );
		views.get( position ).setBackground( null );
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		Log.i( "GuideAdapter", "instantiateItem position is " + position );
		View view = views.get( position );
		ImageLoadUtils.loadDraImg( context, (ImageView) view, rIds[position % rIds.length] );
		//	view.setBackground( ContextCompat.getDrawable( context, rIds[position] ) );
		container.addView( view );
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		view.setLayoutParams( params );
		return view;
	}

	/**
	 * Called when a change in the shown pages is going to start being made.
	 *
	 * @param container The containing View which is displaying this adapter's
	 *                  page views.
	 */
	@Override
	public void startUpdate(@NonNull ViewGroup container) {
		super.startUpdate( container );
	}

	/**
	 * Called when the a change in the shown pages has been completed.  At this
	 * point you must ensure that all of the pages have actually been added or
	 * removed from the container as appropriate.
	 *
	 * @param container The containing View which is displaying this adapter's
	 *                  page views.
	 */
	@Override
	public void finishUpdate(@NonNull ViewGroup container) {
		super.finishUpdate( container );
	}
}
