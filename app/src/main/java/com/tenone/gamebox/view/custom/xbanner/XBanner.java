package com.tenone.gamebox.view.custom.xbanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.custom.xbanner.transformers.BasePageTransformer;
import com.tenone.gamebox.view.custom.xbanner.transformers.Transformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({ "NewApi", "RtlHardcoded" })
public class XBanner extends RelativeLayout implements
		XBannerViewPager.AutoPlayDelegate, ViewPager.OnPageChangeListener {
	private static final int RMP = LayoutParams.MATCH_PARENT;
	private static final int RWC = LayoutParams.WRAP_CONTENT;
	private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;

	private static final int VEL_THRESHOLD = 400;
	private int mPageScrollPosition;
	private float mPageScrollPositionOffset;

	private ViewPager.OnPageChangeListener mOnPageChangeListener;
	private OnItemClickListener mOnItemClickListener;

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;

	private AutoSwitchTask mAutoSwitchTask;

	private LinearLayout mPointRealContainerLl;

	private XBannerViewPager mViewPager;

	private Transformer mTransformer;
	private int mPointLeftRightPading;
	private int mPointTopBottomPading;
	private int mPointContainerLeftRightPadding;
	private List<? extends Object> mModels;
	private List<View> mLessViews;
	private List<View> mViews;
	private boolean mIsOneImg = false;
	private boolean mIsAutoPlay = true;
	private boolean mIsAutoPlaying = false;
	private int mAutoPalyTime = 5000;
	private boolean mIsAllowUserScroll = true;
	private int mSlideScrollMode = OVER_SCROLL_ALWAYS;
	private int mPointPosition = CENTER;
	private Drawable mPointNoraml;
	private Drawable mPointSelected;
	private int mPointDrawableResId = R.drawable.selector_banner_point;
	private Drawable mPointContainerBackgroundDrawable;
	private LayoutParams mPointRealContainerLp;
	private TextView mTipTv;
	private List<String> mTipData;
	private int mTipTextColor;
	private boolean mPointsIsVisible = true;
	private int mTipTextSize;

	public static final int TOP = 10;
	public static final int BOTTOM = 12;

	private int mPointContainerPosition = BOTTOM;

	private XBannerAdapter mAdapter;
	private LayoutParams mPointContainerLp;
	private boolean mIsNumberIndicator = false;
	private TextView mNumberIndicatorTv;
	private Drawable mNumberIndicatorBackground;
	private boolean isShowIndicatorOnlyOne = false;
	private int mPageChangeDuration = 1000;

	public void setmAdapter(XBannerAdapter mAdapter) {
		this.mAdapter = mAdapter;
	}

	public XBanner(Context context) {
		this(context, null);
	}

	public XBanner(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XBanner(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initDefaultAttrs(context);
		initCustomAttrs(context, attrs);
		initView(context);
	}

	private void initDefaultAttrs(Context context) {
		mAutoSwitchTask = new AutoSwitchTask(this);
		mPointLeftRightPading = XBannerUtil.dp2px(context, 3);
		mPointTopBottomPading = XBannerUtil.dp2px(context, 9);
		mPointContainerLeftRightPadding = XBannerUtil.dp2px(context, 5);
		mTipTextSize = XBannerUtil.sp2px(context, 10);
		mTipTextColor = Color.WHITE;
		mPointContainerBackgroundDrawable = new ColorDrawable(
				Color.parseColor("#44aaaaaa"));
	}

	private void initCustomAttrs(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.XBanner);
		if (typedArray != null) {
			mIsAutoPlay = typedArray.getBoolean(R.styleable.XBanner_isAutoPlay,
					true);
			mAutoPalyTime = typedArray.getInteger(
					R.styleable.XBanner_AutoPlayTime, 5000);
			mPointsIsVisible = typedArray.getBoolean(
					R.styleable.XBanner_pointsVisibility, true);
			mPointPosition = typedArray.getInt(
					R.styleable.XBanner_pointsPosition, CENTER);
			mPointContainerLeftRightPadding = typedArray.getDimensionPixelSize(
					R.styleable.XBanner_pointContainerLeftRightPadding,
					mPointContainerLeftRightPadding);
			mPointLeftRightPading = typedArray.getDimensionPixelSize(
					R.styleable.XBanner_pointLeftRightPadding,
					mPointLeftRightPading);
			mPointTopBottomPading = typedArray.getDimensionPixelSize(
					R.styleable.XBanner_pointTopBottomPadding,
					mPointTopBottomPading);
			mPointContainerPosition = typedArray.getInt(
					R.styleable.XBanner_pointContainerPosition, BOTTOM);
			mPointContainerBackgroundDrawable = typedArray
					.getDrawable(R.styleable.XBanner_pointsContainerBackground);
			mPointNoraml = typedArray
					.getDrawable(R.styleable.XBanner_pointNormal);
			mPointSelected = typedArray
					.getDrawable(R.styleable.XBanner_pointSelect);
			mTipTextColor = typedArray.getColor(
					R.styleable.XBanner_tipTextColor, mTipTextColor);
			mTipTextSize = typedArray.getDimensionPixelSize(
					R.styleable.XBanner_tipTextSize, mTipTextSize);
			mIsNumberIndicator = typedArray.getBoolean(
					R.styleable.XBanner_isShowNumberIndicator,
					mIsNumberIndicator);
			mNumberIndicatorBackground = typedArray
					.getDrawable(R.styleable.XBanner_numberIndicatorBacgroud);
			isShowIndicatorOnlyOne = typedArray.getBoolean(
					R.styleable.XBanner_isShowIndicatorOnlyOne,
					isShowIndicatorOnlyOne);
			mPageChangeDuration = typedArray
					.getInt(R.styleable.XBanner_pageChangeDuration,
							mPageChangeDuration);
			typedArray.recycle();
		}
	}

	@SuppressWarnings( "deprecation" )
	private void initView(Context context) {
		RelativeLayout pointContainerRl = new RelativeLayout(context);
		if (Build.VERSION.SDK_INT >= 16) {
			pointContainerRl.setBackground(mPointContainerBackgroundDrawable);
		} else {
			pointContainerRl
					.setBackgroundDrawable(mPointContainerBackgroundDrawable);
		}

		pointContainerRl.setPadding(mPointContainerLeftRightPadding,
				mPointTopBottomPading, mPointContainerLeftRightPadding,
				mPointTopBottomPading);

		mPointContainerLp = new LayoutParams(RMP, RWC);
		mPointContainerLp.addRule(mPointContainerPosition);
		addView(pointContainerRl, mPointContainerLp);
		mPointRealContainerLp = new LayoutParams(RWC, RWC);
		if (mIsNumberIndicator) {
			mNumberIndicatorTv = new TextView(context);
			mNumberIndicatorTv.setId(R.id.xbanner_pointId);
			mNumberIndicatorTv.setGravity(Gravity.CENTER_VERTICAL);
			mNumberIndicatorTv.setSingleLine(true);
			mNumberIndicatorTv.setEllipsize(TextUtils.TruncateAt.END);
			mNumberIndicatorTv.setTextColor(mTipTextColor);
			mNumberIndicatorTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					mTipTextSize);
			mNumberIndicatorTv.setVisibility(View.INVISIBLE);
			if (mNumberIndicatorBackground != null) {
				if (Build.VERSION.SDK_INT >= 16) {
					mNumberIndicatorTv
							.setBackground(mNumberIndicatorBackground);
				} else {
					mNumberIndicatorTv
							.setBackgroundDrawable(mNumberIndicatorBackground);
				}
			}
			pointContainerRl.addView(mNumberIndicatorTv, mPointRealContainerLp);
		} else {
			mPointRealContainerLl = new LinearLayout(context);
			mPointRealContainerLl.setOrientation(LinearLayout.HORIZONTAL);
			mPointRealContainerLl.setId(R.id.xbanner_pointId);
			pointContainerRl.addView(mPointRealContainerLl,
					mPointRealContainerLp);
		}

		if (mPointRealContainerLl != null) {
			if (mPointsIsVisible) {
				mPointRealContainerLl.setVisibility(View.VISIBLE);
			} else {
				mPointRealContainerLl.setVisibility(View.GONE);
			}
		}

		LayoutParams tipLp = new LayoutParams(RMP, RWC);
		tipLp.addRule(CENTER_VERTICAL);
		mTipTv = new TextView(context);
		mTipTv.setGravity(Gravity.CENTER_VERTICAL);
		mTipTv.setSingleLine(true);
		mTipTv.setEllipsize(TextUtils.TruncateAt.END);
		mTipTv.setTextColor(mTipTextColor);
		mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTipTextSize);
		pointContainerRl.addView(mTipTv, tipLp);
		if (CENTER == mPointPosition) {
			mPointRealContainerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			tipLp.addRule(RelativeLayout.LEFT_OF, R.id.xbanner_pointId);
		} else if (LEFT == mPointPosition) {
			mPointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			tipLp.addRule(RelativeLayout.RIGHT_OF, R.id.xbanner_pointId);
			mTipTv.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		} else if (RIGHT == mPointPosition) {
			mPointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			tipLp.addRule(RelativeLayout.LEFT_OF, R.id.xbanner_pointId);
		}
	}

	public void setData(List<View> views, List<? extends Object> data,
			List<String> tips) {
		if (mIsAutoPlay && views.size() < 3 && mLessViews == null) {
			mIsAutoPlay = false;
		}
		this.mModels = data;
		this.mTipData = tips;
		this.mViews = views;

		mIsOneImg = data.size() <= 1;
		if (data != null && !data.isEmpty())
			initViewPager();
	}

	public void setData(@LayoutRes int layoutResId,
			List<? extends Object> models, List<String> tips) {
		mViews = new ArrayList<View>();
		for (int i = 0; i < models.size(); i++) {
			mViews.add(View.inflate(getContext(), layoutResId, null));
		}
		if (mIsAutoPlay && mViews.size() < 3) {
			mLessViews = new ArrayList<View>(mViews);
			mLessViews.add(View.inflate(getContext(), layoutResId, null));
			if (mLessViews.size() == 2) {
				mLessViews.add(View.inflate(getContext(), layoutResId, null));
			}
		}
		setData(mViews, models, tips);
	}

	public void setData(List<? extends Object> models, List<String> tips) {
		setData(R.layout.xbanner_item_image, models, tips);
	}

	public void setPointsIsVisible(boolean isVisible) {
		if (null != mPointRealContainerLl) {
			if (isVisible) {
				mPointRealContainerLl.setVisibility(View.VISIBLE);
			} else {
				mPointRealContainerLl.setVisibility(View.GONE);
			}
		}
	}

	public void setPoinstPosition(int position) {
		if (CENTER == position) {
			mPointRealContainerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		} else if (LEFT == position) {
			mPointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		} else if (RIGHT == position) {
			mPointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
	}

	public void setmPointContainerPosition(int position) {
		if (BOTTOM == position) {
			mPointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		} else if (TOP == position) {
			mPointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		}
	}

	@SuppressWarnings( "deprecation" )
	private void initViewPager() {
		if (mViewPager != null && this.equals(mViewPager.getParent())) {
			removeView(mViewPager);
			mViewPager = null;
		}
		mViewPager = new XBannerViewPager(getContext());
		if (isShowIndicatorOnlyOne || (!isShowIndicatorOnlyOne && !mIsOneImg)) {
			addPoints();
		}
		mViewPager.setAdapter(new XBannerPageAdapter());
		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOverScrollMode(mSlideScrollMode);
		mViewPager.setIsAllowUserScroll(mIsAllowUserScroll);
		setPageChangeDuration(mPageChangeDuration);
		addView(mViewPager, 0, new LayoutParams(RMP, RMP));
		if (!mIsOneImg && mIsAutoPlay) {
			mViewPager.setAutoPlayDelegate(this);
			int zeroItem = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2)
					% getRealCount();
			mViewPager.setCurrentItem(zeroItem, false);
			startAutoPlay();
		} else {
			switchToPoint(0);
		}
	}

	public int getRealCount() {
		return mModels == null ? 0 : mModels.size();
	}

	public XBannerViewPager getViewPager() {
		return mViewPager;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		mPageScrollPosition = position;
		mPageScrollPositionOffset = positionOffset;

		if (mTipTv != null && mTipData != null) {
			if (positionOffset > .5) {
				mTipTv.setText(mTipData.get((position + 1) % mTipData.size()));
				ViewHelper.setAlpha(mTipTv, positionOffset);
			} else {
				mTipTv.setText(mTipData.get(position % mTipData.size()));
				ViewHelper.setAlpha(mTipTv, 1 - positionOffset);
			}
		}

		if (null != mOnPageChangeListener)
			mOnPageChangeListener.onPageScrolled(position % getRealCount(),
					positionOffset, positionOffsetPixels);
	}

	@Override
	public void onPageSelected(int position) {
		position = position % getRealCount();
		switchToPoint(position);

		if (mOnPageChangeListener != null)
			mOnPageChangeListener.onPageSelected(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (mOnPageChangeListener != null)
			mOnPageChangeListener.onPageScrollStateChanged(state);

	}

	@Override
	public void handleAutoPlayActionUpOrCancel(float xVelocity) {
		assert mViewPager != null;
		if (mPageScrollPosition < mViewPager.getCurrentItem()) {
			if (xVelocity > VEL_THRESHOLD
					|| (mPageScrollPositionOffset < 0.7f && xVelocity > -VEL_THRESHOLD)) {
				mViewPager.setBannerCurrentItemInternal(mPageScrollPosition);
			} else {
				mViewPager
						.setBannerCurrentItemInternal(mPageScrollPosition + 1);
			}
		} else {
			if (xVelocity < -VEL_THRESHOLD
					|| (mPageScrollPositionOffset > 0.3f && xVelocity < VEL_THRESHOLD)) {
				mViewPager
						.setBannerCurrentItemInternal(mPageScrollPosition + 1);
			} else {
				mViewPager.setBannerCurrentItemInternal(mPageScrollPosition);
			}
		}
	}

	private class XBannerPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mIsOneImg) {
				return 1;
			}
			return mIsAutoPlay ? Integer.MAX_VALUE : getRealCount();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			final int realPosition = position % getRealCount();
			View view = null;

			if (mLessViews == null) {
				view = mViews.get(realPosition);
			} else {
				view = mLessViews.get(position % mLessViews.size());
			}

			if (container.equals(view.getParent())) {
				container.removeView(view);
			}

			if (mOnItemClickListener != null) {
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mOnItemClickListener.onItemClick(XBanner.this,
								realPosition);
					}
				});
			}

			if (null != mAdapter && mModels.size() != 0) {
				mAdapter.loadBanner(XBanner.this, mModels == null ? null
						: mModels.get(realPosition), view, realPosition);
			}

			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	private void addPoints() {
		if (mPointRealContainerLl != null) {
			mPointRealContainerLl.removeAllViews();
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					XBannerUtil.dp2px(getContext(), 15), LWC);
			lp.setMargins(mPointLeftRightPading, mPointTopBottomPading,
					mPointLeftRightPading, mPointTopBottomPading);
			for (int i = 0; i < getRealCount(); i++) {
				ImageView imageView = new ImageView(getContext());
				imageView.setLayoutParams(lp);
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
				if (mPointNoraml != null && mPointSelected != null) {
					imageView.setImageDrawable(XBannerUtil.getSelector(
							mPointNoraml, mPointSelected));
				} else {
					imageView.setImageResource(mPointDrawableResId);
				}
				mPointRealContainerLl.addView(imageView);
			}
		}

		if (mNumberIndicatorTv != null) {
			if (isShowIndicatorOnlyOne
					|| (!isShowIndicatorOnlyOne && !mIsOneImg)) {
				mNumberIndicatorTv.setVisibility(View.VISIBLE);
			} else {
				mNumberIndicatorTv.setVisibility(View.INVISIBLE);
			}
		}
	}
	private void switchToPoint(final int currentPoint) {
		if (mPointRealContainerLl != null & mModels != null
				&& getRealCount() > 1) {
			for (int i = 0; i < mPointRealContainerLl.getChildCount(); i++) {
				mPointRealContainerLl.getChildAt(i).setEnabled(false);
			}
			mPointRealContainerLl.getChildAt(currentPoint).setEnabled(true);
		}

		if (mTipTv != null && mTipData != null) {
			mTipTv.setText(mTipData.get(currentPoint));
		}

		if (mNumberIndicatorTv != null
				&& mViews != null
				&& (isShowIndicatorOnlyOne || (!isShowIndicatorOnlyOne && !mIsOneImg))) {
			mNumberIndicatorTv
					.setText((currentPoint + 1) + "/" + mViews.size());
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mIsAutoPlay && !mIsOneImg) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				stopAutoPlay();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				startAutoPlay();
				break;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	public void startAutoPlay() {
		if (mIsAutoPlay && !mIsAutoPlaying) {
			mIsAutoPlaying = true;
			postDelayed(mAutoSwitchTask, mAutoPalyTime);
		}
	}

	public void stopAutoPlay() {
		if (mIsAutoPlay && mIsAutoPlaying) {
			mIsAutoPlaying = false;
			removeCallbacks(mAutoSwitchTask);
		}
	}

	public void setOnPageChangeListener(
			ViewPager.OnPageChangeListener onPageChangeListener) {
		mOnPageChangeListener = onPageChangeListener;
	}

	public void setSlideScrollMode(int slideScrollMode) {
		mSlideScrollMode = slideScrollMode;
		if (null != mViewPager) {
			mViewPager.setOverScrollMode(slideScrollMode);
		}
	}

	public void setAllowUserScrollable(boolean allowUserScrollable) {
		mIsAllowUserScroll = allowUserScrollable;
		if (null != mViewPager) {
			mViewPager.setIsAllowUserScroll(allowUserScrollable);
		}
	}

	public void setmAutoPlayAble(boolean mAutoPlayAble) {
		this.mIsAutoPlay = mAutoPlayAble;
	}

	public void setmAutoPalyTime(int mAutoPalyTime) {
		this.mAutoPalyTime = mAutoPalyTime;
	}

	public void setPageTransformer(Transformer transformer) {
		if (transformer != null && mViewPager != null) {
			mTransformer = transformer;
			mViewPager.setPageTransformer(true,
					BasePageTransformer.getPageTransformer(mTransformer));
		}
	}

	public void setCustomPageTransformer(ViewPager.PageTransformer transformer) {
		if (transformer != null && mViewPager != null) {
			mViewPager.setPageTransformer(true, transformer);
		}
	}

	public void setPageChangeDuration(int duration) {
		if (mViewPager != null) {
			mViewPager.setScrollDuration(duration);
		}
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (VISIBLE == visibility) {
			startAutoPlay();
		} else if (INVISIBLE == visibility) {
			stopAutoPlay();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopAutoPlay();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		startAutoPlay();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// requestFocusFromTouch();
		requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}

	private static class AutoSwitchTask implements Runnable {
		private final WeakReference<XBanner> mXBanner;

		private AutoSwitchTask(XBanner mXBanner) {
			this.mXBanner = new WeakReference<XBanner>(mXBanner);
		}

		@Override
		public void run() {
			XBanner banner = mXBanner.get();
			if (banner != null) {
				if (banner.mViewPager != null) {
					int currentItem = banner.mViewPager.getCurrentItem() + 1;
					banner.mViewPager.setCurrentItem(currentItem);
				}
				banner.postDelayed(banner.mAutoSwitchTask, banner.mAutoPalyTime);
			}
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	public interface OnItemClickListener {
		void onItemClick(XBanner banner, int position);
	}

	public interface XBannerAdapter {
		void loadBanner(XBanner banner, Object model, View view, int position);
	}
}
