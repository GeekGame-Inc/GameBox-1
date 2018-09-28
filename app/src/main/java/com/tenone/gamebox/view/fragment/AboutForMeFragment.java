package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDynamicTabChangeListener;
import com.tenone.gamebox.mode.listener.OnHeaderClickListener;
import com.tenone.gamebox.mode.listener.OnLoginStateChangeListener;
import com.tenone.gamebox.mode.listener.OnTypeSelecteListener;
import com.tenone.gamebox.mode.mode.AboutForMeCommentModel;
import com.tenone.gamebox.mode.mode.AboutForMeModel;
import com.tenone.gamebox.mode.mode.AboutForMePublicModel;
import com.tenone.gamebox.mode.mode.AboutForMeZanCommenModel;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.presenter.DrivingPresenter;
import com.tenone.gamebox.view.activity.DynamicDetailsActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.UserInfoActivity;
import com.tenone.gamebox.view.adapter.AboutForMeAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.popupwindow.AboutMeTypeWindow;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.ArrayList;
import java.util.List;


public class AboutForMeFragment extends BaseLazyFragment implements
        SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, HttpResultListener, AdapterView.OnItemClickListener, OnHeaderClickListener, OnTypeSelecteListener, PopupWindow.OnDismissListener, OnLoginStateChangeListener, OnDynamicTabChangeListener {
    @ViewInject(R.id.id_forMe_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_forMe_listView)
    MyRefreshListView listView;
    @ViewInject(R.id.id_empty_tv)
    TextView emptyTv;
    @ViewInject(R.id.id_empty_root)
    View emptyView;
    private TextView classflyTv;
    private AboutForMeAdapter adapter;
    private AboutForMeModel model;
    private int page = 1;
    private int messageType = 1;
    private String[] typeArray = {"\u6211\u7684\u56de\u590d", "\u88ab\u8d5e/\u8e29\u7684\u8bc4\u8bba", "\u88ab\u8d5e/\u8e29\u7684\u52a8\u6001"};
    private AboutMeTypeWindow window;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_about_for_me, container, false );
        ViewUtils.inject( this, view );
        model = new AboutForMeModel();
        initView();
        return view;
    }

    private void initView() {
        ListenerManager.registerOnTypeSelecteListener( this );
        ListenerManager.registerOnLoginStateChangeListener( this );
        adapter = new AboutForMeAdapter( model, getActivity() );
        initHeader();
        listView.setAdapter( adapter );
        if (DrivingPresenter.getInstance() != null) {
            messageType = DrivingPresenter.getInstance().getMessageType();
            classflyTv.setText( typeArray[(messageType - 1)] );
        }
        refreshLayout.setOnRefreshListener( this );
        listView.setOnItemClickListener( this );
        adapter.setOnHeaderClickListener( this );
        ListenerManager.registerOnDynamicTabChangeListener( this );
    }

    private void initHeader() {
        View view = LayoutInflater.from( getActivity() )
                .inflate( R.layout.layout_about_for_me_header, null, false );
        classflyTv = view.findViewById( R.id.id_forMe_classfly );
        classflyTv.setOnClickListener( v -> {
            if (!classflyTv.isSelected()) {
                classflyTv.setSelected( true );
                if (window == null) {
                    window = new AboutMeTypeWindow( getActivity(), typeArray, messageType );
                    window.setOnDismissListener( AboutForMeFragment.this );
                }
                window.showAsDropDown( classflyTv, 0, -30 );
            } else {
                if (window != null) {
                    window.dismiss();
                    window = null;
                }
            }
        } );
        listView.addHeaderView( view );

        listView.setOnTouchListener( (v, event) -> {
            if (window != null) {
                window.dismiss();
                window = null;
                classflyTv.setSelected( false );
            }
            return false;
        } );
    }

    @Override
    public void onRefresh() {
        if (BeanUtils.isLogin()) {
            if (DrivingPresenter.getInstance() != null) {
                messageType = DrivingPresenter.getInstance().getMessageType();
                classflyTv.setText( typeArray[(messageType - 1)] );
            }
            page = 1;
            refreshData( HttpType.REFRESH );
        } else {
            model.clearCommentModels();
            model.clearZanCommenModels();
            model.clearZanDynamicModels();
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing( false );
        }
    }

    @Override
    public void onLoad() {
        if (BeanUtils.isLogin()) {
            page++;
            refreshData( HttpType.LOADING );
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        if (1 == resultItem.getIntValue( "status" )) {
            ResultItem item = resultItem.getItem( "data" );
            if (what == HttpType.REFRESH) {
                switch (messageType) {
                    case 1:
                        model.clearCommentModels();
                        break;
                    case 2:
                        model.clearZanCommenModels();
                        break;
                    case 3:
                        model.clearZanDynamicModels();
                        break;
                }
            }
            if (item != null) {
                setData( item );
                emptyView.setVisibility( View.GONE );
            } else {
                emptyView.setVisibility( what == HttpType.REFRESH ? View.VISIBLE : View.GONE );
            }
        } else {
            Log.i( "onSuccess", resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
    }


    private void setData(ResultItem item) {
        List<ResultItem> items = item.getItems( "list" );
        if (items != null) {
            switch (messageType) {
                case 1:
                    setData1( items );
                    break;
                case 2:
                    setData2( items );
                    break;
                case 3:
                    setData3( items );
                    break;
            }
            adapter.setType( messageType );
            adapter.notifyDataSetChanged();
        }
    }

    private void setData1(List<ResultItem> items) {
        List<AboutForMeCommentModel> commentModels = new ArrayList<AboutForMeCommentModel>();
        for (ResultItem item : items) {
            AboutForMeCommentModel model = new AboutForMeCommentModel();
            model.setcContent( item.getString( "c_content" ) );
            model.setcId( item.getString( "comment_id" ) );
            model.setcUId( item.getString( "c_uid" ) );
            model.setcUNick( item.getString( "c_uid_nickname" ) );
            model.setcUHedaer( item.getString( "c_uid_iconurl" ) );
            model.setcUIsVip( item.getBooleanValue( "c_uid_vip", 1 ) );
            model.setDyContent( item.getString( "d_content" ) );
            model.setDyId( item.getString( "dynamics_id" ) );
            model.setDyImg( item.getString( "d_img" ) );
            model.setDyUId( item.getString( "d_uid" ) );
            model.setDyUNick( item.getString( "d_uid_nickname" ) );
            model.setTime( item.getString( "create_time" ) );
            model.settUserId( item.getString( "c_touid" ) );
            model.settNick( item.getString( "c_touid_nickname" ) );
            commentModels.add( model );
        }
        model.setCommentModels( commentModels );
    }

    private void setData2(List<ResultItem> items) {
        List<AboutForMeZanCommenModel> zanCommenModels = new ArrayList<AboutForMeZanCommenModel>();
        for (ResultItem item : items) {
            AboutForMeZanCommenModel model = new AboutForMeZanCommenModel();
            model.setType( item.getIntValue( "type" ) );

            model.setcUId( item.getString( "cl_uid" ) );
            model.setcUHedaer( item.getString( "cl_uid_iconurl" ) );
            model.setcUNick( item.getString( "cl_uid_nickname" ) );
            model.setcUIsVip( item.getBooleanValue( "cl_uid_vip", 1 ) );
            model.setcId( item.getString( "comment_id" ) );

            model.setDyContent( item.getString( "d_content" ) );
            model.setDyId( item.getString( "dynamics_id" ) );
            model.setDyImg( item.getString( "d_img" ) );
            model.setDyUId( item.getString( "d_uid" ) );
            model.setDyUNick( item.getString( "d_uid_nickname" ) );
            model.setTime( item.getString( "create_time" ) );

            model.settUId( item.getString( "c_touid" ) );
            model.settUNick( item.getString( "c_touid_nickname" ) );

            model.setcDyUId( item.getString( "c_uid" ) );
            model.setcDyUNick( item.getString( "c_uid_nickname" ) );
            model.setcDyContent( item.getString( "c_content" ) );
            zanCommenModels.add( model );
        }
        model.setZanCommenModels( zanCommenModels );
    }

    private void setData3(List<ResultItem> items) {
        List<AboutForMePublicModel> zanDynamicModels = new ArrayList<AboutForMePublicModel>();
        for (ResultItem item : items) {
            AboutForMePublicModel model = new AboutForMePublicModel();
            model.setType( item.getIntValue( "type" ) );
            model.setcUId( item.getString( "dl_uid" ) );
            model.setcUHedaer( item.getString( "dl_uid_iconurl" ) );
            model.setcUNick( item.getString( "dl_uid_nickname" ) );
            model.setcUIsVip( item.getBooleanValue( "dl_uid_vip", 1 ) );
            model.setTime( item.getString( "create_time" ) );
            model.setDyId( item.getString( "dynamics_id" ) );
            model.setDyUId( item.getString( "d_uid" ) );
            model.setDyUNick( item.getString( "d_uid_nickname" ) );
            model.setDyContent( item.getString( "d_content" ) );
            model.setDyImg( item.getString( "d_img" ) );
            zanDynamicModels.add( model );
        }
        model.setZanDynamicModels( zanDynamicModels );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (model != null) {
            if (position > 0) {
                String dynamicId = "";
                switch (messageType) {
                    case 1:
                        dynamicId = model.getCommentModels().get( position - 1 ).getDyId();
                        break;
                    case 2:
                        dynamicId = model.getZanCommenModels().get( position - 1 ).getDyId();
                        break;
                    case 3:
                        dynamicId = model.getZanDynamicModels().get( position - 1 ).getDyId();
                        break;
                }
                startActivity( new Intent( getActivity(), DynamicDetailsActivity.class )
                        .putExtra( "dynamicId", dynamicId ) );
            }
        }
    }

    @Override
    public void onHeaderClick(DriverModel model) {
        if (BeanUtils.isLogin()) {
            startActivity( new Intent( getActivity(), UserInfoActivity.class )
                    .putExtra( "uid", model.getDriverId() ) );
        } else {
            startActivity( new Intent( getActivity(), LoginActivity.class ) );
        }
    }

    @Override
    public void onTypeSelect(int type) {
        messageType = type;
        classflyTv.setText( typeArray[type - 1] );
        page = 1;
        if (BeanUtils.isLogin()) {
            refreshData( HttpType.REFRESH );
        }
    }

    private void refreshData(int refresh) {
        HttpManager.myCommentZan( refresh, getActivity(), AboutForMeFragment.this, messageType, page );
    }

    @Override
    public void onPause() {
        classflyTv.setSelected( false );
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
        window = null;
        super.onPause();
    }


    @Override
    public void onDismiss() {
        classflyTv.setSelected( false );
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
        window = null;
    }


    @Override
    public void onLoginStateChange(boolean isLogin) {
        model.clearCommentModels();
        model.clearZanCommenModels();
        model.clearZanDynamicModels();
        if (!isLogin) {
            emptyTv.setText( "\u60a8\u8fd8\u672a\u767b\u5f55\uff0c\u8bf7\u524d\u5f80\u4e2a\u4eba\u4e2d\u5fc3\u767b\u5f55" );
            emptyView.setVisibility( View.VISIBLE );
            adapter.notifyDataSetChanged();
        } else {
            emptyTv.setText( "\u6682\u65e0\u6570\u636e" );
            refreshLayout.setRefreshing( true );
            if (DrivingPresenter.getInstance() != null) {
                messageType = DrivingPresenter.getInstance().getMessageType();
                classflyTv.setText( typeArray[(messageType - 1)] );
            }
            page = 1;
            refreshData( HttpType.REFRESH );
            classflyTv.setSelected( false );
            if (window != null && window.isShowing()) {
                window.dismiss();
            }
        }
    }

    @Override
    public void onDynamicTabChange() {
        classflyTv.setSelected( false );
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
        window = null;
    }

    @Override
    public void onDestroy() {
        ListenerManager.unRegisterOnDynamicTabChangeListener( this );
        super.onDestroy();
    }

    @Override
    public void onLazyLoad() {
        if (BeanUtils.isLogin()) {
            refreshLayout.setRefreshing( true );
            refreshData( HttpType.REFRESH );
            refreshLayout.setOnLoadListener( this );
        } else {
            emptyTv.setText( "\u60a8\u8fd8\u672a\u767b\u5f55\uff0c\u8bf7\u524d\u5f80\u4e2a\u4eba\u4e2d\u5fc3\u767b\u5f55" );
        }
    }
}
