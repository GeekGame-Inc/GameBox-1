/** 
 * Project Name:GameBox 
 * File Name:FindPwdView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-4-27����11:48:10 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:FindPwdView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-27 ����11:48:10 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface FindPwdView {
	/* ���� */
	TitleBarView getTitleBarView();

	/* �ֻ��� */
	CustomizeEditText getPhoneView();

	/* ��֤�� */
	CustomizeEditText getCodeView();

	/* ������֤�� */
    TextView getSendCodeView();

	/* ע�� */
	Button getNextView();
}
