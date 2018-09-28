package com.tenone.gamebox.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.base.BaseFragment;

public class FirstGuideFragment extends BaseFragment {
	@ViewInject(R.id.id_guide_image)
	ImageView imageView;

	private int[] rIds = {R.drawable.icon_guidance1, R.drawable.icon_guidance2, R.drawable.icon_guidance3};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_guide, container, false );
		ViewUtils.inject( this, view );
		int position = getArguments().getInt( "position" );
		imageView.setBackground( ContextCompat.getDrawable( getContext(), rIds[position] ) );
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		imageView.setBackground( null );
	}
}
