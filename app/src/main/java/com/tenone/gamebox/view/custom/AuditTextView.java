package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tenone.gamebox.R;


public class AuditTextView extends AppCompatTextView {
	private int status = 1;
	private int[] colorId = {R.color.label4, R.color.blue1, R.color.red};
	private String[] texts = new String[]{"\u5ba1\u6838\u6210\u529f", "\u7b49\u5f85\u5ba1\u6838", "\u5ba1\u6838\u5931\u8d25"};

	public AuditTextView(Context context) {
		this( context, null );
	}

	public AuditTextView(Context context, AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public AuditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super( context, attrs, defStyleAttr );
		init();
	}

	private void init() {
		setText( texts[status - 1] );
		setTextColor( getResources().getColor( colorId[status - 1] ) );
	}

	public void setStatus(int status) {
		this.status = status;
		setText( texts[status - 1] );
		setTextColor( getResources().getColor( colorId[status - 1] ) );
		invalidate();
	}
}
