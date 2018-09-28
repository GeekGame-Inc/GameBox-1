package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StrategyMode;
import com.tenone.gamebox.mode.view.GameDetailsFragmentView;
import com.tenone.gamebox.presenter.GameDetailsFragmentPresenter;
import com.tenone.gamebox.view.activity.RebateActivity;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.adapter.EventAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.MoreTextView;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;
public class GameDetailsFragment extends BaseLazyFragment implements
        GameDetailsFragmentView, AdapterView.OnItemClickListener, HttpResultListener {
    View view;
    @ViewInject(R.id.id_game_scrollView)
    NestedScrollView scrollView;

    @ViewInject(R.id.id_fragment_details_imgs)
    RecyclerView recyclerView;

    @ViewInject(R.id.id_fragment_details_layout1)
    RelativeLayout linearLayout1;

    @ViewInject(R.id.id_fragment_details_layout2)
    LinearLayout linearLayout2;

    @ViewInject(R.id.id_fragment_details_layout3)
    LinearLayout linearLayout3;

    @ViewInject(R.id.id_fragment_details_expandableTextView)
    MoreTextView moreTextView;

    @ViewInject(R.id.id_fragment_details_expandableTextView1)
    MoreTextView moreTextView1;

    @ViewInject(R.id.id_fragment_details_expandableTextView2)
    MoreTextView moreTextView2;

    @ViewInject(R.id.id_fragment_details_expandableTextView3)
    MoreTextView moreTextView3;

    @ViewInject(R.id.id_fragment_details_gridView)
    MyGridView gridView;

    @ViewInject(R.id.id_fragment_details_publishComment)
    TextView publishComment;

    @ViewInject(R.id.id_fragment_details_commentList)
    MyListView commentListView;

    @ViewInject(R.id.id_fragment_details_gifIV)
    ImageView imageView;
    @ViewInject(R.id.id_fragment_details_play)
    ImageView playIV;

    @ViewInject(R.id.id_fragment_details_event)
    MyListView myListView;

    @ViewInject(R.id.id_fragment_details_layout9)
    LinearLayout eventLayout;
    @ViewInject(R.id.id_fragment_details_check)
    TextView checkTv;
    @ViewInject(R.id.id_fragment_details_sqfl)
    TextView sqflTv;
    GameDetailsFragmentPresenter presenter;

    ResultItem resultItem;
    String gameId;
    public NestedScrollView scrollView2;
    private String action;

    private EventAdapter adapter;

    public void setAction(String action) {
        this.action = action;
    }

    private List<StrategyMode> models = new ArrayList<StrategyMode>();

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from( getActivity() ).inflate(
                    R.layout.fragment_details_game, container, false );
        }
        ViewUtils.inject( this, view );
        scrollView2 = scrollView;
        recyclerView.setNestedScrollingEnabled( false );
        Bundle bundle = getArguments();
        resultItem = (ResultItem) bundle.get( "resultItem" );
        gameId = bundle.getString( "id" );
        presenter = new GameDetailsFragmentPresenter( getActivity(), this );
        presenter.initView();
        presenter.setAdapter();
        presenter.initListener();
        adapter = new EventAdapter( models, getActivity() );
        myListView.setAdapter( adapter );
        myListView.setOnItemClickListener( this );
        sqflTv.setOnClickListener( v -> startActivity( new Intent( getActivity(), RebateActivity.class ) ) );
        return view;
    }

    @Override
    public RecyclerView getImgsView() {
        return recyclerView;
    }

    @Override
    public MyGridView getGridView() {
        return gridView;
    }

    @Override
    public MoreTextView getMoreTextView() {
        return moreTextView;
    }

    @Override
    public ResultItem getResultItem() {
        return resultItem;
    }


    @Override
    public TextView getPublishBt() {
        return publishComment;
    }

    @Override
    public TextView getCheckTv() {
        return checkTv;
    }

    @Override
    public MyListView getCommentListView() {
        return commentListView;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public MoreTextView getFeatureMoreTextView() {
        return moreTextView1;
    }

    @Override
    public MoreTextView getRebateMoreTextView() {
        return moreTextView2;
    }

    @Override
    public MoreTextView getVipMoreTextView() {
        return moreTextView3;
    }

    @Override
    public RelativeLayout getRebateLayout() {
        return linearLayout1;
    }

    @Override
    public LinearLayout getVipLayout() {
        return linearLayout2;
    }

    @Override
    public ImageView getGifImageView() {
        return imageView;
    }

    @Override
    public LinearLayout getGifLayout() {
        return linearLayout3;
    }

    @Override
    public ImageView getGifPalyImageView() {
        return playIV;
    }

    @Override
    public NestedScrollView getNestedScrollView() {
        return scrollView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity( new Intent( getActivity(), WebActivity.class )
                .putExtra( "title", "�����" )
                .putExtra( "url", models.get( position ).getUrl() ) );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if (0 == resultItem.getIntValue( "status" )) {
            ResultItem data = resultItem.getItem( "data" );
            if (!BeanUtils.isEmpty( data )) {
                List<ResultItem> items = data.getItems( "list" );
                if (!BeanUtils.isEmpty( items )) {
                    setData( items );
                }
            }
        }
    }

    @Override
    public void onError(int what, String error) {
    }

    public void setData(List<ResultItem> data) {
        eventLayout.setVisibility( View.VISIBLE );
        for (ResultItem item : data) {
            StrategyMode mode = new StrategyMode();
            mode.setUrl( item.getString( "info_url" ) );
            mode.setStrategyName( item.getString( "title" ) );
            mode.setTime( item.getString( "release_time" ) );
            mode.setStrategyImgUrl( item.getString( "pic" ) );
            models.add( mode );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLazyLoad() {
        HttpManager.articleListByGame( 1, getActivity(), this, 2, 1, gameId );
    }
}
