package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.adapter.PublishDynamicGridAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.custom.popupwindow.SelecteDynamicTypeWindow;
import com.tenone.gamebox.view.service.PublishDynamicService;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.FileSizeUtil;

import java.util.ArrayList;


public class PublishDynamicsActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, PublishDynamicGridAdapter.OnDeleteClickListener, SelecteDynamicTypeWindow.OnTypeSelectListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_publishDynamics_edit)
	CustomizeEditText editText;
	@ViewInject(R.id.id_publishDynamics_grid)
	GridView gridView;

	private static final int REQUEST_CODE = 110;
	private int max_count = 4;

	private PublishDynamicGridAdapter adapter;
	private ArrayList<String> paths = new ArrayList<String>();

	private SelecteDynamicTypeWindow window;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_publish_dynamic );
		ViewUtils.inject( this );
		initView();
	}

	private void initView() {
		titleBarView.setTitleText( "\u6211\u8981\u53d1\u8868" );
		titleBarView.setLeftImg( R.drawable.icon_back_grey );
		titleBarView.setRightText( "\u53d1\u5e03" );
		adapter = new PublishDynamicGridAdapter( this, paths );
		gridView.setAdapter( adapter );
		gridView.setOnItemClickListener( this );
		gridView.setOnItemLongClickListener( this );
		adapter.setOnDeleteClickListener( this );
	}

	@OnClick({R.id.id_titleBar_leftImg, R.id.id_titleBar_rightText})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_titleBar_leftImg:
				finish();
				break;
			case R.id.id_titleBar_rightText:
				String content = editText.getText().toString();
				if (TextUtils.isEmpty( content ) || content.length() < 10 || BeanUtils.isEmpty( paths )) {
					ToastCustom.makeText( this, "\u60a8\u6240\u53d1\u8868\u7684\u5185\u5bb9\u672a\u8fbe\u5230\u53d1\u8f66\u9700\u6c42\uff0c\u8bf7\u91cd\u65b0\u7f16\u8f91", ToastCustom.LENGTH_SHORT ).show();
					return;
				}
				for (String str : paths) {
					if (str.contains( ".gif" ) || str.contains( ".GIF" )) {
						if (FileSizeUtil.getFileOrFilesSize( str, FileSizeUtil.SIZETYPE_MB ) > 9.5) {
							ToastCustom.makeText( this, "\u4e0a\u4f20\u7684\u56fe\u7247\u592a\u5927\uff0c\u8bf7\u91cd\u65b0\u9009\u62e9",
									ToastCustom.LENGTH_SHORT ).show();
							return;
						}
					}
				}
				Intent intent = new Intent( this, PublishDynamicService.class );
				intent.putExtra( "content", content );
				intent.putExtra( "list", paths );
				startService( intent );
				ToastCustom.makeText( this, "\u6b63\u5728\u540e\u53f0\u4e0a\u4f20,\u8bf7\u7a0d\u7b49", ToastCustom.LENGTH_SHORT ).show();
				finish();
				break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == (adapter.getCount() - 1) && paths.size() < max_count) {
			if (window == null) {
				window = new SelecteDynamicTypeWindow( this, paths, REQUEST_CODE );
			}
			window.showAtLocation( titleBarView, Gravity.BOTTOM, 0, 0 );
			window.setListener( this );
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult( requestCode, resultCode, data );
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			paths.clear();
			paths.addAll( BGAPhotoPickerActivity.getSelectedImages( data ) );
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.setLongClickPosition( position );
		adapter.notifyDataSetChanged();
		return false;
	}

	@Override
	public void onDeleteClick(int position) {
		paths.remove( position );
		adapter.notifyDataSetChanged();
	}


	@Override
	public void onTypeSelect(int type) {
		max_count = 0 == type ? 4 : 1;
		adapter.setMaxSize( max_count );
		adapter.notifyDataSetChanged();
	}
}
