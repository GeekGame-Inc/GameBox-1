package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.NewSignInGridViewAdapter;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.ArrayList;
import java.util.List;

public class NewSignInActivity extends AppCompatActivity implements HttpResultListener {
	@ViewInject(R.id.id_new_sigin_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_new_sigin_bg)
	ImageView bgIv;
	@ViewInject(R.id.id_new_sigin_sign)
	TextView signTv;
	@ViewInject(R.id.id_new_sigin_grid1)
	MyGridView gridView1;
	@ViewInject(R.id.id_new_sigin_grid2)
	MyGridView gridView2;
	@ViewInject(R.id.id_new_sigin_bottomLayout)
	RelativeLayout bottomLayout;
	@ViewInject(R.id.id_signin_signIv)
	ImageView signIv;
	@ViewInject(R.id.id_signin_day)
	TextView dayTv;
	@ViewInject(R.id.id_signin_ereyDayTv)
	TextView everyDayTv;
	@ViewInject(R.id.id_signin_addTv)
	TextView addTv;

	private NewSignInGridViewAdapter adapter1, adapter2;
	private List<String> array1 = new ArrayList<>(), array2 = new ArrayList<>();
	private int screenWidth, screenHeight, dp44, statusBarHeight, signCounts = 0;
	private boolean isSign = false;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_new_signin );
		ViewUtils.inject( this );
		ImmersionBar.with( this ).titleBar( toolbar ).statusBarDarkFont( true ).init();
		screenWidth = DisplayMetricsUtils.getScreenWidth( this );
		screenHeight = DisplayMetricsUtils.getScreenHeight( this );
		dp44 = DisplayMetricsUtils.dipTopx( this, 44 );
		statusBarHeight = DisplayMetricsUtils.getStatusBarHeight( this );
		initView();
	}

	@OnClick({R.id.id_signin_signIv})
	public void onClick(View view) {
		if (view.getId() == R.id.id_signin_signIv) {
			if (isSign) {
				ToastUtils.showToast( NewSignInActivity.this, "\u60a8\u4eca\u5929\u5df2\u7ecf\u7b7e\u8fc7\u5230\u4e86" );
				return;
			}
			HttpManager.signIn( HttpType.LOADING, this, this );
		}
	}

	private void initView() {
		toolbar.setNavigationOnClickListener( v -> finish() );
		ViewGroup.LayoutParams params = bgIv.getLayoutParams();
		params.width = screenWidth;
		params.height = screenHeight / 2;
		bgIv.setLayoutParams( params );

		RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) signTv.getLayoutParams();
		params1.topMargin = dp44 + statusBarHeight + DisplayMetricsUtils.dipTopx( this, 20 );
		signTv.setLayoutParams( params1 );

		RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) bottomLayout.getLayoutParams();
		params2.topMargin = params.height - DisplayMetricsUtils.dipTopx( this, 30 );
		bottomLayout.setLayoutParams( params2 );

		RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) gridView1.getLayoutParams();
		params3.topMargin = params1.topMargin + DisplayMetricsUtils.dipTopx( this, 60 );
		gridView1.setLayoutParams( params3 );

		RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) signIv.getLayoutParams();
		params4.topMargin = params.height - DisplayMetricsUtils.dipTopx( this, 70 );
		signIv.setLayoutParams( params4 );
		initGridView();
		HttpManager.signInit( HttpType.REFRESH, this, this );
	}

	private void initGridView() {
		adapter1 = new NewSignInGridViewAdapter( this, array1, false );
		adapter2 = new NewSignInGridViewAdapter( this, array2, true );
		gridView1.setAdapter( adapter1 );
		gridView2.setAdapter( adapter2 );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			switch (what) {
				case HttpType.REFRESH:
					ResultItem item = resultItem.getItem( "data" );
					setData( item );
					break;
				case HttpType.LOADING:
					if (1 == resultItem.getIntValue( "status" )) {
						ToastUtils.showToast( NewSignInActivity.this, "\u4eca\u65e5\u9886\u53d6" + resultItem.getString( "data" ) + "\u91d1\u5e01" );
						HttpManager.signInit( HttpType.REFRESH, NewSignInActivity.this, NewSignInActivity.this );
						TrackingUtils.setEvent( TrackingUtils.SIGNEVENT, TrackingUtils.getUserInfoMap() );
					}
					break;
			}
		} else {
			ToastUtils.showToast( NewSignInActivity.this, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastUtils.showToast( this, error );
	}

	public void setData(ResultItem data) {
		isSign = data.getBooleanValue( "today_is_sign", 1 );
		signCounts = data.getIntValue( "sign_counts" );
		signIv.setSelected( isSign );
		array1.clear();
		array2.clear();
		List<ResultItem> items = data.getItems( "accum_bonus" );
		List<Integer> integers = new ArrayList<>();
		if (!BeanUtils.isEmpty( items )) {
			StringBuilder builder = new StringBuilder();
			int size = items.size();
			for (int i = 0; i < size; i++) {
				ResultItem item = items.get( i );
				String num = item.getString( "num" );
				integers.add( Integer.valueOf( num ) );
				String bonus = item.getString( "bonus" );
				if (i < size - 1) {
					builder.append( "\u7d2f\u8ba1\u7b7e\u5230" + num + "\u5929,\u989d\u5916\u83b7\u53d6" + bonus + "\u91d1\u5e01," );
				} else {
					builder.append( "\u672c\u6708\u5168\u90e8\u7b7e\u5230\uff0c\u989d\u5916\u83b7\u53d6" + bonus + "\u91d1\u5e01\u3002" );
				}
				array1.add( num + "天" );
				array2.add( bonus );
			}
			addTv.setText( builder.toString() );
			adapter1.notifyDataSetChanged();
			adapter2.notifyDataSetChanged();
		}
		ResultItem dayBonus = data.getItem( "day_bonus" );
		everyDayTv.setText( getEveryDayText( dayBonus ) );
		dayTv.setText( Html.fromHtml( getResources().getString( R.string.new_sign_text,
				signCounts + "", calculationRemaining( integers ) + "" ) ) );
	}

	private String getEveryDayText(ResultItem item) {
		String str1 = item.getString( "normal" );
		String str2 = item.getString( "vip_extra" );
		return "\u666e\u901a\u7528\u6237\u6bcf\u65e5\u7b7e\u5230\u83b7\u53d6" + str1 + "\u91d1\u5e01\uff0cvip\u7528\u6237\u989d\u5916\u83b7\u5f97" + str2 + "\u91d1\u5e01\u3002";
	}

	private int calculationRemaining(List<Integer> integers) {
		int remainning = 0, size = integers.size();
		for (int i = 0; i < size; i++) {
			int j = integers.get( i ).intValue();
			if (signCounts >= j) {
				continue;
			}
			remainning = j - signCounts;
			break;
		}
		return remainning;
	}
}
