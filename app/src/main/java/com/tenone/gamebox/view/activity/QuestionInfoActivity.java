package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.john.superadapter.OnItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnLoadMoreListener;
import com.tenone.gamebox.mode.mode.AnswerModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.UserInfoModel;
import com.tenone.gamebox.view.adapter.AnswerAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.custom.LoadMoreRecyclerView;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.custom.dialog.QuestionNoticeDialog;
import com.tenone.gamebox.view.custom.dialog.RationaleDialog;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class QuestionInfoActivity extends BaseActivity implements HttpResultListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, TextView.OnEditorActionListener {
	@ViewInject(R.id.id_question_info_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_question_info_refresh)
	SwipeRefreshLayout refreshLayout;

	@ViewInject(R.id.id_question_info_list)
	LoadMoreRecyclerView recyclerView;
	@ViewInject(R.id.id_question_info_title)
	TextView titleTv;
	@ViewInject(R.id.id_question_info_notice)
	ImageView noticeIv;


	@ViewInject(R.id.id_question_info_icon)
	ImageView headerIv;
	@ViewInject(R.id.id_question_info_name)
	TextView nickTv;
	@ViewInject(R.id.id_question_info_question)
	TextView questionTv;
	@ViewInject(R.id.id_question_info_time)
	TextView timeTv;
	@ViewInject(R.id.id_question_info_count)
	TextView answerCountTv;
	@ViewInject(R.id.id_question_info_send)
	TextView sendTv;
	@ViewInject(R.id.id_question_info_edit)
	EditText editText;
	@ViewInject(R.id.id_question_info_bottom)
	RelativeLayout bottomView;
	@ViewInject(R.id.id_empty_iv)
	ImageView emptyView;
	@ViewInject(R.id.id_question_info_hint)
	TextView hintTv;
	private String consultId;
	private int page = 1;
	private AnswerAdapter adapter;
	private List<AnswerModel> models = new ArrayList<>();
	private boolean isMyself = false;
	private QuestionNoticeDialog.QuestionNoticeBuilder builder;
	private RationaleDialog.RationaleBuilder rationaleBuilder;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		consultId = getIntent().getStringExtra( "consultId" );
		setContentView( R.layout.activity_question_info );
		ViewUtils.inject( this );
		initTitle();
		initView();
	}

	private void initTitle() {
		toolbar.setTitle( "" );
		titleTv.setText( "\u95ee\u7b54\u8be6\u60c5" );
		toolbar.setContentInsetsAbsolute( 0, 0 );
		setSupportActionBar( toolbar );
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled( true );
		}
		toolbar.setNavigationOnClickListener( v -> finish() );
		Drawable drawable = ContextCompat.getDrawable( this, R.drawable.icon_wen );
		DrawableCompat.setTintList( drawable, ColorStateList.valueOf( ContextCompat.getColor( this, R.color.gray_69 ) ) );
		noticeIv.setBackground( drawable );
		noticeIv.setOnClickListener( v -> {
			if (builder == null) {
				builder = new QuestionNoticeDialog.QuestionNoticeBuilder( this );
				builder.setNotices( Constant.getConsultNotice().toArray( new String[Constant.getConsultNotice().size()] ) );
				builder.setProtocols( Constant.getConsultProtocol().toArray( new String[Constant.getConsultProtocol().size()] ) );
			}
			builder.show();
		} );
	}

	private void initView() {
		refreshLayout.setRefreshing( true );
		adapter = new AnswerAdapter( this, models, R.layout.item_answer );
		adapter.setOnItemClickListener( this );
		refreshLayout.setOnRefreshListener( this );
		recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
		recyclerView.addItemDecoration( new SpacesItemDecoration( this,
				LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor( this, R.color.background ) ) );
		recyclerView.setAdapter( adapter );
		recyclerView.setEmptyView( emptyView );
		recyclerView.initLoadMore( this );
		recyclerView.setLoadMoreEnabled( true );
		request( HttpType.REFRESH );
		editText.setOnEditorActionListener( this );
	}

	private void request(int what) {
		HttpManager.consultInfo( what, this, this, consultId, page );
	}

	private void doAnswer(int what, String content) {
		HttpManager.doAnswer( what, this, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				if (1 == resultItem.getIntValue( "status" )) {
					refreshLayout.setRefreshing( true );
					page = 1;
					request( HttpType.REFRESH );
					editText.setText( "" );
					hideSoftInput();
				}
				ToastUtils.showToast( QuestionInfoActivity.this, resultItem.getString( "msg" ) );
			}

			@Override
			public void onError(int what, String error) {
				ToastUtils.showToast( QuestionInfoActivity.this, error );
			}
		}, consultId, content );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		recyclerView.loadMoreComplete();
		if (1 == resultItem.getIntValue( "status" )) {
			if (what == HttpType.REFRESH) {
				models.clear();
			}
			ResultItem data = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( data )) {
				ResultItem info = data.getItem( "consult_info" );
				if (info != null) {
					if (what == HttpType.REFRESH) {
						List<ResultItem> userList = info.getItems( "user_list" );
						if (!BeanUtils.isEmpty( userList )) {
							setUserData( userList );
						}
					}
					List<ResultItem> list = info.getItems( "list" );
					if (!BeanUtils.isEmpty( list )) {
						setData( list );
					} else {
						recyclerView.setLoadMoreEnabled( false );
					}
				}
				if (what == HttpType.REFRESH) {
					ResultItem consult = data.getItem( "consult" );
					int count = 0;
					if (info != null) {
						count = info.getIntValue( "consult_info_counts" );
					}
					setView( consult, count );
				}
			} else {
				recyclerView.setLoadMoreEnabled( false );
			}
			adapter.notifyDataSetChanged();
		} else {
			showToast( resultItem.getString( "msg" ) );
			recyclerView.setLoadMoreEnabled( false );
		}
	}

	private void setData(List<ResultItem> list) {
		for (ResultItem data : list) {
			AnswerModel model = new AnswerModel();
			model.setId( data.getIntValue( "id" ) );
			model.setAnswer( data.getString( "content" ) );

			model.setBest( data.getBooleanValue( "is_reward", 1 ) );
			model.setTop( data.getBooleanValue( "top", 1 ) );
			model.setTaskBonus( data.getBooleanValue( "is_task_bonus", 1 ) );
			UserInfoModel userInfoModel = new UserInfoModel();
			userInfoModel.setHeader( data.getString( "icon_url" ) );
			userInfoModel.setNick( data.getString( "nick_name" ) );
			model.setUserInfoModel( userInfoModel );
			model.setTime( data.getString( "create_time" ) );
			models.add( model );
		}
	}

	private void setUserData(List<ResultItem> list) {
		for (ResultItem data : list) {
			AnswerModel model = new AnswerModel();
			model.setId( data.getIntValue( "id" ) );
			model.setAnswer( data.getString( "content" ) );
			model.setBest( data.getBooleanValue( "is_reward", 1 ) );
			model.setTop( data.getBooleanValue( "top", 1 ) );
			model.setTaskBonus( data.getBooleanValue( "is_task_bonus", 1 ) );
			UserInfoModel userInfoModel = new UserInfoModel();
			userInfoModel.setHeader( data.getString( "icon_url" ) );
			userInfoModel.setNick( data.getString( "nick_name" ) );
			model.setUserInfoModel( userInfoModel );
			model.setTime( data.getString( "create_time" ) );
			models.add( model );
		}
	}

	private void setView(ResultItem consult, int count) {
		ImageLoadUtils.loadCircleImg( this, consult.getString( "icon_url" ), headerIv );
		nickTv.setText( consult.getString( "nick_name" ) );
		questionTv.setText( consult.getString( "content" ) );
		String time = consult.getString( "create_time" );
		if (!TextUtils.isEmpty( time )) {
			try {
				long t = Long.valueOf( time ).longValue() * 1000;
				timeTv.setText( TimeUtils.formatData( t, "yy-MM-dd" ) );
			} catch (NumberFormatException e) {
				timeTv.setText( "" );
			}
		}
		answerCountTv.setText( "\u5171" + count + "\u6761\u73a9\u5bb6\u56de\u7b54" );
		int type = consult.getIntValue( "type" );
		isMyself = TextUtils.equals( consult.getString( "uid" ), SpUtil.getUserId() ) || type == 2;
		if (type == 4) {
			setNoEdit( "\u8fd1\u671f\u73a9\u8fc7\u8be5 \u6e38\u620f\u7684\u73a9\u5bb6\u624d\u53ef\u56de\u7b54\u54df~" );
		} else if (!isMyself) {
			editText.setFocusable( true );
			editText.setFocusableInTouchMode( true );
			editText.requestFocus();
			editText.setHint( "\u73a9\u8fc7\u8be5\u6e38\u620f\uff0c\u6211\u6765\u8bf4\u8bf4~" );
			sendTv.setVisibility( View.VISIBLE );
			sendTv.setOnClickListener( v -> doAnswer() );
		} else {
			bottomView.setVisibility( View.GONE );
			hintTv.setVisibility( View.VISIBLE );
		}
	}

	private void setNoEdit(String hint) {
		Drawable leftDrawable = ContextCompat.getDrawable( this, R.drawable.m_icon_tanhao );
		leftDrawable.setBounds( 0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight() );
		editText.setCompoundDrawables( leftDrawable, null, null, null );
		editText.setFocusable( false );
		sendTv.setVisibility( View.GONE );
		editText.setHint( hint );
	}

	@Override
	public void onError(int what, String error) {
		showToast( error );
		recyclerView.loadMoreComplete();
		recyclerView.setLoadMoreEnabled( false );
	}

	@Override
	public void onItemClick(View itemView, int viewType, int position) {
		if (rationaleBuilder == null) {
			rationaleBuilder = new RationaleDialog.RationaleBuilder( this );
		}
		rationaleBuilder.setMessage( "确认发放悬赏金币给玩家~" );
		rationaleBuilder.setTitle( "发放悬赏" );
		rationaleBuilder.setNegativeButtonText( "取消" );
		rationaleBuilder.setPositiveButtonText( "确认" );
		rationaleBuilder.setOnClickListener( (dialog, which) -> {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				doReward( models.get( position ).getId() );
			}
			rationaleBuilder.dismiss();
		} );
		rationaleBuilder.create();
	}

	@Override
	public void onRefresh() {
		page = 1;
		request( HttpType.REFRESH );
	}

	@Override
	public void onLoadMore() {
		page += 1;
		request( HttpType.LOADING );
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		boolean handled = false;
		if (actionId == EditorInfo.IME_ACTION_SEND) {
			handled = true;
			hideSoftInput();
			doAnswer();
		}
		return handled;
	}

	private void hideSoftInput() {
		/*隐藏软键盘*/
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
		if (inputMethodManager.isActive()) {
			inputMethodManager.hideSoftInputFromWindow( this.getCurrentFocus().getWindowToken(), 0 );
		}
	}

	private void doAnswer() {
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( this, LoginActivity.class ) );
		} else {
			String content = editText.getText().toString();
			if (!TextUtils.isEmpty( content )) {
				doAnswer( 100, content );
			} else {
				ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u8981\u56de\u7b54\u7684\u5185\u5bb9~" );
			}
		}
	}

	private void doReward(int answerId) {
		HttpManager.doReward( HttpType.REFRESH, this, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				ToastUtils.showToast( QuestionInfoActivity.this, resultItem.getString( "msg" ) );
				page = 1;
				refreshLayout.setRefreshing( true );
				request( HttpType.REFRESH );
			}

			@Override
			public void onError(int what, String error) {
				ToastUtils.showToast( QuestionInfoActivity.this, error );
			}
		}, consultId, answerId + "" );
	}
}