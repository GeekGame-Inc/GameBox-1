package com.tenone.gamebox.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.TransferModel;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("HandlerLeak")
public class TransferRecordFragment extends Fragment implements
        OnRefreshListener, OnLoadListener, HttpResultListener {

    @ViewInject(R.id.id_rebate_record_listView)
    ListView listView;
    @ViewInject(R.id.id_rebate_record_refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_empty_root)
    LinearLayout emptyView;

    private Context mContext;
    private CommonAdapter<TransferModel> mAdapter;

    private List<TransferModel> models = new ArrayList<TransferModel>();
    private int page = 1;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater
                .inflate( R.layout.fragment_rebate_record, container, false );
        ViewUtils.inject( this, view );
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView() {
        mAdapter = new CommonAdapter<TransferModel>( mContext, models,
                R.layout.item_transfer_record ) {
            @SuppressWarnings("deprecation")
            @Override
            public void convert(CommonViewHolder holder, TransferModel t) {
                holder.setText( R.id.id_item_transfer_record_newGameName,
                        t.getNewGameName() );
                holder.setText( R.id.id_item_transfer_record_oldGameName,
                        t.getOldGameName() );
                holder.setText( R.id.id_item_transfer_record_newServer,
                        t.getNewServer() );
                holder.setText( R.id.id_item_transfer_record_oldServer,
                        t.getOldServer() );
                holder.setText( R.id.id_item_transfer_record_newRoleName,
                        t.getNewRoleName() );
                holder.setText( R.id.id_item_transfer_record_oldRoleName,
                        t.getOldRoleName() );
                holder.setText( R.id.id_item_transfer_record_time,
                        t.getTransferTime() );
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
                holder.setBackgroundDra( R.id.id_item_transfer_record_state, drawable );
            }
        };
        listView.setAdapter( mAdapter );
        listView.setEmptyView( emptyView );
        refreshLayout.setOnRefreshListener( this );
        refreshLayout.setOnLoadListener( this );
        request( HttpType.REFRESH, page );
    }

    private void request(int what, int page) {
        HttpManager.changegameLog( what, mContext, this, page );
    }

    @Override
    public void onLoad() {
        page += 1;
        request( HttpType.LOADING, page );
    }

    @Override
    public void onRefresh() {
        page = 1;
        request( HttpType.REFRESH, page );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        if (1 == resultItem.getIntValue( "status" )) {
            if (HttpType.REFRESH == what) {
                models.clear();
            }
            ResultItem item = resultItem.getItem( "data" );
            setData( item );
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

    private void setData(ResultItem item) {
        List<ResultItem> items = item.getItems( "list" );
        if (item != null) {
            for (int i = 0; i < items.size(); i++) {
                ResultItem resultItem = items.get( i );
                TransferModel model = new TransferModel();
                model.setNewGameName( resultItem.getString( "new_appname" ) );
                model.setNewRoleName( resultItem.getString( "new_rolename" ) );
                model.setNewServer( resultItem.getString( "new_servername" ) );
                model.setOldGameName( resultItem.getString( "origin_appname" ) );
                model.setOldServer( resultItem.getString( "origin_servername" ) );
                model.setOldRoleName( resultItem.getString( "origin_rolename" ) );
                model.setTransferTime( resultItem.getString( "create_time" ) );
                model.setState( resultItem.getIntValue( "status" ) );
                models.add( model );
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
