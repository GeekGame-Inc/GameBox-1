package com.tenone.gamebox.view.custom.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.adapter.CoinSpinnerAdapter;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.BeanUtils;

public class ReleaseQuestionWindow extends PopupWindow implements AdapterView.OnItemSelectedListener, TextWatcher {
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

	private View rootView;
	private Context context;
	private LayoutInflater inflater;
	private String[] reward;
	private String gameId, num = "", money = "0";
	private CoinSpinnerAdapter adapter;

	public ReleaseQuestionWindow(Context context, String[] rewards, String gameId, String num) {
		super( context );
		this.reward = rewards;
		this.gameId = gameId;
		this.num = num;
		this.context = context;
		this.inflater = LayoutInflater.from( context );
		setFocusable( true );
		setTouchable( true );
		setOutsideTouchable( true );
		setAnimationStyle( R.anim.bottom_in );
		setInputMethodMode( PopupWindow.INPUT_METHOD_NEEDED );
		setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
		setWidth( ViewGroup.LayoutParams.MATCH_PARENT );
		setHeight( ViewGroup.LayoutParams.WRAP_CONTENT );
		setBackgroundDrawable( new ColorDrawable( 0xb0000000 ) );
		rootView = inflater.inflate( R.layout.window_release_question, null, false );
		setContentView( rootView );
		ViewUtils.inject( this, rootView );
		initView();
	}

	private void initView() {
		if (!BeanUtils.isEmpty( reward )) {
			adapter = new CoinSpinnerAdapter( reward, context.getApplicationContext() );
			spinner.setAdapter( adapter );
			spinner.setPrompt( "\u8bf7\u9009\u62e9\u60ac\u8d4f\u91d1\u989d" );
			spinner.setOnItemSelectedListener( this );
		}
		releaseTv.setSelected( true );
		releaseTv.setClickable( false );
		editText.addTextChangedListener( this );
		numTv.setText( "向" + num + "位玩过该游戏的玩家请教" );
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		money = reward[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s != null) {
			int length = s.length();
			releaseTv.setSelected( length <= 4 );
			releaseTv.setClickable( length > 4 );
		}
		if (!TextUtils.isEmpty( s )) {
			indexTv.setText( s.length() + "/" + MAXLENGTH );
		} else {
			indexTv.setText( "0/" + MAXLENGTH );
		}
	}
}
