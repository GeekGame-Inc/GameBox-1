package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnTopGetClickListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.SuperTopModel;
import com.tenone.gamebox.mode.mode.TopModel;
import com.tenone.gamebox.view.adapter.TopAdapter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class YesterdayTopFragment extends BaseFragment implements OnTopGetClickListener {
    @ViewInject(R.id.id_top_list)
    ListView listView;

    private SuperTopModel superTopModel;
    private TopAdapter adapter;
    private List<TopModel> models = new ArrayList<TopModel>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_top, container, false );
        ViewUtils.inject( this, view );
        superTopModel = (SuperTopModel) getArguments().get( "data" );
        initView();
        return view;
    }

    private void initView() {
        View headView = LayoutInflater.from( getActivity() ).inflate( R.layout.layout_top_header, null, false );
        headView.setLayoutParams( new AbsListView.LayoutParams( AbsListView.LayoutParams.MATCH_PARENT,
                DisplayMetricsUtils.dipTopx( getActivity(), 45 ) ) );
        listView.addHeaderView( headView );
        if (!BeanUtils.isEmpty( superTopModel )) {
            models.clear();
            models.addAll( superTopModel.getModels());
        }
        adapter = new TopAdapter( getActivity(), models, false );
        adapter.setOnTopGetClickListener( this );
        listView.setAdapter( adapter );
    }

    @Override
    public void onGetClick(int position) {
        buildProgressDialog();
        HttpManager.receiveReward( 1, getActivity(), new HttpResultListener() {
            @Override
            public void onSuccess(int what, ResultItem resultItem) {
                cancelProgressDialog();
                ToastCustom.makeText( getActivity(), resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
                if (1 == resultItem.getIntValue( "status" )) {
                    models.get( position ).setGot( 1 );
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int what, String error) {
                cancelProgressDialog();
                ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
            }
        } );

    }
}
