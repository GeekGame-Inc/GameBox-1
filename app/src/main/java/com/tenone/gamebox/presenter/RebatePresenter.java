package com.tenone.gamebox.presenter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.RebateBiz;
import com.tenone.gamebox.mode.listener.FragmentChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.RebateView;
import com.tenone.gamebox.view.activity.RebateHelpActivity;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.MarqueeTextView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.custom.xbanner.transformers.BasePageTransformer;
import com.tenone.gamebox.view.custom.xbanner.transformers.Transformer;
import com.tenone.gamebox.view.utils.ChenColorUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.M)
public class RebatePresenter extends BasePresenter implements OnClickListener,
		HttpResultListener, FragmentChangeListener {
	private Context mContext;
	private RebateView rebateView;
	private RebateBiz rebateBiz;
	private ManagementAdapter mAdapter;
	private float width = 0, widthOffset = 0;
	private String sdkUid;

	public RebatePresenter(Context cxt, RebateView view, String sdkUid) {
		this.mContext = cxt;
		this.rebateView = view;
		this.sdkUid = sdkUid;
		this.rebateBiz = new RebateBiz();
	}

	public void initView() {
		request( HttpType.REFRESH );
		initTitle();
		setAdapter();
		initTabView();
		initListener();
	}

	
	@SuppressWarnings("deprecation")
	public void initTitle() {
		Drawable leftDrawable = mContext.getResources().getDrawable(
				R.drawable.icon_xqf_b );
		leftImg().setImageDrawable( leftDrawable );
		titleTv().setText( R.string.apply_for_rebate );
		Drawable rightDrawable = mContext.getResources().getDrawable(
				R.drawable.icon_wen );
		rightImg().setImageDrawable(
				ChenColorUtils.tintDrawable( rightDrawable,
						ColorStateList.valueOf( mContext.getResources().getColor( R.color.gray_69 ) ) ) );
	}

	private void request(int what) {
		HttpManager.rebateNotice( what, mContext, this );
	}

	
	public void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new ManagementAdapter(
					((FragmentActivity) mContext).getSupportFragmentManager() );
		}
		mAdapter.setArray( getFragments() );
		mAdapter.setmTitleList( getTitles( R.array.rebate_titles ) );
		viewPager().setAdapter( mAdapter );
	}

	
	public void initTabView() {
		tabPageIndicator().setViewPager( viewPager() );
		underlinePageIndicator().setViewPager( viewPager() );
		underlinePageIndicator().setFades( false );
		tabPageIndicator().setOnPageChangeListener( underlinePageIndicator() );
		
		width = tabPageIndicator().getTextWidth();
		
		widthOffset = (DisplayMetricsUtils.getScreenWidth( mContext )
				/ mAdapter.getCount() - width) / 2;
		underlinePageIndicator().setDefultWidth( width );
		underlinePageIndicator().setDefultOffset( widthOffset );
		viewPager().setCurrentItem( 0 );
		viewPager().setOffscreenPageLimit( 2 );
		viewPager().setPageTransformer( true,
				BasePageTransformer.getPageTransformer( Transformer.Stack ) );
	}

	
	public void initListener() {
		leftImg().setOnClickListener( this );
		rightImg().setOnClickListener( this );
		ListenerManager.registerFragmentChangeListener( this );
	}

	private ImageView leftImg() {
		return rebateView.leftImg();
	}

	private ImageView rightImg() {
		return rebateView.rightImg();
	}

	private TextView titleTv() {
		return rebateView.titleTv();
	}

	private TabPageIndicator tabPageIndicator() {
		return rebateView.tabPageIndicator();
	}

	private CustomerUnderlinePageIndicator underlinePageIndicator() {
		return rebateView.underlinePageIndicator();
	}

	private ViewPager viewPager() {
		return rebateView.viewPager();
	}

	public MarqueeTextView marqueeTextView() {
		return rebateView.marqueeTextView();
	}

	private List<Fragment> getFragments() {
		return rebateBiz.getFragments(sdkUid);
	}

	public List<String> getTitles(int rid) {
		return rebateBiz.getTitles( mContext, rid );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_newtTitle_leftImg:
				((BaseActivity) mContext).finish();
				break;
			case R.id.id_newtTitle_rightImg:
				Intent intent = new Intent();
				intent.putExtra( "what", 0 );
				intent.setClass( mContext, RebateHelpActivity.class );
				mContext.startActivity( intent );
				break;
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			List<ResultItem> items = resultItem.getItems( "data" );
			List<String> array = new ArrayList<String>();
			for (int i = 0; i < items.size(); i++) {
				ResultItem item = items.get( i );
				String str = item.getString( "rolename" ) + "\u7533\u8bf7\u4e86"
						+ item.getString( "amount" ) + "\u5143\u8fd4\u5229";
				array.add( str );
			}
			marqueeTextView().setTextArraysAndClickListener( array, null );
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( mContext, error, ToastCustom.LENGTH_SHORT ).show();
	}

	@Override
	public void onFragmentChange(int which) {
		viewPager().setCurrentItem( which );
	}
}
