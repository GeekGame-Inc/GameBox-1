package com.tenone.gamebox.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftRightListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.TradingRecordModel;
import com.tenone.gamebox.view.adapter.TradingRecordAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.popupwindow.TradingConditionsWindow;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class HaveToBuyFragment extends BaseLazyFragment implements HttpResultListener, Runnable, SwipeRefreshLayout.OnRefreshListener, OnTabLayoutTextToLeftRightListener {
    @ViewInject(R.id.id_record_classify)
    TextView textView;
    @ViewInject(R.id.id_record_topLayout)
    LinearLayout linearLayout;
    @ViewInject(R.id.id_record_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_record_listview)
    MyRefreshListView listView;

    private List<TradingRecordModel> models = new ArrayList<TradingRecordModel>();
    private TradingRecordAdapter adapter;
    private Context context;
    private List<String> conditions = new ArrayList<String>();
    private TradingConditionsWindow conditionsWindow;
    private int type = 5;
    private View headerView;
    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate( R.layout.fragment_trading_record, container, false );
        ViewUtils.inject( this, view );
        linearLayout.setVisibility( View.VISIBLE );
        initView();
        return view;
    }

    private void initView() {
        new Thread( this ).start();
    }

    private void initList() {
			conditions.add( getString( R.string.all ) );
			conditions.add( "\u4ed8\u6b3e\u6210\u529f" );
			conditions.add( "\u4ea4\u6613\u6210\u529f" );
			conditions.add( "\u4ea4\u6613\u53d6\u6d88" );
    }

    private void initConditionsWindow() {
        if (conditionsWindow == null) {
            conditionsWindow = new TradingConditionsWindow( context, conditions, 0 );
            conditionsWindow.setOutsideTouchable( true );
            conditionsWindow.setOnTradingConditionsCallback( conditions -> {
                type = getType( conditions );
                textView.setText( conditions );
                refreshLayout.setRefreshing( true );
                request();
            } );
            conditionsWindow.setOnDismissListener( () -> {
                textView.setSelected( false );
            } );
        }
        conditionsWindow.showAsDropDown( linearLayout, 0, 1 );
        textView.setSelected( true );
    }

    private int getType(String conditions) {
        int t = 5;
        switch (conditions) {
					case "\u5168\u90e8":
						t = 5;
						break;
					case "\u4ed8\u6b3e\u6210\u529f":
						t = 1;
						break;
					case "\u4ea4\u6613\u6210\u529f":
						t = 4;
						break;
					case "\u4ea4\u6613\u53d6\u6d88":
						t = 3;
						break;
        }
        return t;
    }

    @Override
    public void onLazyLoad() {
        refreshLayout.setRefreshing( true );
        request();
    }

    private void request() {
        HttpManager.buyerRecord( HttpType.REFRESH, context, this, type );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        if (1 == resultItem.getIntValue( "status" )) {
            models.clear();
            List<ResultItem> data = resultItem.getItems( "data" );
            if (!BeanUtils.isEmpty( data )) {
                setData( data );
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast( context, resultItem.getString( "msg" ) );
        }
    }

    private void setData(List<ResultItem> data) {
        for (ResultItem item : data) {
            TradingRecordModel model = new TradingRecordModel();
            model.setGameName( item.getString( "game_name" ) );
            model.setMoney( item.getString( "money" ) );
            model.setStatus( item.getIntValue( "status" ) );
            model.setTime( item.getString( "create_time" ) );
            model.setTitle( item.getString( "title" ) );
            model.setReason( item.getString( "off_reason" ) );
            models.add( model );
        }
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
        ToastUtils.showToast( context, error );
    }

    @Override
    public void run() {
        initList();
        headerView = LayoutInflater.from( getActivity() ).inflate( R.layout.layout_header, null, false );
        tv = headerView.findViewById( R.id.id_header_textview );
        adapter = new TradingRecordAdapter( context, models, 0 );
        listView.post( () -> {
            tv.setText( "*\u6e29\u99a8\u63d0\u793a:\u4ea4\u6613\u6210\u529f\u540e\u8d26\u53f7\u5bc6\u7801\u5c06\u4f1a\u4ee5\u77ed\u4fe1\u7684\u5f62\u5f0f\u53d1\u9001\u7ed9\u60a8\uff0c\u8bf7\u6ce8\u610f\u67e5\u6536\uff0c\u5982\u679c\u957f\u65f6\u95f4\u672a\u6536\u5230\u8bf7\u53ca\u65f6\u8054\u7cfb\u5ba2\u670d\u8fdb\u884c\u6c9f\u901a" );
            listView.addHeaderView( headerView );
            listView.setAdapter( adapter );
            textView.setOnClickListener( v -> initConditionsWindow() );
            refreshLayout.setOnRefreshListener( this );
            ListenerManager.registerOnTabLayoutTextToLeftRightListener( this );
        } );
    }

    @Override
    public void onRefresh() {
        request();
    }

    @Override
    public void onTabLayoutTextToLeftRight(int margin) {
        if (adapter != null) {
            adapter.setPadding( margin );
            adapter.setSetPadding( true );
        }
        if (tv != null) {
            int dp10 = DisplayMetricsUtils.dipTopx( getActivity(), 10 );
            tv.setPadding( margin, dp10, margin, dp10 );
        }
        linearLayout.setPadding( margin, 0, margin, 0 );
    }

    @Override
    public void onDestroy() {
        ListenerManager.unRegisterOnTabLayoutTextToLeftRightListener( this );
        super.onDestroy();
    }
}
