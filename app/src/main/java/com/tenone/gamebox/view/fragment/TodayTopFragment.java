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
import com.tenone.gamebox.mode.mode.SuperTopModel;
import com.tenone.gamebox.view.adapter.TopAdapter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class TodayTopFragment extends BaseFragment {
    @ViewInject(R.id.id_top_list)
    ListView listView;

    private SuperTopModel superTopModel;
    private TopAdapter adapter;


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
        adapter = new TopAdapter( getActivity(), superTopModel.getModels(), true );
        listView.setAdapter( adapter );
    }
}
