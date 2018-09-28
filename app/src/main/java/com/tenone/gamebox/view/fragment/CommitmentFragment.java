package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.CommitmentModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.CommitmentAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class CommitmentFragment extends BaseLazyFragment implements HttpResultListener, OnRecyclerViewItemClickListener<CommitmentModel>, SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.id_commitment_img)
    ImageView imageView;
    @ViewInject(R.id.id_commitment_recy)
    RecyclerView recyclerView;
    @ViewInject(R.id.id_commitment_scroll)
    NestedScrollView scrollView;
    @ViewInject(R.id.id_commitment_refresh)
    SwipeRefreshLayout refreshLayout;

    private CommitmentAdapter adapter;
    private List<CommitmentModel> models = new ArrayList<CommitmentModel>();

    @Override
    public void onLazyLoad() {
        refreshLayout.setRefreshing( true );
        HttpManager.getCommitment( getActivity(), 0, this );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_commitment, container, false );
        ViewUtils.inject( this, view );
        recyclerView.setNestedScrollingEnabled( false );
        scrollView.setNestedScrollingEnabled( true );
        adapter = new CommitmentAdapter( models, getActivity() );
        adapter.setOnRecyclerViewItemClickListener( this );
        LinearLayoutManager manager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
        recyclerView.setLayoutManager( manager );
        recyclerView.setAdapter( adapter );
        refreshLayout.setOnRefreshListener( this );
        return view;
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        if (1 == resultItem.getIntValue( "status" )) {
            ResultItem data = resultItem.getItem( "data" );
            setData( data );
        } else {
            ToastUtils.showToast( getActivity(), resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
        ToastUtils.showToast( getActivity(), error );
    }


    private void setData(ResultItem data) {
        models.clear();
        ImageLoadUtils.loadCommitmentImg( imageView, getActivity(), data.getString( "pic" ) );
        List<ResultItem> items = data.getItems( "list" );
        if (!BeanUtils.isEmpty( items )) {
            for (ResultItem item : items) {
                CommitmentModel model = new CommitmentModel();
                model.setTitle( item.getString( "title" ) );
                model.setContent( item.getString( "content" ) );
                model.setqQ( item.getString( "qq" ) );
                models.add( model );
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRecyclerViewItemClick(CommitmentModel commitmentModel) {
        startQQ( commitmentModel.getqQ() );
    }

    private void startQQ(String qq) {
        if (ApkUtils.checkAppExist( getActivity(), "com.tencent.mobileqq" )) {
            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;// uin�Ƿ��͹�ȥ��qq����
                startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
            } catch (Exception e) {
                ToastUtils.showToast( getActivity(), "�����QQ����" );
            }
        } else {
            ToastUtils.showToast( getActivity(), "�����Ƿ�װ��QQ�ͻ���" );
        }
    }

    @Override
    public void onRefresh() {
        HttpManager.getCommitment( getActivity(), 0, this );
    }
}
