package com.tenone.gamebox.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.FragmentChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.RebateModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.RoleModel;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.custom.popupwindow.SelectRoleWindow;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ���뷵��
 *
 * @author John Lie
 */
public class ApplyForRebateFragment extends Fragment implements
		HttpResultListener, SwipeRefreshLayout.OnRefreshListener {
	@ViewInject(R.id.id_fragment_rebate_listView)
	ListView listView;
	@ViewInject(R.id.id_fragment_empty)
	ImageView imageView;
	@ViewInject(R.id.id_fragment_rebate_refresh)
	SwipeRefreshLayout refreshLayout;
	private Context mContext;

	private CommonAdapter<RebateModel> mAdapter;

	private List<RebateModel> models = new ArrayList<RebateModel>();
	private RebateModel currentModel;
	private String uid;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
													 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if (bundle != null) {
			uid = bundle.getString( "uid" );
		}
		View view = inflater
				.inflate( R.layout.fragment_rebate, container, false );
		ViewUtils.inject( this, view );
		mContext = getActivity();
		initView();
		return view;
	}

	private void initView() {
		mAdapter = new CommonAdapter<RebateModel>( mContext, models,
				R.layout.item_apply_for_rebate ) {
			@Override
			public void convert(CommonViewHolder holder, RebateModel t) {
				holder.setText( R.id.id_apply_for_rebate_gamename,
						t.getGameName() );
				holder.setText( R.id.id_apply_for_rebate_gameserver,
						t.getGameServer() + "��" );
				String roleName = t.getGameRoleName();
				String[] array = roleName.split( ";" );
				if (array != null && array.length > 0) {
					holder.setText( R.id.id_apply_for_rebate_gamerolename,
							array[0] );
				}
				TextView textView = holder
						.getView( R.id.id_apply_for_rebate_money );
				textView.setText( getString( t.getMoney() ) );
				holder.setOnClickListener( R.id.id_apply_for_rebate_button,
						new ButtonClickClass( t ) );
				holder.setOnClickListener(
						R.id.id_apply_for_rebate_gamerolename,
						new ButtonClickClass( t ) );
			}
		};
		listView.setAdapter( mAdapter );
		listView.setEmptyView( imageView );
		refreshLayout.setRefreshing( true );
		refreshLayout.setOnRefreshListener( this );
		request( HttpType.REFRESH );
	}

	private Spanned getString(String str) {
		return Html.fromHtml( getResources().getString( R.string.rebate_html,
				str ) );
	}

	private void request(int what) {
		if (TextUtils.isEmpty( uid )) {
			HttpManager.rebateInfo( what, mContext, this );
		} else {
			HttpManager.rebateInfo( what, mContext, this, uid );
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		if (1 == resultItem.getIntValue( "status" )) {
			switch (what) {
				case 90:
					ListenerManager
							.sendFragmentChange( FragmentChangeListener.FRAGMENTTWO );
					ToastCustom.makeText( mContext, "����ɹ�,�������ĵȴ����",
							ToastCustom.LENGTH_SHORT ).show();
					if (currentModel != null)
						models.remove( currentModel );
					mAdapter.notifyDataSetChanged();
					break;
				default:
					if (HttpType.REFRESH == what) {
						models.clear();
					}
					setData( resultItem );
					break;
			}
		} else {
			ToastCustom.makeText( mContext, resultItem.getString( "msg" ),
					ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		ToastCustom.makeText( mContext, error, ToastCustom.LENGTH_SHORT ).show();
	}

	private void setData(ResultItem resultItem) {
		List<ResultItem> items = resultItem.getItems( "data" );
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				ResultItem item = items.get( i );
				RebateModel model = new RebateModel();
				model.setGameName( item.getString( "gamename" ) );
				model.setMoney( item.getString( "game_coin" ) );
				model.setGameId( item.getString( "appid" ) );

				List<ResultItem> rs = item.getItems( "user" );
				List<RoleModel> roleModels = new ArrayList<RoleModel>();
				if (!BeanUtils.isEmpty( rs )) {
					for (ResultItem r : rs) {
						RoleModel roleModel = new RoleModel();
						roleModel.setRoleId( r.getString( "roleid" ) );
						roleModel.setRoleName( r.getString( "rolename" ) );
						roleModel.setServerName( r.getString( "serverID" ) );
						roleModels.add( roleModel );
					}
					model.setRoleModels( roleModels );
					if (!BeanUtils.isEmpty( roleModels )) {
						RoleModel roleModel = roleModels.get( 0 );
						model.setRoleId( roleModel.getRoleId() );
						model.setGameServer( roleModel.getServerName() );
						model.setGameRoleName( roleModel.getRoleName() );
					}
				}
				models.add( model );
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onRefresh() {
		request( HttpType.REFRESH );
	}

	private class ButtonClickClass implements OnClickListener, SelectRoleWindow.OnRoleSelectListener {
		private String appid = "", roleName = "", roleId = "", serverId = "";
		private RebateModel model;
		private List<RoleModel> roleModels;
		private SelectRoleWindow window;

		public ButtonClickClass(RebateModel m) {
			currentModel = m;
			this.model = m;
			this.appid = model.getGameId();
			roleModels = m.getRoleModels();
			roleName = m.getGameRoleName();
			roleId = m.getRoleId();
			serverId = m.getGameServer();
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.id_apply_for_rebate_button:
					requst();
					break;
				case R.id.id_apply_for_rebate_gamerolename:
					if (!BeanUtils.isEmpty( roleModels )) {
						if (window != null && window.isShowing()) {
							window.dismiss();
						}
						window = null;
						window = new SelectRoleWindow( mContext, roleModels );
						window.setOnRoleSelectListener( this );
						window.showAsDropDown( v, 0, 20 );
					}
					break;
			}
		}

		private void requst() {
			if (TextUtils.isEmpty( uid )) {
				HttpManager.rebateApply( 90, mContext, ApplyForRebateFragment.this,
						appid, roleName, roleId, serverId );
			} else {
				HttpManager.rebateApply( 90, mContext, ApplyForRebateFragment.this,
						appid, roleName, roleId, serverId, uid );
			}
		}

		@Override
		public void onRoleSelect(RoleModel model) {
			currentModel.setGameRoleName( model.getRoleName() );
			currentModel.setGameServer( model.getServerName() );
			currentModel.setRoleId( model.getRoleId() );
			mAdapter.notifyDataSetChanged();
		}
	}
}
