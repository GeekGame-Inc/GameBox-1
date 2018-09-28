package com.tenone.gamebox.view.activity;

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
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.superadapter.OnItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnLoadMoreListener;
import com.tenone.gamebox.mode.mode.AnswerModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.QuestionModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.QuestionsAndAnswerAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.custom.LoadMoreRecyclerView;
import com.tenone.gamebox.view.custom.dialog.QuestionNoticeDialog;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAndAnswerActivity extends BaseActivity implements Runnable, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, HttpResultListener {
	@ViewInject(R.id.id_question_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_question_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_question_list)
	LoadMoreRecyclerView recyclerView;

	@ViewInject(R.id.id_question_title)
	TextView titleTv;
	@ViewInject(R.id.id_question_notice)
	ImageView noticeIv;
	@ViewInject(R.id.id_question_icon)
	ImageView iconIv;
	@ViewInject(R.id.id_question_name)
	TextView nameTv;

	@ViewInject(R.id.id_question_player)
	TextView playerTv;
	@ViewInject(R.id.id_question_answer)
	TextView answerTv;
	@ViewInject(R.id.id_empty_iv)
	ImageView emptyView;


	private GameModel gameModel;
	private QuestionsAndAnswerAdapter adapter;
	private List<QuestionModel> models = new ArrayList<QuestionModel>();
	private int page = 1;
	private QuestionNoticeDialog.QuestionNoticeBuilder builder;
	private String[] reward;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_game_question );
		ViewUtils.inject( this );
		gameModel = (GameModel) getIntent().getExtras().get( "gameModel" );
		initTitle();
		if (gameModel != null) {
			initView();
		}
	}

	private void initTitle() {
		toolbar.setTitle( "" );
		titleTv.setText( "\u6e38\u620f\u95ee\u7b54" );
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
		ImageLoadUtils.loadImg( iconIv, this, gameModel.getImgUrl() );
		nameTv.setText( gameModel.getName() );
		new Thread( this ).start();
	}

	private Spanned getPlayerTxt(int player) {
		String text = getString( R.string.player_txt, player + "" );
		return Html.fromHtml( text );
	}

	private Spanned getQuestionTxt(int question, int answer) {
		String text = getString( R.string.question_txt2, question + "", answer + "" );
		return Html.fromHtml( text );
	}

	@OnClick({R.id.id_question_consult})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_question_consult:
				if (!BeanUtils.isLogin()) {
					startActivity( new Intent( this, LoginActivity.class ) );
					return;
				}
				startActivityForResult( new Intent( this, ReleaseQuestionActivity.class )
						.putExtra( "rewards", reward )
						.putExtra( "gameId", gameModel.getGameId() + "" )
						.putExtra( "num", gameModel.getPlayers() ), 2000 );
				overridePendingTransition( R.anim.bottom_in, R.anim.bottom_out );
				break;
		}
	}

	@Override
	public void run() {
		Spanned player = getPlayerTxt( gameModel.getPlayers() );
		Spanned question = getQuestionTxt( gameModel.getQuestions(), gameModel.getAnswers() );
		runOnUiThread( () -> {
			adapter = new QuestionsAndAnswerAdapter( this, models, R.layout.item_question );
			adapter.setOnItemClickListener( this );
			refreshLayout.setOnRefreshListener( this );
			recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
			recyclerView.setAdapter( adapter );
			recyclerView.setEmptyView( emptyView );
			recyclerView.initLoadMore( this );
			recyclerView.setLoadMoreEnabled( true );
			playerTv.setText( player );
			answerTv.setText( question );
			request( HttpType.REFRESH );
		} );
	}

	private void request(int what) {
		HttpManager.consultList( what, this, this, page, gameModel.getGameId() + "" );
	}

	private void setQuestionsModel(List<ResultItem> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ResultItem item = list.get( i );
			QuestionModel model = new QuestionModel();
			model.setQuestion( item.getString( "content" ) );
			model.setQuestionId( item.getString( "id" ) );
			model.setReward( item.getIntValue( "money" ) );
			model.setType( item.getIntValue( "type" ) );
			model.setTime( item.getString( "create_time" ) );
			model.setNum( item.getIntValue( "answer_count" ) );
			model.setCoinsIsReceived( item.getBooleanValue( "type", 2 ) );
			model.setCoin( item.getIntValue( "money" ) );
			List<ResultItem> answers = item.getItems( "answer" );
			if (!BeanUtils.isEmpty( answers )) {
				List<AnswerModel> array = new ArrayList<AnswerModel>();
				for (ResultItem answer : answers) {
					AnswerModel answerModel = new AnswerModel();
					answerModel.setAnswer( answer.getString( "content" ) );
					array.add( answerModel );
				}
				model.setAnswers( array );
			}
			models.add( model );
		}
	}

	private void setMineQuestionsModel(List<ResultItem> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ResultItem item = list.get( i );
			QuestionModel model = new QuestionModel();
			model.setQuestion( item.getString( "content" ) );
			model.setQuestionId( item.getString( "id" ) );
			model.setReward( item.getIntValue( "money" ) );
			model.setType( item.getIntValue( "type" ) );
			model.setTime( item.getString( "create_time" ) );
			model.setNum( item.getIntValue( "answer_count" ) );
			model.setCoinsIsReceived( item.getBooleanValue( "type", 2 ) );
			model.setCoin( item.getIntValue( "money" ) );
			List<ResultItem> answers = item.getItems( "answer" );
			if (!BeanUtils.isEmpty( answers )) {
				List<AnswerModel> array = new ArrayList<AnswerModel>();
				for (ResultItem answer : answers) {
					AnswerModel answerModel = new AnswerModel();
					answerModel.setAnswer( answer.getString( "content" ) );
					array.add( answerModel );
				}
				model.setAnswers( array );
			}
			models.add( model );
		}
	}

	@Override
	public void onLoadMore() {
		page += 1;
		request( HttpType.LOADING );
	}

	@Override
	public void onRefresh() {
		recyclerView.setLoadMoreEnabled( true );
		page = 1;
		request( HttpType.REFRESH );
	}

	@Override
	public void onItemClick(View itemView, int viewType, int position) {
		startActivityForResult( new Intent( this, QuestionInfoActivity.class )
				.putExtra( "consultId", models.get( position ).getQuestionId() ), 500 );
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
				if (what == HttpType.REFRESH) {
					List<ResultItem> conf = data.getItems( "conf" );
					if (!BeanUtils.isEmpty( conf )) {
						reward = new String[conf.size()];
						for (int i = 0; i < conf.size(); i++) {
							reward[i] = String.valueOf( conf.get( i ) );
						}
					}
					List<ResultItem> mine = data.getItems( "user" );
					if (!BeanUtils.isEmpty( mine )) {
						setMineQuestionsModel( mine );
					}
				}
				List<ResultItem> list = data.getItems( "list" );
				if (BeanUtils.isEmpty( list )) {
					recyclerView.setLoadMoreEnabled( false );
				} else {
					setQuestionsModel( list );
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

	@Override
	public void onError(int what, String error) {
		showToast( error );
		recyclerView.loadMoreComplete();
		recyclerView.setLoadMoreEnabled( false );
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult( requestCode, resultCode, data );
		if ((requestCode == 2000 || requestCode == 500) && resultCode == RESULT_OK) {
			refreshLayout.setRefreshing( true );
			recyclerView.setLoadMoreEnabled( true );
			page = 1;
			request( HttpType.REFRESH );
		}
	}
}
