package com.tenone.gamebox.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.FragmentChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.RebateModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ������¼
 *
 * @author John Lie
 */
public class RebateRecordFragment extends Fragment implements
		OnRefreshListener, OnLoadListener, HttpResultListener, FragmentChangeListener {

	@ViewInject(R.id.id_rebate_record_listView)
	ListView listView;
	@ViewInject(R.id.id_rebate_record_refreshLayout)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_empty_root)
	LinearLayout emptyView;
	private Context mContext;
	private CommonAdapter<RebateModel> mAdapter;
	private List<RebateModel> models = new ArrayList<RebateModel>();

	int page = 1;
	private String uid;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
													 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if (bundle != null) {
			uid = bundle.getString( "uid" );
		}
		View view = inflater.inflate( R.layout.fragment_rebate_record,
				container, false );
		ViewUtils.inject( this, view );
		mContext = getActivity();
		initView();
		return view;
	}

	private void initView() {

		ListenerManager.registerFragmentChangeListener( this );
		mAdapter = new CommonAdapter<RebateModel>( mContext, models,
				R.layout.item_rebate_record ) {
			@SuppressWarnings("deprecation")
			@Override
			public void convert(CommonViewHolder holder, RebateModel t) {
				holder.setText( R.id.id_rebate_record_gamename, t.getGameName() );
				holder.setText( R.id.id_rebate_record_gameserver,
						t.getGameServer() );
				holder.setText( R.id.id_rebate_record_money, t.getMoney() );
				holder.setText( R.id.id_rebate_record_gamerolename,
						t.getGameRoleName() );
				holder.setText( R.id.id_rebate_record_time, t.getTime() );
				int rid = 0;
				switch (t.getState()) {
					case 1:
						rid = R.drawable.icon_succeed;
						break;
					case 2:
						rid = R.drawable.icon_shenhezhong;
						break;
					case 3:
						rid = R.drawable.icon_shibai;
						break;
				}
				Drawable drawable = getResources().getDrawable( rid );
				holder.setBackgroundDra( R.id.id_rebate_record_state, drawable );
			}
		};
		listView.setAdapter( mAdapter );
		listView.setEmptyView( emptyView );
		refreshLayout.setOnRefreshListener( this );
		refreshLayout.setOnLoadListener( this );
		request( HttpType.REFRESH );
	}

	private void request(int what) {
		if (TextUtils.isEmpty( uid )) {
			HttpManager.rebateRecord( what, mContext, this, page );
		} else {
			HttpManager.rebateRecord( what, mContext, this, page, uid );
		}
	}

	@Override
	public void onLoad() {
		page += 1;
		request( HttpType.LOADING );
	}

	@Override
	public void onRefresh() {
		page = 1;
		request( HttpType.REFRESH );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
		if (1 == resultItem.getIntValue( "status" )) {
			if (what == HttpType.REFRESH) {
				models.clear();
			}
			ResultItem item = resultItem.getItem( "data" );
			if (item != null) {
				setData( item );
			}
		} else {
			ToastCustom.makeText( mContext, resultItem.getString( "msg" ),
					ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
		ToastCustom.makeText( mContext, error, ToastCustom.LENGTH_SHORT ).show();
	}

	private void setData(ResultItem resultItem) {
		List<ResultItem> items = resultItem.getItems( "rebate" );
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				ResultItem item = items.get( i );
				RebateModel model = new RebateModel();
				model.setGameName( item.getString( "gamename" ) );
				model.setGameServer( item.getString( "servername" ) );
				model.setMoney( item.getString( "game_coin" ) );
				model.setGameRoleName( item.getString( "rolename" ) );
				model.setState( item.getIntValue( "status" ) );
				model.setTime( item.getString( "create_time" ) );
				models.add( model );
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onFragmentChange(int which) {
		refreshLayout.setRefreshing( true );
		page = 1;
		request( HttpType.REFRESH );
	}
}
