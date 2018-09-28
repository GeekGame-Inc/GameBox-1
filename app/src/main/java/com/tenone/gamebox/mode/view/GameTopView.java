/** 
 * Project Name:GameBox 
 * File Name:GameTopView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-10����11:26:39 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ��Ϸ���а� ClassName:GameTopView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-10 ����11:26:39 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameTopView {
	/* ��ȡ������ */
	View getSerachBar();

	/* ��ȡ���� */
	TitleBarView getTitleBarView();

	/* ��ȡˢ�¿ؼ� */
	RefreshLayout getRefreshLayout();

	/* ��ȡ�б� */
	ListView getListView();

	/* ��ȡIntent */
	Intent getIntent();
	
	TextView getDownBadgeTv();

	TextView getMessageBadgeTv();
}
