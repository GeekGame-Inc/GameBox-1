package com.tenone.gamebox.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.CoinSpinnerAdapter;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ToastUtils;

public class ReleaseQuestionActivity extends Activity implements Runnable, TextWatcher, HttpResultListener, AdapterView.OnItemSelectedListener {
	private static final int MAXLENGTH = 100;
	@ViewInject(R.id.id_window_release_question_reward)
	Spinner spinner;
	@ViewInject(R.id.id_window_release_question_edit)
	CustomizeEditText editText;
	@ViewInject(R.id.id_window_release_question_index)
	TextView indexTv;
	@ViewInject(R.id.id_window_release_question_release)
	TextView releaseTv;
	@ViewInject(R.id.id_window_num)
	TextView numTv;
	@ViewInject(R.id.id_window_release_question_root)
	RelativeLayout rootView;
	private String[] reward;
	private String gameId, money = "0";
	private CoinSpinnerAdapter adapter;
	private int num = 0;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		Intent intent = getIntent();
		reward = intent.getStringArrayExtra( "rewards" );
		gameId = intent.getStringExtra( "gameId" );
		num = intent.getIntExtra( "num", 0 );
		setContentView( R.layout.window_release_question );
		ViewUtils.inject( this );
		ViewGroup.LayoutParams params = rootView.getLayoutParams();
		params.width = DisplayMetricsUtils.getScreenWidth( this );
		rootView.setLayoutParams( params );
		initView();
	}

	private void initView() {
		if (!BeanUtils.isEmpty( reward )) {
			adapter = new CoinSpinnerAdapter( reward, this );
			spinner.setAdapter( adapter );
			spinner.setPrompt( "\u8bf7\u9009\u62e9\u60ac\u8d4f\u91d1\u989d" );
			spinner.setDropDownVerticalOffset( DisplayMetricsUtils.dipTopx( this, 30 ) );
			spinner.setOnItemSelectedListener( this );
		}
		releaseTv.setSelected( true );
		releaseTv.setClickable( false );
		editText.addTextChangedListener( this );
		Spanned text = getTiWenTxt();
		numTv.setText( text );
	}


	private Spanned getTiWenTxt() {
		String txt = num > 10000 ? (num / 10000) + "w+" : num + "";
		String text = getString( R.string.tiwen_txt, txt );
		return Html.fromHtml( text );
	}

	@OnClick({R.id.id_window_release_question_release, R.id.id_window_release_question_close})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_window_release_question_close:
				finish();
				break;
			case R.id.id_window_release_question_release:
				String text = editText.getText().toString().trim();
				if (TextUtils.isEmpty( text ) || text.length() < 5) {
					ToastUtils.showToast( this, "5~100\u5b57\u8303\u56f4\u5185\uff0c\u8bf7\u63cf\u8ff0\u60a8\u7684\u95ee\u9898" );
					return;
				}
				releaseTv.setClickable( false );
				HttpManager.putQuestion( HttpType.REFRESH, this, this, money, text, gameId );
				break;
		}
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (!TextUtils.isEmpty( s )) {
			String text = s.toString();
			if (!text.startsWith( " " ) && !text.endsWith( " " )) {
				int length = text.length();
				releaseTv.setSelected( length <= 4 );
				releaseTv.setClickable( length > 4 );
				indexTv.setText( length + "/" + MAXLENGTH );
			} else if (text.endsWith( " " ) && !text.startsWith( " " )) {
				int length = text.length() - 1;
				releaseTv.setSelected( length <= 4 );
				releaseTv.setClickable( length > 4 );
				indexTv.setText( length + "/" + MAXLENGTH );
			} else {
				int length = text.trim().length();
				releaseTv.setSelected( length <= 4 );
				releaseTv.setClickable( length > 4 );
				indexTv.setText( length + "/" + MAXLENGTH );
			}
		} else {
			indexTv.setText( "0/" + MAXLENGTH );
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			setResult( RESULT_OK );
			finish();
		} else {
			releaseTv.setClickable( true );
			ToastUtils.showToast( this, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastUtils.showToast( this, error );
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		money = reward[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void run() {

	}
}
