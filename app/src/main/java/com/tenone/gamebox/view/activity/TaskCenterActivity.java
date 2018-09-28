package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.AppBarStateChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.TaskCenterModel;
import com.tenone.gamebox.view.adapter.TaskCenterAdapter;
import com.tenone.gamebox.view.base.BaseAppCompatActivity;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskCenterActivity extends BaseAppCompatActivity implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, OnRecyclerViewItemClickListener<Integer> {
	@ViewInject(R.id.id_task_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_task_recycler)
	RecyclerView recyclerView;
	@ViewInject(R.id.id_task_appBar)
	AppBarLayout appBarLayout;
	@ViewInject(R.id.id_task_title)
	TextView titleTv;

	private TaskCenterAdapter adapter;
	private List<TaskCenterModel> models = new ArrayList<TaskCenterModel>();
	private int[] iconIds = {R.drawable.icon_task_sign, R.drawable.icon_task_comment, R.drawable.icon_task_drivering,
			R.drawable.icon_task_share, R.drawable.icon_task_top, R.drawable.icon_task_mrwd, R.drawable.icon_task_cj, R.drawable.icon_task_dh, R.drawable.icon_task_vip};
	private Class[] classes = {NewSignInActivity.class, NewGameDetailsActivity.class, PublishDynamicsActivity.class, ShareActivity.class, TopActivity.class, MyQuestionActivity.class, WebActivity.class, ExchangePlatformActivity.class, OpeningVipActvity.class};
	private boolean isCreate = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_task_center );
		ViewUtils.inject( this );
		ImmersionBar.with( this ).titleBar( toolbar ).statusBarDarkFont( true ).init();
		initTitle();
		initData();
		initView();
		isCreate = true;
	}

	private void initTitle() {
		toolbar.setTitle( "" );
		toolbar.setContentInsetsAbsolute( 0, 0 );
		setSupportActionBar( toolbar );
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled( true );
		}
		toolbar.setNavigationOnClickListener( v -> {
			finish();
		} );
		appBarLayout.addOnOffsetChangedListener( new AppBarStateChangeListener() {
			@Override
			public void onStateChanged(AppBarLayout appBarLayout, State state) {
				if (state == State.EXPANDED) {
					toolbar.setSelected( false );
					titleTv.setTextColor( getResources().getColor( R.color.white ) );
				} else if (state == State.COLLAPSED) {
					toolbar.setSelected( true );
					titleTv.setTextColor( getResources().getColor( R.color.black_33 ) );
				} else {
					toolbar.setSelected( true );
					titleTv.setTextColor( getResources().getColor( R.color.black_33 ) );
				}
			}
		} );
	}

	private void initView() {
		LinearLayoutManager manager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL,
				false );
		adapter = new TaskCenterAdapter( this, models );
		adapter.setOnRecyclerViewItemClickListener( this );
		recyclerView.setLayoutManager( manager );
		recyclerView.addItemDecoration( new SpacesItemDecoration( this, LinearLayoutManager.VERTICAL, 1,
				getResources().getColor( R.color.divider ) ) );
		recyclerView.setAdapter( adapter );
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isCreate) {
			HttpManager.taskCenter( this, HttpType.REFRESH, this );
		}
	}

	private void initData() {
		String[] nameArray = getResources().getStringArray( R.array.task_center );
		for (int i = 0; i < nameArray.length; i++) {
			TaskCenterModel model = new TaskCenterModel();
			model.setIconId( iconIds[i] );
			model.setItemName( nameArray[i] );
			model.setStatus( 0 );
			switch (i) {
				case 5:
					model.setVisible( false );
					break;
				case 6:
					model.setVisible( false );
					break;
				case 7:
					model.setVisible( false );
					break;
				case 8:
					model.setVisible( false );
					break;
				default:
					model.setVisible( true );
					break;
			}
			models.add( model );
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem data = resultItem.getItem( "data" );
			setView( data );
		} else {
			ToastCustom.makeText( this, resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
	}

	public void setView(ResultItem data) {
		String top = data.getString( "recom_top" );
		ResultItem day = data.getItem( "sign_day_bonus" );
		String normal = day.getString( "normal" );
		String vipExtra = day.getString( "vip_extra" );
		String pl = data.getString( "pl_coin" );
		String lottery = data.getString( "lottery_bonus" );
		String rank = data.getString( "rank_recom_top" );
		String dirvie = data.getString( "drive_bonus" );
		ResultItem task = data.getItem( "task" );
		for (int i = 0; i < models.size(); i++) {
			TaskCenterModel model = models.get( i );
			StringBuilder builder = new StringBuilder();
			int key = i + 1;
			if (key == 5) {
				key = 8;
			}
			builder.append( key );
			model.setStatus( task.getIntValue( builder.toString() ) );
			switch (i) {
				case 0:
					String text = "\u7b7e\u5230+" + normal + "\u91d1\u5e01,VIP\u989d\u5916+" + vipExtra + "\u91d1\u5e01";
					model.setIntro( text );
					break;
				case 1:
					String text1 = "\u6bcf\u65e5\u9996\u6b21\u8bc4\u8bba\u5956\u52b1" + pl + "\u91d1\u5e01";
					model.setIntro( text1 );
					break;
				case 2:
					String text2 = "\u6bcf\u65e5\u6210\u529f\u53d1\u8f66\u53ef\u83b7\u5f97" + dirvie + "\u91d1\u5e01";
					model.setIntro( text2 );
					break;
				case 3:
					String text3 = "\u6700\u9ad8\u5956\u52b1" + top + "\u91d1\u5e01/\u4eba";
					model.setIntro( text3 );
					break;
				case 4:
					String text4 = "\u6700\u9ad8\u5956\u52b1" + rank + "\u91d1\u5e01";
					model.setIntro( text4 );
					break;
				case 5:
					String text8 = "\u6bcf\u65e5\u524d10\u6761\u56de\u590d\u5747\u53ef\u83b7\u5f97" + Constant.getQuestionTaskCoin() + "\u91d1\u5e01\u5956\u52b1";
					model.setIntro( text8 );
					break;
				case 6:
					String text5 = "\u6700\u9ad8\u5f97\u5230" + lottery + "\u91d1\u5e01";
					model.setIntro( text5 );
					break;
				case 7:
					String text6 = "\u8d76\u7d27\u6362\u5e73\u53f0\u5e01\u6765\u73a9\u6e38\u620f\u5427";
					model.setIntro( text6 );
					break;
				case 8:
					String text7 = "\u6210\u4e3aVIP\u6bcf\u65e5\u53ef\u83b7\u5f97\u66f4\u591a\u91d1\u5e01";
					model.setIntro( text7 );
					break;
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		HttpManager.taskCenter( this, HttpType.REFRESH, this );
	}

	@Override
	public void onRecyclerViewItemClick(Integer position) {
		Intent intent = new Intent( this, classes[position] );
		switch (position) {
			case 1:
				int topGameId = MyApplication.getTopGameId();
				if (topGameId < 1) {
					topGameId = MyApplication.getDefultGameId();
				}
				intent.putExtra( "id", topGameId + "" );
				intent.setAction( "mine" );
				break;
			case 5:
				intent.putExtra( "title", "\u91d1\u5e01\u62bd\u5956" );
				String url = MyApplication.getHttpUrl().getLuckyUrl() + "&uid="
						+ SpUtil.getUserId();
				intent.putExtra( "url", url );
				Map<String, Object> map = new HashMap<String, Object>();
				map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
				map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
				map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
				TrackingUtils.setEvent( TrackingUtils.LUCKYDRAWEVENT, map );
				break;
		}
		startActivity( intent );
	}
}
