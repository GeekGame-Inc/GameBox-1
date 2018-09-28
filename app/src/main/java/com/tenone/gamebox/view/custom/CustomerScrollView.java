package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak" })
public class CustomerScrollView extends ScrollView {
	private ScrollViewListener scrollViewListener = null;
	private OnScrollListener onScrollListener;
	/**
	 * ��Ҫ�������û���ָ�뿪MyScrollView��MyScrollView���ڼ���������������������Y�ľ��룬Ȼ�����Ƚ�
	 */
	private int lastScrollY;

	public CustomerScrollView(Context context) {
		this(context, null);
	}

	public CustomerScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomerScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * ���ù����ӿ�
	 * 
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
	
	 public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
	        this.scrollViewListener = scrollViewListener;
	    }

	/**
	 * �����û���ָ�뿪MyScrollView��ʱ���ȡMyScrollView������Y���룬Ȼ��ص���onScroll������
	 */
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			int scrollY = CustomerScrollView.this.getScrollY();
			// ��ʱ�ľ���ͼ�¼�µľ��벻��ȣ��ڸ�5�����handler������Ϣ
			if (lastScrollY != scrollY) {
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);
			}
			if (onScrollListener != null) {
				onScrollListener.onScroll(scrollY);
			}
		}

	};

	/**
	 * ��дonTouchEvent�� ���û�������ScrollView�����ʱ��
	 * ֱ�ӽ�ScrollView������Y�������ص���onScroll�����У����û�̧���ֵ�ʱ��
	 * ScrollView���ܻ��ڻ��������Ե��û�̧�������Ǹ�5�����handler������Ϣ����handler���� ScrollView�����ľ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (onScrollListener != null) {
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			handler.sendMessageDelayed(handler.obtainMessage(), 5);
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * 
	 * �����Ļص��ӿ�
	 */
	public interface OnScrollListener {
		/**
		 * �ص������� ����MyScrollView������Y�������
		 * 
		 * @param scrollY
		 *            ��
		 */
		void onScroll(int scrollY);
	}

	 @Override
	    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
	        super.onScrollChanged(x, y, oldx, oldy);
	        if (scrollViewListener != null) {

	            if (oldy < y ) {// ��ָ���ϻ�������Ļ�����»�
	                scrollViewListener.onScroll(oldy,y,false);

	            } else if (oldy > y ) {// ��ָ���»�������Ļ�����ϻ�
	                scrollViewListener.onScroll(oldy,y,true);
	            }
	        }
	    }
	
	public  interface ScrollViewListener{//dy Y�Ử�����룬isUp �Ƿ񷵻ض���
         void onScroll(int oldy, int dy, boolean isUp);
    }
}
