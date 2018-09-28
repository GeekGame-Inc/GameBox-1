package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

	private Context mContext;
	List<String> imgurls;
	ImageClickListener imgClickListener;

	public ImageClickListener getImgClickListener() {
		return imgClickListener;
	}

	public void setImgClickListener(ImageClickListener imgClickListener) {
		this.imgClickListener = imgClickListener;
	}

	public ViewPagerAdapter(Context context, List<String> imgurls) {
		this.mContext = context;
		this.imgurls = imgurls;
	}

	@Override
	public int getCount() {
		return imgurls.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		view.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		ImageView imageview = new ImageView(mContext);
		imageview.setScaleType(ScaleType.CENTER_CROP);
		ImageLoadUtils
				.loadNormalImg(imageview, mContext, imgurls.get(position));
		view.addView(imageview);
		imageview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (imgClickListener != null) {
					imgClickListener.imageClick(position);
				}
			}
		});
		return imageview;
	}

	public interface ImageClickListener {
		void imageClick(int position);
	}

	// public void updateItemView(ListView listView, int position) {
	// int index = position - listView.getFirstVisiblePosition();
	// if (index >= 0 && index < listView.getChildCount()) {
	// View itemView = listView.getChildAt(index);
	// getView(position, itemView, listView);
	// }
	// }
}