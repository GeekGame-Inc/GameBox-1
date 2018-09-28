/** 
 * Project Name:GameBox 
 * File Name:NotificationFragmentPresenter.java 
 * Package Name:com.tenone.gamebox.presenter 
 * Date:2017-3-23����3:48:50 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.NotificationFragmentView;
import com.tenone.gamebox.view.activity.NotificationDetailsActivity;
import com.tenone.gamebox.view.fragment.MessageListFragment;

/**
 * ClassName:NotificationFragmentPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-23 ����3:48:50 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class NotificationFragmentPresenter extends BasePresenter implements
		OnClickListener {
	NotificationFragmentView fragmentView;
	Context mContext;

	public NotificationFragmentPresenter(NotificationFragmentView v, Context cxt) {
		this.fragmentView = v;
		this.mContext = cxt;
	}

	public void initListener() {
		getOpenServiceTv().setOnClickListener(this);
		getSystemNotificationTv().setOnClickListener(this);
	}

	public TextView getOpenServiceTv() {
		return fragmentView.getOpenServiceTv();
	}

	public TextView getSystemNotificationTv() {
		return fragmentView.getSystemNotificationTv();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_notification_openServicer:
			// ��������
			openOtherActivity(mContext, new Intent(mContext,
					NotificationDetailsActivity.class));
			break;
		case R.id.id_notification_systemNotification:
			// ϵͳ֪ͨ
            openOtherActivity(mContext, new Intent(mContext,
                    MessageListFragment.class));
			break;
		}
	}
}
