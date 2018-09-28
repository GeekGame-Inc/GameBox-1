package com.tenone.gamebox.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.AppBarStateChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnMainViewPagerChangeListener;
import com.tenone.gamebox.mode.listener.OnMineItemClickListener;
import com.tenone.gamebox.mode.mode.MineItemModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StatisticActionEnum;
import com.tenone.gamebox.presenter.AppStatisticsManager;
import com.tenone.gamebox.view.activity.AboutActivity;
import com.tenone.gamebox.view.activity.BindMobileActivity;
import com.tenone.gamebox.view.activity.CallCenterActivity;
import com.tenone.gamebox.view.activity.CoinDetailsActivity;
import com.tenone.gamebox.view.activity.ExchangePlatformActivity;
import com.tenone.gamebox.view.activity.GameTransferActivity;
import com.tenone.gamebox.view.activity.GoldCoinCenterActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.MainActivity;
import com.tenone.gamebox.view.activity.ManagementActivity;
import com.tenone.gamebox.view.activity.ModificationPwdActivity;
import com.tenone.gamebox.view.activity.MyAttentionActivity;
import com.tenone.gamebox.view.activity.MyGiftActivity;
import com.tenone.gamebox.view.activity.MyMessageActivity;
import com.tenone.gamebox.view.activity.MyQuestionActivity;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.activity.NewSignInActivity;
import com.tenone.gamebox.view.activity.OpeningVipActvity;
import com.tenone.gamebox.view.activity.PlatformCoinDetailActivity;
import com.tenone.gamebox.view.activity.RebateActivity;
import com.tenone.gamebox.view.activity.SettingActivity;
import com.tenone.gamebox.view.activity.ShareActivity;
import com.tenone.gamebox.view.activity.TopActivity;
import com.tenone.gamebox.view.activity.UserInfoActivity;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.adapter.NewMineItemAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ResourceAsColor")
public class MineFragment extends BaseLazyFragment implements OnMineItemClickListener, HttpResultListener, OnMainViewPagerChangeListener {
	@ViewInject(R.id.id_new_mine_appBar)
	AppBarLayout appBarLayout;
	@ViewInject(R.id.id_new_mine_collapsing)
	CollapsingToolbarLayout collapsingToolbarLayout;
	@ViewInject(R.id.id_new_mine_gold)
	TextView goldTv;
	@ViewInject(R.id.id_new_mine_platform)
	TextView platformTv;

	@ViewInject(R.id.id_new_mine_goldLayout)
	LinearLayout goldLayout;
	@ViewInject(R.id.id_new_mine_platformLayout)
	LinearLayout platformLayout;
	@ViewInject(R.id.id_new_mine_share)
	TextView shareTv;

	@ViewInject(R.id.id_new_mine_shareLayout)
	LinearLayout shareLayout;


	@ViewInject(R.id.id_new_mine_open)
	TextView openTv;
	@ViewInject(R.id.id_new_game_nologinLayout)
	RelativeLayout nologinLayout;
	@ViewInject(R.id.id_new_game_loginLayot)
	RelativeLayout loginLayot;
	@ViewInject(R.id.id_new_game_header)
	CircleImageView headerIv;
	@ViewInject(R.id.id_new_game_nickName)
	TextView nickNameTv;
	@ViewInject(R.id.id_new_mine_title)
	TextView titleTv;
	@ViewInject(R.id.id_new_mine_setting)
	ImageView settingIv;
	@ViewInject(R.id.id_new_game_vip)
	ImageView vipIv;
	@ViewInject(R.id.id_new_mine_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_new_mine_recycler)
	RecyclerView recyclerView;


