package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.john.superadapter.OnItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnLoadMoreListener;
import com.tenone.gamebox.mode.mode.GameQuestionModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.QuestionModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.activity.QuestionInfoActivity;
import com.tenone.gamebox.view.adapter.MyQuestionAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.LoadMoreRecyclerView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MyQuestionFragment extends BaseLazyFragment implements HttpResultListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
	@ViewInject(R.id.id_fragment_question_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_fragment_question_list)
	LoadMoreRecyclerView recyclerView;
	@ViewInject(R.id.id_empty_iv)
	ImageView emptyView;
	private int type = 1, page = 1;
	private List<GameQuestionModel> models = new ArrayList<>();
	private MyQuestionAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		type = bundle.getInt( "type" );
		View view = inflater.inflate( R.layout.fragment_question, container, false );
		ViewUtils.inject( this, view );
		initView();
		return view;
	}


	private void initView() {
		adapter = new MyQuestionAdapter( getActivity(), models, R.layout.item_my_question );
		adapter.setOnItemClickListener( this );
		refreshLayout.setOnRefreshListener( this );
		recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
		recyclerView.setAdapter( adapter );
		recyclerView.setEmptyView( emptyView );
		recyclerView.initLoadMore( this );
		recyclerView.setLoadMoreEnabled( true );
	}

	@Override
	public void onLazyLoad() {
		refreshLayout.setRefreshing( true );
		request( HttpType.REFRESH );
	}

	private void request(int what) {
		if (type == 1) {
			HttpManager.answerGame( what, getActivity(), this, page );
		} else {
			HttpManager.myQuestions( what, getActivity(), this, page );
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		recyclerView.loadMoreComplete();
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem data = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( data )) {
				if (type == 1) {
					answerResult( what, data );
				} else {
					questionResult( what, data );
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
		recyclerView.setLoadMoreEnabled( false );
		ToastUtils.showToast( getActivity(), error );
	}

	private void answerResult(int what, ResultItem data) {
		if (what == HttpType.REFRESH) {
			models.clear();
		}
		List<ResultItem> list = data.getItems( "list" );
		if (!BeanUtils.isEmpty( list )) {
			for (ResultItem item : list) {
				GameQuestionModel model = new GameQuestionModel();
				model.setGameIcon( item.getString( "logo" ) );
				model.setGameName( item.getString( "game_name" ) );
				model.setGameId( item.getIntValue( "appid" ) );
				QuestionModel questionModel = new QuestionModel();
				questionModel.setQuestionId( item.getString( "id" ) );
				questionModel.setTime( item.getString( "create_time" ) );
				questionModel.setQuestion( item.getString( "content" ) );
				questionModel.setNum( item.getIntValue( "answer_counts" ) );
				model.setQuestionModel( questionModel );
				models.add( model );
			}
		} else {
			recyclerView.setLoadMoreEnabled( false );
		}
	}

	private void questionResult(int what, ResultItem data) {
		if (what == HttpType.REFRESH) {
			models.clear();
		}
		List<ResultItem> list = data.getItems( "list" );
		if (!BeanUtils.isEmpty( list )) {
			for (ResultItem item : list) {
				GameQuestionModel model = new GameQuestionModel();
				model.setGameIcon( item.getString( "logo" ) );
				model.setGameId( item.getIntValue( "appid" ) );
				model.setGameName( item.getString( "name" ) );
				QuestionModel questionModel = new QuestionModel();
				questionModel.setQuestionId( item.getString( "id" ) );
				questionModel.setTime( item.getString( "time" ) );
				questionModel.setQuestion( item.getString( "question" ) );
				questionModel.setNum( item.getIntValue( "anwsers" ) );
				model.setQuestionModel( questionModel );
				models.add( model );
			}
		} else {
			recyclerView.setLoadMoreEnabled( false );
		}
	}

	@Override
	public void onItemClick(View itemView, int viewType, int position) {
		startActivity( new Intent( getActivity(), QuestionInfoActivity.class )
				.putExtra( "consultId", models.get( position ).getQuestionModel().getQuestionId() ) );
	}

	@Override
	public void onRefresh() {
		recyclerView.setLoadMoreEnabled( true );
		page = 1;
		request( HttpType.REFRESH );
	}

	@Override
	public void onLoadMore() {
		page += 1;
		request( HttpType.LOADING );
	}
}
