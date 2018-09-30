package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.fragment.MyQuestionFragment;

public class MyQuestionActivity extends BaseActivity {
	@ViewInject(R.id.id_toolbar_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_toolbar_rightIv)
	ImageView rightIv;
	@ViewInject(R.id.id_toolbar_title)
	TextView titleTv;

	MyQuestionFragment fragment;

	int type = 1;
	FragmentManager manager;
	FragmentTransaction transaction;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		type = getIntent().getIntExtra( "type", 1 );
		setContentView( R.layout.activity_my_question );
		ViewUtils.inject( this );
		initTitle();
		initView();
	}

	private void initView() {
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt( "type", type );
		fragment = new MyQuestionFragment();
		fragment.setArguments( bundle );
		transaction.add( R.id.id_my_question_fragment, fragment );
		transaction.commit();
	}

	private void initTitle() {
		toolbar.setTitle( "" );
		toolbar.setContentInsetsAbsolute( 0, 0 );
		setSupportActionBar( toolbar );
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled( true );
		}
		toolbar.setNavigationOnClickListener( v -> {
			finish();
		} );
		titleTv.setText( type == 1 ? "\u6211\u6765\u56de\u7b54" : "\u6211\u7684\u63d0\u95ee" );
		Drawable drawable = ContextCompat.getDrawable( this, R.drawable.icon_wen );
		DrawableCompat.setTintList( drawable, ColorStateList.valueOf( ContextCompat.getColor( this, R.color.gray_69 ) ) );
		rightIv.setBackground( drawable );
		rightIv.setOnClickListener( v ->
			startActivity( new Intent( this, WebActivity.class )
					.putExtra( "url", "http://boxapi.185sy.com/index.php?g=api&m=Consult&a=index" )
					.putExtra( "title", "\u95ee(\u7b54)\u5ba1\u6838\u89c4\u8303" ) ) );
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (transaction != null && fragment != null) {
			transaction.remove( fragment );
		}
		transaction = null;
		fragment = null;
	}
}
