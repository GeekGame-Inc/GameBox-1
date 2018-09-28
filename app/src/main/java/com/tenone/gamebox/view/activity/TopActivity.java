package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.SuperTopModel;
import com.tenone.gamebox.mode.mode.TopModel;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.DrawView;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.fragment.TodayTopFragment;
import com.tenone.gamebox.view.fragment.YesterdayTopFragment;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class TopActivity extends BaseActivity implements HttpResultListener, PlatformActionListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBar;
    @ViewInject(R.id.id_top_viewpager)
    ViewPager viewPager;
    @ViewInject(R.id.id_top_tabPageIndicator)
    TabPageIndicator indicator;
    @ViewInject(R.id.id_top_underlineIndicator)
    CustomerUnderlinePageIndicator underlineIndicator;
    @ViewInject(R.id.id_top_numTv)
    TextView numTv;

    private ManagementAdapter adapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<String> title = new ArrayList<String>();
    private float width = 0, widthOffset = 0;
    private int currentFragment = 0;
    private ArrayList<TopModel> todayModels = new ArrayList<TopModel>();
    private ArrayList<TopModel> yesterdayModels = new ArrayList<TopModel>();
    private SuperTopModel todayModel, yesterdayModel;
    private DrawView drawView;
    private SharePopupWindow sharePopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_top );
        ViewUtils.inject( this );
        initTitle();
        initData();
    }


    private void initTitle() {
        titleBar.setTitleText( getString( R.string.top ) );
        titleBar.setLeftImg( R.drawable.icon_xqf_b );
        Drawable drawable = ContextCompat.getDrawable( this, R.drawable.icon_wen );
        DrawableCompat.setTintList( drawable, ColorStateList.valueOf( ContextCompat.getColor( this,R.color.gray_69 ) ) );
        titleBar.setRightImg( drawable);
        String[] array = getResources().getStringArray( R.array.top );
        for (String str : array) {
            title.add( str );
        }
    }

    private void initData() {
        buildProgressDialog();
        HttpManager.userRanking( 1, this, this );
        HttpManager.rankingList( 0, this, this );
    }

    private void initFragment() {
        Bundle bundle1 = new Bundle();
        todayModel = new SuperTopModel( todayModels );
        bundle1.putSerializable( "data", todayModel );
        TodayTopFragment todayTopFragment = new TodayTopFragment( );
        todayTopFragment.setArguments( bundle1 );
        fragments.add( todayTopFragment );
        Bundle bundle2 = new Bundle();
        yesterdayModel = new SuperTopModel( yesterdayModels );
        bundle2.putSerializable( "data", yesterdayModel );
        YesterdayTopFragment yesterdayTopFragment = new YesterdayTopFragment();
        yesterdayTopFragment.setArguments( bundle2 );
        fragments.add( yesterdayTopFragment );
        initView();
    }

    private void initView() {
        adapter = new ManagementAdapter( getSupportFragmentManager() );
        adapter.setArray( fragments );
        adapter.setmTitleList( title );
        viewPager.setAdapter( adapter );
        indicator.setViewPager( viewPager );
        underlineIndicator.setViewPager( viewPager );
        underlineIndicator.setFades( false );
        indicator.setOnPageChangeListener( underlineIndicator );
        
        width = indicator.getTextWidth();
        
        widthOffset = (DisplayMetricsUtils.getScreenWidth( this )
                / adapter.getCount() - width) / 2;
        underlineIndicator.setDefultWidth( width );
        underlineIndicator.setDefultOffset( widthOffset );

        viewPager.setCurrentItem( currentFragment );
        viewPager.setOffscreenPageLimit( 2 );

        runOnUiThread( () -> {
            drawView = new DrawView( TopActivity.this );
            drawView.show();
            drawView.setOnClickListener( v -> {
                String url = MyApplication.getHttpUrl().getFrendRecom() + "?c="
                        + MyApplication.getConfigModle().getChannelID() + "&u="
                        + SpUtil.getUserId();
                showPopuwindow( url );
            } );
        } );
    }

    @OnClick({R.id.id_titleBar_leftImg, R.id.id_titleBar_rightImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_titleBar_leftImg:
                finish();
                break;
            case R.id.id_titleBar_rightImg:
                startActivity( new Intent( this, RebateHelpActivity.class ).putExtra( "what", 2 ) );
                break;
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
            switch (what) {
                case 0:
                    ResultItem data = resultItem.getItem( "data" );
                    if (!BeanUtils.isEmpty( data ))
                        setData( data );
                    break;
                case 1:
									numTv.setText( "\u60a8\u4eca\u65e5\u9080\u8bf7\u4eba\u6570:" + resultItem.getString( "data" ) );
									break;
            }
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
    }

    public void setData(ResultItem data) {
        List<ResultItem> today = data.getItems( "today" );
        if (!BeanUtils.isEmpty( today )) {
            for (ResultItem t : today) {
                TopModel model = new TopModel();
                model.setCoin( t.getString( "coin" ) );
                model.setCount( t.getString( "count" ) );
                model.setGot( t.getIntValue( "got" ) );
                model.setIcon( t.getString( "icon_url" ) );
                model.setRanking( t.getString( "ranking" ) );
                model.setSex( t.getIntValue( "sex" ) );
                model.setVip( t.getIntValue( "vip" ) );
                model.setUid( t.getString( "uid" ) );
                model.setUserName( t.getString( "username" ) );
                todayModels.add( model );
            }
        }

        List<ResultItem> yesterday = data.getItems( "yesterday" );
        if (!BeanUtils.isEmpty( yesterday )) {
            for (ResultItem t : yesterday) {
                TopModel model = new TopModel();
                model.setCoin( t.getString( "coin" ) );
                model.setCount( t.getString( "count" ) );
                model.setGot( t.getIntValue( "got" ) );
                model.setIcon( t.getString( "icon_url" ) );
                model.setRanking( t.getString( "ranking" ) );
                model.setSex( t.getIntValue( "sex" ) );
                model.setVip( t.getIntValue( "vip" ) );
                model.setUid( t.getString( "uid" ) );
                model.setUserName( t.getString( "username" ) );
                yesterdayModels.add( model );
                if (t.getString( "uid" ).equals( SpUtil.getUserId() ) && t.getIntValue( "got" ) == 0) {
                    currentFragment = 1;
                }
            }
        }
        initFragment();
    }

    @Override
    protected void onStop() {
        if (drawView != null) {
            drawView.hide();
            drawView = null;
        }
        super.onStop();
    }

    private void showPopuwindow(String url) {
        drawView.hide();
        if (sharePopupWindow == null) {
            sharePopupWindow = new SharePopupWindow( this, url );
        }
        sharePopupWindow.showAtLocation( titleBar, Gravity.BOTTOM, 0, 0 );
        sharePopupWindow.setPlatformActionListener( this );
        sharePopupWindow.setOnDismissListener( () -> {
            drawView.show();
        } );
    }

	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
		ToastCustom.makeText( this, "\u5206\u4eab\u6210\u529f", ToastCustom.LENGTH_SHORT ).show();
	}

	@Override
	public void onError(Platform platform, int i, Throwable throwable) {
		ToastCustom.makeText( this, "\u5206\u4eab\u51fa\u9519", ToastCustom.LENGTH_SHORT ).show();
	}

	@Override
	public void onCancel(Platform platform, int i) {
		ToastCustom.makeText( this, "\u5206\u4eab\u53d6\u6d88", ToastCustom.LENGTH_SHORT ).show();
	}
}
