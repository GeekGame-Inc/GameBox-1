/**
 * Project Name:GameBox
 * File Name:RegisterAble.java
 * Package Name:com.tenone.gamebox.mode.able
 * Date:2017-3-15����5:40:50
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.view;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ע�� ClassName:RegisterAble <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-15 ����5:40:50 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface RegisterView {

    /* ���� */
    TitleBarView getTitleBarView();

    /* �˺� */
    CustomizeEditText getAccountView();

    /* ���� */
    CustomizeEditText getPwdView();

    /* ���� */
    CustomizeEditText getPwdView2();

    /* �ֻ��� */
    CustomizeEditText getPhoneView();

    /* ��֤�� */
    CustomizeEditText getCodeView();

    /* ������֤�� */
    TextView getSendCodeView();

    /* ע�� */
    Button getRegisterView();

    /*Э��*/
    CheckBox getCheckBox();


    LinearLayout getLayout1();

    LinearLayout getLayout2();
}