	private NewMineItemAdapter adapter;
	private List<MineItemModel> itemModels = new ArrayList<MineItemModel>();
	private List<Class> items = new ArrayList<Class>();
	private int[] imgIdArray = {R.drawable.icon_qiandao, R.drawable.icon_meiripinglun, R.drawable.a_icon_meiriwenda,
			R.drawable.icon_yaoqinghaoyou, R.drawable.icon_share_top, R.drawable.icon_driver, R.drawable.icon_jbdh,
			R.drawable.icon_jbcj, R.drawable.icon_hbmx, R.drawable.icon_flsq,
			R.drawable.icon_shenqingzhuanyou, R.drawable.icon_yygl,
			R.drawable.icon_wodelibao, R.drawable.icon_wodeshouchang,
			R.drawable.icon_wdxx, R.drawable.b_icon_wodewenda, R.drawable.icon_kefu,
			R.drawable.icon_xgmm, R.drawable.icon_bdsj, R.drawable.icon_gy};
	private String[] itemArray;

	private boolean isVip;
	private String gold, platform, earnings;
	private boolean isCreate = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_mine, container, false );
		ViewUtils.inject( this, view );
		ListenerManager.registerOnMainViewPagerChangeListener( this );
		itemArray = getActivity().getResources().getStringArray( R.array.mine_item );
		return view;
	}


	private void initView() {
		collapsingToolbarLayout.setTitle( "" );
		toolbar.setTitle( "" );
		toolbar.setContentInsetsAbsolute( 0, 0 );
		appBarLayout.addOnOffsetChangedListener( new AppBarStateChangeListener() {
			@Override
			public void onStateChanged(AppBarLayout appBarLayout, State state) {
				if (state == State.EXPANDED) {

					titleTv.setText( "" );
				} else if (state == State.COLLAPSED) {

					titleTv.setText( "\u4e2a\u4eba\u4e2d\u5fc3" );
				} else {

					titleTv.setText( "\u4e2a\u4eba\u4e2d\u5fc3" );
				}
			}
		} );
		initData();
		LinearLayoutManager manager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
		adapter = new NewMineItemAdapter( itemModels, getActivity() );
		adapter.setOnMineItemClickListener( this );
		recyclerView.setLayoutManager( manager );
		recyclerView.setAdapter( adapter );
		ininShowView( BeanUtils.isLogin() );
		isCreate = true;
	}

	private void setOnViewClickListener() {
		settingIv.setOnClickListener( v -> startActivity( new Intent( getActivity(), SettingActivity.class ) ) );
		goldLayout.setOnClickListener( v -> startActivity( new Intent( getActivity(), GoldCoinCenterActivity.class ) ) );
		platformLayout.setOnClickListener( v -> startActivity( new Intent( getActivity(), PlatformCoinDetailActivity.class )
				.setAction( "platform" ) ) );
		loginLayot.setOnClickListener( v -> startActivity( new Intent( getActivity(), UserInfoActivity.class )
				.putExtra( "uid", SpUtil.getUserId() ) ) );
		shareLayout.setOnClickListener( v -> startActivity( new Intent( getActivity(), TopActivity.class ) ) );
	}

	private void unOnViewClickListener() {
		goldLayout.setOnClickListener( null );
		platformLayout.setOnClickListener( null );
		headerIv.setOnClickListener( null );
		loginLayot.setOnClickListener( null );
		shareLayout.setOnClickListener( null );
	}

	private void initData() {
		initClass();
		itemModels.clear();
		for (int i = 0; i < items.size(); i++) {
			MineItemModel model = new MineItemModel();
			model.setDrawableId( imgIdArray[i] );
			model.setItemName( itemArray[i] );
			boolean isShowLine = (6 == i || 9 == i || 12 == i || 16 == i);
			model.setShowLine( isShowLine );
			itemModels.add( model );
		}
	}

	private void initClass() {
		items.clear();
		items.add( NewSignInActivity.class );
		items.add( NewGameDetailsActivity.class );
		items.add( MyQuestionActivity.class );
		items.add( ShareActivity.class );
		items.add( TopActivity.class );
		items.add( MainActivity.class );
		items.add( ExchangePlatformActivity.class );
		items.add( WebActivity.class );
		items.add( CoinDetailsActivity.class );
		items.add( RebateActivity.class );
		items.add( GameTransferActivity.class );
		items.add( ManagementActivity.class );
		items.add( MyGiftActivity.class );
		items.add( MyAttentionActivity.class );
		items.add( MyMessageActivity.class );
		items.add( MyQuestionActivity.class );
		items.add( CallCenterActivity.class );
		items.add( ModificationPwdActivity.class );
		items.add( BindMobileActivity.class );
		if (BeanUtils.is185()) {
			items.add( AboutActivity.class );
		}
	}

	private void setView() {
		vipIv.setSelected( isVip );
		goldTv.setText( gold );
		shareTv.setText( earnings );
		platformTv.setText( platform );
		ImageLoadUtils.loadNormalImg( headerIv, getActivity(), SpUtil.getHeaderUrl() );
		nickNameTv.setText( SpUtil.getAccount() );
	}

	private void resetView() {
		vipIv.setSelected( false );
		goldTv.setText( "0" );
		shareTv.setText( "0" );
		platformTv.setText( "0" );
	}

	@Override
	public void onMineItemClic(int position) {
		addButtonStatistics( position );
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( getActivity(),
					LoginActivity.class ) );
			return;
		}
		Intent intent = null;
		if (position != 5) {
			intent = new Intent( getActivity(), items.get( position ) );
		} else {
			Intent intent1 = new Intent();
			intent1.setAction( "ToDriving" );
			LocalBroadcastManager.getInstance( getActivity() ).sendBroadcast( intent1 );
			return;
		}
		switch (position) {
			case 1:
				int topGameId = MyApplication.getTopGameId();
				if (topGameId < 1) {
					topGameId = MyApplication.getDefultGameId();
				}
				intent.putExtra( "id", topGameId + "" );
				intent.setAction( "mine" );
				break;
			case 2:
				intent.putExtra( "type", 1 );
				break;
			case 7:
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
			case 14:
				intent.putExtra( "tag", 0 );
				break;
			case 18:
				intent.setAction( isBindMobile() ? "unbind" : "bind" );
				break;
			case 15:
				intent.putExtra( "type", 2 );
				break;
		}
		startActivity( intent );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem item = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( item )) {
				String recom_top = item.getString( "recom_top" );
				isVip = (1 == item.getIntValue( "is_vip" ));
				platform = item.getString( "platform_money" );
				gold = item.getString( "coin" );
				earnings = item.getString( "recom_bonus" );
				ResultItem sign_day_bonus = item.getItem( "sign_day_bonus" );
				if (!BeanUtils.isEmpty( sign_day_bonus )) {
					String platform_coin_ratio = item
							.getString( "platform_coin_ratio" );
					String pl_coin = item.getString( "pl_coin" );
					String vipSign = sign_day_bonus.getString( "vip_extra" );
					String sign = sign_day_bonus.getString( "normal" );
					String mobile = item.getString( "mobile" );
					String deplete_coin = item.getString( "deplete_coin" );
					String topGold = item.getString( "rank_recom_top" );
					setInstruction( sign, pl_coin, recom_top,
							platform_coin_ratio, vipSign, mobile, deplete_coin, topGold );
				}
			}
		} else {
			showToast( resultItem.getString( "msg" ) );
		}
		setView();
	}

	@Override
	public void onError(int what, String error) {
		showToast( error );
	}

	private void setInstruction(String sign, String comment, String share, String exchange, String vipSign,
															String mobile, String deplete_coin, String topGold) {
		for (int i = 0; i < itemModels.size(); i++) {
			MineItemModel model = itemModels.get( i );
			model.setHtml( true );
			switch (i) {
				case 0:
					model.setText( getActivity().getResources()
							.getString( R.string.sign_text, sign, vipSign ) );
					break;
				case 1:
					String plStr = comment.replace( "-", "~" );
					model.setText( getActivity().getResources().
							getString( R.string.comment_text, plStr ) );
					break;
				case 2:
					model.setText( getActivity().getResources().
							getString( R.string.answer_text, Constant.getQuestionTaskCoin() ) );
					break;
				case 3:
					model.setText( getActivity().getResources().getString( R.string.share_text, share ) );
					break;
				case 4:
					model.setText( getActivity().getResources().getString( R.string.top_text, topGold ) );
					break;
				case 5:
					model.setText( getActivity().getResources()
							.getString( R.string.dirver_text, "5~30" ) );
					break;
				case 6:
					model.setText( getActivity().getResources()
							.getString( R.string.exchange_text, exchange, "10", "1" ) );
					break;
				case 7:
					model.setText( getActivity().getResources()
							.getString( R.string.lucky_text, deplete_coin ) );
					break;
				case 9:
					model.setText( "\u5145\u503c\u6709\u5956,\u5143\u5b9d\u8fd4\u8fd8" );
					model.setHtml( false );
					break;
				case 16:
					model.setText( "\u5bfb\u6c42\u5e2e\u52a9,\u95ee\u9898\u53cd\u9988" );
					model.setHtml( false );
					break;
				case 18:
					model.setText( BeanUtils.isLogin() ? (isBindMobile() ? CharSequenceUtils
							.getVisibilyPhone( mobile ) : "\u53bb\u7ed1\u5b9a") : "" );
					model.setHtml( false );
					break;
				default:
					model.setText( "" );
					model.setHtml( false );
					break;
			}
			adapter.notifyDataSetChanged();
		}
	}

	private boolean isBindMobile() {
		boolean isBind = false;
		isBind = (!TextUtils.isEmpty( SpUtil.getPhone() ))
				&& BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN,
				SpUtil.getPhone() );
		return isBind;
	}


	private void ininShowView(boolean isLogin) {
		openTv.setOnClickListener( v -> {
			addButtonStatistics( -1 );
			if (isLogin) {
				startActivity( new Intent( getActivity(), OpeningVipActvity.class ) );
			} else {
				startActivity( new Intent( getActivity(), LoginActivity.class ) );
			}
		} );
		if (isLogin) {
			loginLayot.setVisibility( View.VISIBLE );
			nologinLayout.setVisibility( View.GONE );
			setOnViewClickListener();
			nologinLayout.setOnClickListener( null );
			HttpManager.userCenter( 18, getActivity(), this );
		} else {
			loginLayot.setVisibility( View.GONE );
			nologinLayout.setVisibility( View.VISIBLE );
			unOnViewClickListener();
			resetView();
			nologinLayout.setOnClickListener( v -> {
				addButtonStatistics( -2 );
				startActivity( new Intent( getActivity(), LoginActivity.class ) );
			} );
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ImmersionBar.with( this ).destroy();
		ListenerManager.unRegisterOnMainViewPagerChangeListener( this );
	}

	@Override
	public void onLazyLoad() {
		initView();
	}

	@Override
	public void onMainViewPagerChange(int index) {
		if (index == 3) {
			ImmersionBar.with( this ).titleBar( toolbar ).statusBarDarkFont( true ).init();
			ininShowView( BeanUtils.isLogin() );
		}
	}

	private void addButtonStatistics(int type) {
		switch (type) {
			case -2:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_REGISTER );
				break;
			case -1:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_OPEN_VIP );
				break;
			case 0:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_SIGN );
				break;
			case 1:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_COMMENT );
				break;
			case 2:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_DO_ANSWER );
				break;
			case 3:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_SHARE );
				break;
			case 4:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_SHARE_TOP );
				break;
			case 5:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_DRIVE );
				break;
			case 6:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_EXCHANGE_COINS );
				break;
			case 7:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_RAFFLE );
				break;
			case 8:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_DETAIL );
				break;
			case 9:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_REBATE );
				break;
			case 10:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_TRANSFER );
				break;
			case 11:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_MANAGER );
				break;
			case 12:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_GIFTS );
				break;
			case 13:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_COLLECT );
				break;
			case 14:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_MESSAGE );
				break;
			case 15:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_MY_QUESTIONS );
			case 16:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_CALL_CENTER );
				break;
			case 17:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_CHANGE_PASSWORD );
				break;
			case 18:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_BIND_MOBILE );
				break;
			case 19:
				AppStatisticsManager.addStatistics( StatisticActionEnum.PERSONAL_ABOUT );
				break;
		}
	}
}
