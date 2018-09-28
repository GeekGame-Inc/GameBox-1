package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DynamicOperationListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDataRefreshListener;
import com.tenone.gamebox.mode.listener.OnHeaderClickListener;
import com.tenone.gamebox.mode.listener.OnLoginStateChangeListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.activity.DynamicDetailsActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.UserInfoActivity;
import com.tenone.gamebox.view.adapter.DynamicAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


public class ImageDynamicFragment extends BaseLazyFragment implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, DynamicOperationListener, OnHeaderClickListener, AdapterView.OnItemClickListener, PlatformActionListener, OnLoginStateChangeListener, OnDataRefreshListener {
    @ViewInject(R.id.id_dynamic_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_dynamic_listView)
    MyRefreshListView listView;
    @ViewInject(R.id.id_empty_root)
    View emptyView;

    private List<DynamicModel> models = new ArrayList<DynamicModel>();
    private DynamicAdapter adapter;
    private int page = 1, dp20, scrWidth;
    private boolean hasData = true;
    private static final int PRAISE = 58;
    private static final int CANCLEPRAISE = 60;
    private static final int STEPON = 59;
    private static final int CANCLESTEPON = 61;

    private DynamicModel currentModel;
    private SharePopupWindow sharePopupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_dynamic, container, false );
        ViewUtils.inject( this, view );
        dp20 = DisplayMetricsUtils.dipTopx( getActivity(), 20 );
        scrWidth = DisplayMetricsUtils.getScreenWidth( getActivity() );
        initView();
        return view;
    }


    private void initView() {
        adapter = new DynamicAdapter( models, getActivity() );
        adapter.setShowAttention( true );
        adapter.setListener( this );
        adapter.setOnHeaderClickListener( this );
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( this );
        refreshLayout.setOnRefreshListener( this );
        refreshLayout.setOnLoadListener( this );
        ListenerManager.registerOnLoginStateChangeListener( this );
        ListenerManager.registerOnDataRefreshListener( this );
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        if (1 == resultItem.getIntValue( "status" )) {
            if (what == HttpType.REFRESH) {
                models.clear();
            }
            List<ResultItem> items = resultItem.getItems( "data" );
            if (!BeanUtils.isEmpty( items )) {
                setData( items );
            } else {
                emptyView.setVisibility( what == HttpType.REFRESH ? View.VISIBLE : View.GONE );
            }
        } else {
            if (HttpType.LOADING == what) {
                hasData = false;
                page--;
            }
        }
    }

    private void setData(List<ResultItem> items) {
        for (ResultItem item : items) {
            ResultItem dynamics = item.getItem( "dynamics" );
            DynamicModel model = new DynamicModel();
            model.setComments( dynamics.getString( "comment" ) );
            model.setContent( dynamics.getString( "content" ) );
            model.setTop( dynamics.getBooleanValue( "top", 1 ) );
            model.setLikes( dynamics.getString( "likes" ) );
            model.setDislikes( dynamics.getString( "dislike" ) );
            model.setTime( dynamics.getString( "create_time" ) );
            model.setDynamicModelId( dynamics.getString( "id" ) );
            model.setLeavel( dynamics.getIntValue( "level" ) );
            model.setRemark( dynamics.getString( "remark" ) );
            List<ResultItem> imgs = dynamics.getItems( "imgs" );
            model.setShares( dynamics.getString( "share" ) );
            if (null != imgs && imgs.size() > 0) {
                ArrayList<String> imgsArray = new ArrayList<String>();
                for (int i = 0; i < imgs.size(); i++) {
                    String img = String.valueOf( imgs.get( i ) );
                    imgsArray.add( String.valueOf( img ) );
                }
                model.setDynamicImg( imgsArray );
            }

            ResultItem user = item.getItem( "user" );
            DriverModel driverModel = new DriverModel();
            driverModel.setNick( user.getString( "nick_name" ) );
            driverModel.setSex( user.getString( "sex" ) );
            driverModel.setVip( user.getBooleanValue( "vip", 1 ) );
            driverModel.setHeader( user.getString( "icon_url" ) );
            driverModel.setDriverId( dynamics.getString( "uid" ) );
            driverModel.setAttention( user.getIntValue( "follow" ) );
            model.setModel( driverModel );
            model.setGood( "1".equals( user.getString( "operate" ) ) );
            model.setDisGood( "0".equals( user.getString( "operate" ) ) );


            List<ResultItem> dynamicComments = dynamics.getItems( "comment_info" );
            ArrayList<DynamicCommentModel> dynamicCommentModels = new ArrayList<DynamicCommentModel>();
            if (dynamicCommentModels != null) {
                for (ResultItem dm : dynamicComments) {
                    DynamicCommentModel dynamicCommentModel = new DynamicCommentModel();
                    dynamicCommentModel.setCommentContent( dm.getString( "content" ) );
                    dynamicCommentModel.setToUserNick( dm.getString( "username" ) );
                    dynamicCommentModels.add( dynamicCommentModel );
                }
            }
            model.setCommentModels( dynamicCommentModels );
            models.add( model );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
    }

    @Override
    public void onRefresh() {
        hasData = true;
        page = 1;
        HttpManager.getDynamics( HttpType.REFRESH, getActivity(), this, page, 1 );
        refreshLayout.setOnLoadListener( null );
        refreshLayout.setOnLoadListener( this );
    }

    @Override
    public void onLoad() {
        if (hasData) {
            page++;
            HttpManager.getDynamics( HttpType.LOADING, getActivity(), this, page, 1 );
        } else {
            refreshLayout.setOnLoadListener( null );
            ToastCustom.makeText( getActivity(), "\u5df2\u7ecf\u5230\u5e95\u4e86", ToastCustom.LENGTH_SHORT ).show();
            refreshLayout.setLoading( false );
        }
    }

    @Override
    public void onPraiseClick(final DynamicModel model) {
        if (BeanUtils.isLogin()) {
            currentModel = model;
            if (currentModel.isDisGood()) {
                HttpManager.cancleDynamicsLike( CANCLESTEPON, getActivity(), new HttpResultListener() {
                    @Override
                    public void onSuccess(int what, ResultItem resultItem) {
                        if (1 == resultItem.getIntValue( "status" )) {
                            ResultItem item = resultItem.getItem( "data" );
                            if (item != null && 2 == item.getIntValue( "operate" )) {
                                model.setDisGood( false );
                                String txt = model.getDislikes();
                                try {
                                    int num = Integer.valueOf( txt ).intValue();
                                    num--;
                                    model.setDislikes( num + "" );
                                } catch (NumberFormatException e) {
                                }
                                HttpManager.dynamicsLike( PRAISE, getActivity(),
                                        new HttpResultListener() {
                                            @Override
                                            public void onSuccess(int what, ResultItem resultItem) {
                                                if (1 == resultItem.getIntValue( "status" )) {
                                                    ResultItem item = resultItem.getItem( "data" );
                                                    if (item != null && 1 == item.getIntValue( "operate" )) {
                                                        String txt = "";
                                                        int num = 0;
                                                        model.setGood( true );
                                                        txt = model.getLikes();
                                                        try {
                                                            num = Integer.valueOf( txt );
                                                        } catch (NumberFormatException e) {
                                                        }
                                                        num++;
                                                        model.setLikes( num + "" );
                                                        adapter.notifyDataSetChanged();
                                                        int bonus = item.getIntValue( "bonus" );
                                                        if (bonus > 0)
                                                            ToastCustom.makeText( getActivity(),
                                                                    "\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1"
																																				+ bonus + "\u91d1\u5e01",
                                                                    ToastCustom.LENGTH_SHORT ).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onError(int what, String error) {
                                            }
                                        }, currentModel.getDynamicModelId(), 1 );
                            }
                        }
                    }

                    @Override
                    public void onError(int what, String error) {
                    }
                }, model.getDynamicModelId(), 0 );
            } else if (currentModel.isGood()) {
                HttpManager.cancleDynamicsLike( CANCLEPRAISE, getActivity(),
                        new HttpResultListener() {
                            @Override
                            public void onSuccess(int what, ResultItem resultItem) {
                                if (1 == resultItem.getIntValue( "status" )) {
                                    ResultItem item = resultItem.getItem( "data" );
                                    if (item != null && 2 == item.getIntValue( "operate" )) {
                                        model.setGood( false );
                                        String txt = model.getLikes();
                                        try {
                                            int num = Integer.valueOf( txt ).intValue();
                                            num--;
                                            model.setLikes( num + "" );
                                        } catch (NumberFormatException e) {
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onError(int what, String error) {
                            }
                        }, currentModel.getDynamicModelId(), 1 );
            } else

            {
                HttpManager.dynamicsLike( PRAISE, getActivity(),
                        new HttpResultListener() {
                            @Override
                            public void onSuccess(int what, ResultItem resultItem) {
                                if (1 == resultItem.getIntValue( "status" )) {
                                    ResultItem item = resultItem.getItem( "data" );
                                    if (item != null && 1 == item.getIntValue( "operate" )) {
                                        String txt = "";
                                        int num = 0;
                                        model.setGood( true );
                                        model.setDisGood( false );
                                        txt = model.getLikes();
                                        try {
                                            num = Integer.valueOf( txt ).intValue();
                                        } catch (NumberFormatException e) {
                                        }
                                        num++;
                                        model.setLikes( num + "" );
                                        int bonus = item.getIntValue( "bonus" );
                                        if (bonus > 0)
                                            ToastCustom.makeText( getActivity(),
																								"\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1" + bonus + "\u91d1\u5e01",
                                                    ToastCustom.LENGTH_SHORT ).show();
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onError(int what, String error) {
                            }
                        }, currentModel.getDynamicModelId(), 1 );
            }
        } else {
            startActivity( new Intent( getActivity(), LoginActivity.class ) );
        }
    }

    @Override
    public void onStepOnClick(final DynamicModel model) {
        if (BeanUtils.isLogin()) {
            currentModel = model;
            if (currentModel.isGood()) {
                HttpManager.cancleDynamicsLike( 5585, getActivity(), new HttpResultListener() {
                    @Override
                    public void onSuccess(int what, ResultItem resultItem) {
                        if (1 == resultItem.getIntValue( "status" )) {
                            ResultItem item = resultItem.getItem( "data" );
                            if (item != null && 2 == item.getIntValue( "operate" )) {
                                model.setGood( false );
                                String txt = model.getLikes();
                                try {
                                    int num = Integer.valueOf( txt ).intValue();
                                    num--;
                                    model.setLikes( num + "" );
                                } catch (NumberFormatException e) {
                                }
                                HttpManager.dynamicsLike( STEPON, getActivity(),
                                        new HttpResultListener() {
                                            @Override
                                            public void onSuccess(int what, ResultItem resultItem) {
                                                if (1 == resultItem.getIntValue( "status" )) {
                                                    ResultItem item = resultItem.getItem( "data" );
                                                    if (item != null && 0 == item.getIntValue( "operate" )) {
                                                        model.setGood( false );
                                                        model.setDisGood( true );
                                                        String txt = model.getDislikes();
                                                        try {
                                                            int num = Integer.valueOf( txt ).intValue();
                                                            num++;
                                                            model.setDislikes( num + "" );
                                                            adapter.notifyDataSetChanged();
                                                        } catch (NumberFormatException e) {
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onError(int what, String error) {
                                            }
                                        }, currentModel.getDynamicModelId(), 0 );
                            }
                        }
                    }

                    @Override
                    public void onError(int what, String error) {
                    }
                }, model.getDynamicModelId(), 1 );
            } else if (currentModel.isDisGood()) {
                HttpManager.cancleDynamicsLike( CANCLESTEPON, getActivity(),
                        new HttpResultListener() {
                            @Override
                            public void onSuccess(int what, ResultItem resultItem) {
                                if (1 == resultItem.getIntValue( "status" )) {
                                    ResultItem item = resultItem.getItem( "data" );
                                    if (item != null && 2 == item.getIntValue( "operate" )) {
                                        model.setDisGood( false );
                                        String txt = model.getDislikes();
                                        try {
                                            int num = Integer.valueOf( txt ).intValue();
                                            num--;
                                            model.setDislikes( num + "" );
                                            adapter.notifyDataSetChanged();
                                        } catch (NumberFormatException e) {
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(int what, String error) {
                            }
                        }, currentModel.getDynamicModelId(), 0 );
            } else {
                HttpManager.dynamicsLike( STEPON, getActivity(),
                        new HttpResultListener() {
                            @Override
                            public void onSuccess(int what, ResultItem resultItem) {
                                if (1 == resultItem.getIntValue( "status" )) {
                                    ResultItem item = resultItem.getItem( "data" );
                                    if (item != null && 0 == item.getIntValue( "operate" )) {
                                        model.setDisGood( true );
                                        String txt = currentModel.getDislikes();
                                        try {
                                            int num = Integer.valueOf( txt ).intValue();
                                            num++;
                                            model.setDislikes( num + "" );
                                            adapter.notifyDataSetChanged();
                                        } catch (NumberFormatException e) {
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(int what, String error) {
                            }
                        }, currentModel.getDynamicModelId(), 0 );
            }
        } else {
            startActivity( new Intent( getActivity(), LoginActivity.class ) );
        }
    }

    @Override
    public void onCommentClick(DynamicModel model) {
        startActivity( new Intent( getActivity(), DynamicDetailsActivity.class )
                .putExtra( "dynamicId", model.getDynamicModelId() ) );
    }

    String currentDynamicsId;

    @Override
    public void onShareClick(final DynamicModel model) {
        currentDynamicsId = model.getDynamicModelId();
        if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
            sharePopupWindow.dismiss();
        }
        sharePopupWindow = null;
        sharePopupWindow = new SharePopupWindow( getActivity(), model );
        sharePopupWindow.showAtLocation( refreshLayout, Gravity.BOTTOM, 0, 0 );
        sharePopupWindow.setPlatformActionListener( this );
    }

    @Override
    public void onAttentionClick(final DriverModel model) {
        if (BeanUtils.isLogin()) {
            HttpManager.followOrCancel( 188, getActivity(), new HttpResultListener() {
                @Override
                public void onSuccess(int what, ResultItem resultItem) {
                    if (1 == resultItem.getIntValue( "status" )) {
                        model.setAttention( model.isAttention() == 1 ? 0 : 1 );
                        ListenerManager.sendOnDataRefreshListener( OnDataRefreshListener.ATT );
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(int what, String error) {
                }
            }, model.getDriverId(), model.isAttention() == 1 ? 2 : 1 );
        } else {
            startActivity( new Intent( getActivity(), LoginActivity.class ) );
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity( new Intent( getActivity(), DynamicDetailsActivity.class )
                .putExtra( "dynamicId", models.get( position ).getDynamicModelId() ) );
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        HttpManager.shareDynamics( 15, getActivity(), new HttpResultListener() {
            @Override
            public void onSuccess(int what, ResultItem resultItem) {
                Log.i( "share_dynamics", resultItem.getString( "msg" ) );
            }

            @Override
            public void onError(int what, String error) {
                Log.i( "share_dynamics", error );
            }
        }, currentDynamicsId );
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    public void onLoginStateChange(boolean isLogin) {
        refreshLayout.setRefreshing( true );
        hasData = true;
        page = 1;
        HttpManager.getDynamics( HttpType.REFRESH, getActivity(), this, page, 1 );
        refreshLayout.setOnLoadListener( null );
        refreshLayout.setOnLoadListener( this );
    }

    @Override
    public void onDataRefresh(int which) {
        if (which == OnDataRefreshListener.HOT) {
            hasData = true;
            page = 1;
            HttpManager.getDynamics( HttpType.REFRESH, getActivity(), this, page, 1 );
            refreshLayout.setOnLoadListener( null );
            refreshLayout.setOnLoadListener( this );
        }
    }

    @Override
    public void onDestroy() {
        ListenerManager.unRegisterOnDataRefreshListener( this );
        super.onDestroy();
    }

    @Override
    public void onLazyLoad() {
        refreshLayout.setRefreshing( true );
        HttpManager.getDynamics( HttpType.REFRESH, getActivity(), this, page, 1 );
    }
}
